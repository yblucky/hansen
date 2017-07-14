package com.api.service.impl;

import com.api.constant.RecordType;
import com.api.constant.UserStatusType;
import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.UserMapper;
import com.api.model.CardGrade;
import com.api.model.User;
import com.api.service.CardGradeService;
import com.api.service.UserService;
import com.api.util.PoundageUtil;
import com.api.util.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserServiceImpl extends CommonServiceImpl<User> implements UserService {
    @Autowired
    private UserMapper baseUserDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CardGradeService cardGradeService;

    @Override
    protected CommonDao<User> getDao() {
        return baseUserDao;
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }


    /**
     * 计算用户收入按比例存入三种币中
     *
     * @param incomAmt 用户收入金额
     * @param userId   用户id
     * @param type     流水类型
     */
    @Override
    @Transactional
    public void calc(Double incomAmt, String userId, RecordType type) {
        User user = userService.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        Double equityAmt = PoundageUtil.getPoundage(incomAmt * 0.7, 1d);
        Double payAmt = PoundageUtil.getPoundage(incomAmt * 0.2, 1d);
        Double tradeAmt = PoundageUtil.getPoundage(incomAmt * 0.1, 1d);
        User model = new User();
        model.setId(user.getId());
        model.setEquityAmt(equityAmt);
        model.setPayAmt(payAmt);
        model.setTradeAmt(tradeAmt);
        userService.updateById(user.getId(), model);
        // TODO: 2017/7/14 记录收货三种币收入信息
    }

    /**
     * 用户每周收益
     */
    @Override
    @Transactional
    public void weeklyIncome(User user) {
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode().intValue()) {
            System.out.println("用户不是激活状态");
            return;
        }
        if (ToolUtil.daysBetween(user.getReleaseTime(), new Date().toString()) < 7) {
            System.out.println("用户还没到释放时间");
            return;
        }
        if (user.getSumProfits() > user.getMaxProfits()) {
            System.out.println("用户累计收益已超过最大收益，不能继续领取");
            return;
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardLevel());
        //静态收益
        double incomeAmt = PoundageUtil.getPoundage(user.getInsuranceAmt() * cardGrade.getReleaseScale(), 1d);
        //静态收益转为三种币
        calc(incomeAmt, user.getId(), RecordType.RELASE);
        //用户剩余保单金额
        double insuranceAmt = PoundageUtil.getPoundage(user.getInsuranceAmt() - incomeAmt, 1d);
        User model = new User();
        //剩余保单金额
        model.setInsuranceAmt(insuranceAmt);
        //更新释放时间
        model.setReleaseTime(new Date().toString());
        //更新累计收益
        model.setSumProfits(PoundageUtil.getPoundage(user.getSumProfits() + incomeAmt, 1d));
        if (user.getSumProfits() > user.getMaxProfits()) {
            System.out.println("用户累计收益已超过最大收益，不能继续领取");
            //用户状态设为出局
            model.setStatus(UserStatusType.OUT.getCode());
            //TODO 记录出局信息
        } else {
            model.setStatus(UserStatusType.WAITACTIVATE.getCode());
            // TODO: 2017/7/14 用户需要重新激活才可以继续释放奖金
        }
        this.updateById(user.getId(), model);
        // TODO: 2017/7/14 记录用户释放信息

    }

    /**
     * 用户动态收益-直推奖
     *
     * @param pushUserId 推荐人id
     */
    @Override
    @Transactional
    public void pushBonus(String pushUserId) {
        User pushUser = this.readById(pushUserId);
        if (pushUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }
        //一代直推奖
        User parentUser = this.readById(pushUser.getFirstReferrer());
        //一代推荐人必须也是激活状态
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            //保单等级以最小为准
            Integer cardLevel = parentUser.getCardLevel() > pushUser.getCardLevel() ? pushUser.getCardLevel() : parentUser.getCardLevel();
            CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            //直推收益按两者最小保单金额*6%
            double incomeAmt = PoundageUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.06, 1d);
            //存入一代推荐人的三种钱包中
            calc(incomeAmt, parentUser.getId(), RecordType.PUSH);
            // TODO: 2017/7/14 记录一代直推奖记录
        }
        //二代直推奖
        User grandfatherUser = this.readById(pushUser.getSecondReferrer());
        //二代推荐人必须是激活状态
        if (grandfatherUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            //二代推荐人下属部门必须有三个以上是激活状态的
            User selModel = new User();
            selModel.setFirstReferrer(grandfatherUser.getId());
            selModel.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
            Integer count = this.readCount(selModel);
            if (count >= 3) {
                //保单等级以最小为准
                Integer cardLevel = grandfatherUser.getCardLevel() > pushUser.getCardLevel() ? pushUser.getCardLevel() : grandfatherUser.getCardLevel();
                CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                //直推收益按两者最小保单金额*4%
                double incomeAmt = PoundageUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.04, 1d);
                //存入二代推荐人的三种钱包中
                calc(incomeAmt, parentUser.getId(), RecordType.PUSH);
                // TODO: 2017/7/14 记录二代直推奖记录
            }
        }
    }

    /**
     * 用户动态收益-管理奖
     *
     * @param pushUserId 推荐人id
     */
    @Override
    @Transactional
    public void manageBonus(String pushUserId) {
        User pushUser = this.readById(pushUserId);
        if (pushUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }
        //一代管理奖
        User parentUser = this.readById(pushUser.getFirstReferrer());
        //一代推荐人必须也是激活状态并且一代推荐人的保单等级大于或等于推荐人
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode() && parentUser.getCardLevel().intValue() >= pushUser.getCardLevel()) {
            //保单等级以最小为准
            Integer cardLevel = pushUser.getCardLevel();
            CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            //管理收益按两者最小保单金额*2%
            double incomeAmt = PoundageUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.02, 1d);
            //存入一代推荐人的三种钱包中
            calc(incomeAmt, parentUser.getId(), RecordType.MANAGE);
            // TODO: 2017/7/14 记录一代管理奖记录
        }
        //二代管理奖
        User grandfatherUser = this.readById(pushUser.getSecondReferrer());
        //二代推荐人必须是激活状态
        if (grandfatherUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode() && grandfatherUser.getCardLevel().intValue() >= pushUser.getCardLevel()) {
            //二代推荐人下属部门必须有三个以上是激活状态的
            User selModel = new User();
            selModel.setFirstReferrer(grandfatherUser.getId());
            selModel.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
            Integer count = this.readCount(selModel);
            if (count >= 3) {
                //保单等级以最小为准
                Integer cardLevel = pushUser.getCardLevel();
                CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                //管理收益按两者最小保单金额*4%
                double incomeAmt = PoundageUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.01, 1d);
                //存入二代推荐人的三种钱包中
                calc(incomeAmt, parentUser.getId(), RecordType.MANAGE);
                // TODO: 2017/7/14 记录二代直推奖记录
            }
        }
    }
}
