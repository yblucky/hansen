package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.common.constant.GradeRecordType;
import com.hansen.common.constant.GradeType;
import com.hansen.common.constant.RecordType;
import com.hansen.common.constant.UserStatusType;
import com.hansen.common.numberutils.CurrencyUtil;
import com.hansen.common.toolutils.OrderNoUtil;
import com.hansen.common.toolutils.ToolUtil;
import com.hansen.mapper.UserMapper;
import com.hansen.model.CardGrade;
import com.hansen.model.Grade;
import com.hansen.model.TradeOrder;
import com.hansen.model.User;
import com.hansen.service.*;
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
    private CardGradeService cardGradeService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private TradeRecordService tradeRecordService;
    @Autowired
    private UserGradeRecordService userGradeRecordService;

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
    public void userIncomeAmt(Double incomAmt, String userId, RecordType type, String orderNo) {
        User user = this.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        Double equityAmt = CurrencyUtil.getPoundage(incomAmt * 0.07, 1d);
        Double payAmt = CurrencyUtil.getPoundage(incomAmt * 0.02, 1d);
        Double tradeAmt = CurrencyUtil.getPoundage(incomAmt * 0.01, 1d);
        User model = new User();
        model.setId(user.getId());
        model.setEquityAmt(equityAmt);
        model.setPayAmt(payAmt);
        model.setTradeAmt(tradeAmt);
        this.updateById(user.getId(), model);
        // TODO: 2017/7/14 记录收货三种币收入信息
        tradeRecordService.addRecord(userId, incomAmt, equityAmt, payAmt, tradeAmt, orderNo, type);
    }

    /**
     * 用户每周收益
     */
    @Override
    @Transactional
    public void weeklyIncomeAmt(User user) {
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
        String orderNo = OrderNoUtil.get();
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        //静态收益
        double incomeAmt = CurrencyUtil.getPoundage(user.getInsuranceAmt() * cardGrade.getReleaseScale(), 1d);
        //静态收益转为三种币
        userIncomeAmt(incomeAmt, user.getId(), RecordType.RELASE, orderNo);
        //用户剩余保单金额
        double insuranceAmt = CurrencyUtil.getPoundage(user.getInsuranceAmt() - incomeAmt, 1d);
        User model = new User();
        //剩余保单金额
        model.setInsuranceAmt(insuranceAmt);
        //更新释放时间
        model.setReleaseTime(new Date().toString());
        //更新累计收益
        model.setSumProfits(CurrencyUtil.getPoundage(user.getSumProfits() + incomeAmt, 1d));
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
        this.addTradeOrder(user.getId(), user.getId(), orderNo, incomeAmt, RecordType.RELASE);
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
            String orderNo = OrderNoUtil.get();
            //保单等级以最小为准
            Integer cardLevel = parentUser.getCardGrade() > pushUser.getCardGrade() ? pushUser.getCardGrade() : parentUser.getCardGrade();
            CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            //直推收益按两者最小保单金额*6%
            double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.06, 1d);
            //存入一代推荐人的三种钱包中
            userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.PUSH, orderNo);
            // TODO: 2017/7/14 记录一代直推奖记录
            this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.PUSH);
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
                String orderNo = OrderNoUtil.get();
                //保单等级以最小为准
                Integer cardLevel = grandfatherUser.getCardGrade() > pushUser.getCardGrade() ? pushUser.getCardGrade() : grandfatherUser.getCardGrade();
                CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                //直推收益按两者最小保单金额*4%
                double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.04, 1d);
                //存入二代推荐人的三种钱包中
                userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.PUSH, orderNo);
                // TODO: 2017/7/14 记录二代直推奖记录
                this.addTradeOrder(pushUserId, grandfatherUser.getId(), orderNo, incomeAmt, RecordType.PUSH);
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
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode() && parentUser.getCardGrade().intValue() >= pushUser.getCardGrade()) {
            String orderNo = OrderNoUtil.get();
            //保单等级以最小为准
            Integer cardLevel = pushUser.getCardGrade();
            CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            //管理收益按两者最小保单金额*2%
            double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.02, 1d);
            //存入一代推荐人的三种钱包中
            userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.MANAGE, orderNo);
            // TODO: 2017/7/14 记录一代管理奖记录
            this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.MANAGE);
        }
        //二代管理奖
        User grandfatherUser = this.readById(pushUser.getSecondReferrer());
        //二代推荐人必须是激活状态
        if (grandfatherUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode() && grandfatherUser.getCardGrade().intValue() >= pushUser.getCardGrade()) {
            //二代推荐人下属部门必须有三个以上是激活状态的
            User selModel = new User();
            selModel.setFirstReferrer(grandfatherUser.getId());
            selModel.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
            Integer count = this.readCount(selModel);
            if (count >= 3) {
                String orderNo = OrderNoUtil.get();
                //保单等级以最小为准
                Integer cardLevel = pushUser.getCardGrade();
                CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                //管理收益按两者最小保单金额*4%
                double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * 0.01, 1d);
                //存入二代推荐人的三种钱包中
                userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.MANAGE, orderNo);
                // TODO: 2017/7/14 记录二代直推奖记录
                this.addTradeOrder(pushUserId, grandfatherUser.getId(), orderNo, incomeAmt, RecordType.MANAGE);
            }
        }
    }

    /**
     * 更新用户等级
     *
     * @param user
     */
    @Override
    @Transactional
    public void reloadUserGrade(User user) throws Exception {
        if (user.getGrade() == null || "".equals(user.getGrade())) {
            user.setGrade(0);
        }
        Grade userGrade = gradeService.getUserGrade(user.getId());
        if (userGrade != null && userGrade.getGrade().intValue() > user.getGrade().intValue()) {
            User model = new User();
            model.setGrade(userGrade.getGrade());
            this.updateById(user.getId(), model);
            // TODO: 2017/7/17 记录会员等级升级记录
            Integer historyGrade = user.getGrade();
            user = this.readById(user.getId());
            userGradeRecordService.addGradeRecord(user, GradeRecordType.GRADEUPDATE, historyGrade);
        }
    }

    /**
     * 用户动态收益-级差奖
     *
     * @param userId 推荐人id
     */
    @Override
    public void differnceBonus(String userId) {
        User user = this.readById(userId);
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }
        //用户激活的保单等级
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        double insuranceAmt = cardGrade.getInsuranceAmt();
        double bonusScale = 0d;
        //用户所处的等级
        Grade grade = gradeService.getGradeDetail(user.getGrade());
        if (grade != null) {
            bonusScale = grade.getRewardScale();
        } else {
            user.setGrade(0);
        }
        User childUser = user;
        //1星到5星的奖励比率为 3% < 5% < 10% < 15% < 20% 平级奖 1% 最多领取21%
        for (int i = 0; i < 5; i++) {
            User parentUser = this.readById(childUser.getId());
            if (parentUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
                System.out.println("用户未激活保单");
                return;
            }
            if (parentUser.getGrade() == null || parentUser.getGrade().intValue() < 1) {
                System.out.println("用户业绩未达标，不能领取级差奖");
                //中断级差
                return;
            }
            //上级小于下级
            if (parentUser.getGrade().intValue() < user.getGrade().intValue()) {
                // TODO: 2017/7/17 级差中断是否要记录
                //中断级差
                return;
            }
            //上下级平级且等级都超过二星
            if (parentUser.getGrade().intValue() > GradeType.GRADE1.getCode().intValue() && parentUser.getGrade().intValue() == user.getGrade().intValue()) {
                // TODO: 2017/7/17 领取平级奖  保单金额*1%
                String orderNo = OrderNoUtil.get();
                double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * 0.01, 1d);
                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.EQUALITY, orderNo);
                //领取平级奖记录
                this.addTradeOrder(userId, parentUser.getId(), orderNo, incomeAmt, RecordType.EQUALITY);
                //中断级差
                return;
            }
            //上下级形成级差
            if (parentUser.getGrade().intValue() > user.getGrade().intValue()) {
                String orderNo = OrderNoUtil.get();
                //第一级由激活保单的用户自己领取
                if (bonusScale > 0 && i == 0) {
                    double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * bonusScale, 1d);
                    this.userIncomeAmt(incomeAmt, user.getId(), RecordType.DIFFERENT, orderNo);
                }
                // TODO: 2017/7/17 领取级差奖  保单金额*1%
                Grade parentGrade = gradeService.getGradeDetail(parentUser.getGrade());
                //当前可领取的比率减去上一级的比率
                double scale = CurrencyUtil.getPoundage(parentGrade.getRewardScale() - bonusScale, 1d);
                double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * scale, 1d);
                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.DIFFERENT, orderNo);
                bonusScale = parentGrade.getRewardScale();
                //领取级差奖
                this.addTradeOrder(userId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT);
            }
            childUser = parentUser;
        }
    }

    //新增用户收入记录
    private void addTradeOrder(String sendUserId, String receviceUserId, String order, Double amt, RecordType recordType) {
        TradeOrder model = new TradeOrder();
        model.setSendUserId(sendUserId);
        model.setReceviceUserId(receviceUserId);
        model.setOrderNo(order);
        model.setAmt(amt);
        model.setConfirmAmt(amt);
        model.setPoundage(0d);
        model.setSource(recordType.getCode());
        model.setRemark(recordType.getMsg());
        tradeOrderService.create(model);
    }
}
