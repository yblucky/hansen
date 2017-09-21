package com.service.impl;

import com.base.dao.CommonDao;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.base.service.impl.CommonServiceImpl;
import com.constant.*;
import com.mapper.UserMapper;
import com.model.*;
import com.service.*;
import com.utils.DateUtils.DateUtils;
import com.utils.codeutils.Md5Util;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @date 2016年11月27日
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements UserService {
    @Autowired
    private UserMapper userMapper;
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
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserDepartmentService userDepartmentService;
    @Autowired
    private ActiveCodeService activeCodeService;
    @Autowired
    private TransferCodeService transferCodeService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private UserSignService userSignService;
    @Autowired
    private WalletOrderService walletOrderService;

    @Override
    protected CommonDao<User> getDao() {
        return userMapper;
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
    public void userIncomeAmt(Double incomAmt, String userId, RecordType type, String orderNo) throws Exception {
        User user = this.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        Map<String, Double> map = tradeOrderService.getCoinNoFromRmb(incomAmt);
        Double payAmt = map.get("payAmt");
        Double tradeAmt = map.get("tradeAmt");
        Double equityAmt = map.get("equityAmt");
        this.updatePayAmtByUserId(userId, payAmt);
        this.updateTradeAmtByUserId(userId, tradeAmt);
        this.updateEquityAmtByUserId(userId, equityAmt);
        this.updateSumProfitsByUserId(userId, incomAmt);
        // TODO: 2017/7/14 记录收货三种币收入信息
        tradeRecordService.addRecord(userId, incomAmt, equityAmt, payAmt, tradeAmt, orderNo, type);
    }

    /**
     * 用户每周收益    现在改为做完7次任务后获取奖励(后台参数维护)
     */
    @Override
    @Transactional
    public void weeklyIncomeAmt(User user) throws Exception {
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode().intValue()) {
            System.out.println("用户不是激活状态");
            return;
        }

        if (user.getSumProfits() > user.getMaxProfits()) {
            System.out.println("用户累计收益已超过最大收益，不能继续领取");
            return;
        }
//        this.reloadUserGrade(user);
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        //静态收益
        double incomeAmt = CurrencyUtil.getPoundage(user.getInsuranceAmt() * cardGrade.getReleaseScale(), 1d);
        //静态收益转为三种币
//        userIncomeAmt(incomeAmt, user.getId(), RecordType.RELASE, orderNo);
        //用户剩余保单金额
        double insuranceAmt = CurrencyUtil.getPoundage(user.getInsuranceAmt() - incomeAmt, 1d);
        User model = new User();
        //剩余保单金额
        model.setInsuranceAmt(insuranceAmt);
        //更新释放时间
        model.setReleaseTime("");

        if (user.getSumProfits() > user.getMaxProfits()) {
            System.out.println("用户累计收益已超过最大收益，不能继续领取");
            //用户状态设为出局
            model.setStatus(UserStatusType.OUT.getCode());
            //TODO 记录出局信息
        } else {
            model.setStatus(UserStatusType.OUT.getCode());
            //用户需要重新激活才可以继续释放奖金
        }
        this.updateById(user.getId(), model);
        // TODO记录用户释放信息
        String remark = "收益释放：会员" + user.getUid() + "领取任务收益";
        String orderNo = OrderNoUtil.get() + "-" + RecordType.RELASE.toString();
        TradeOrder tradeOrder = this.addTradeOrder(user.getId(), user.getId(), orderNo, incomeAmt, RecordType.RELASE, 1, -1, remark, OrderStatus.HANDLED.getCode());

        userSignService.addUserSign(user.getId(), incomeAmt, SignType.WAITING_SIGN, "静态释放，新增奖励发放记录");

        //判断是否有管理奖
        //写入管理奖待做任务领取奖励记录，有则写
        this.manageBonus(user.getId(), tradeOrder, incomeAmt);
    }

    /**
     * 用户动态收益-直推奖
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    @Transactional
    public void pushBonus(String pushUserId, TradeOrder order) throws Exception {
        logger.error("--------------------开始计算直推奖：" + order + "----------------------");
        User pushUser = this.readById(pushUserId);
        if (pushUser == null) {
            logger.error("--------------------找不到用户：" + order + "----------------------");
            return;
        }
        if (order == null) {
            logger.error("--------------------找不到订单 " + order + "----------------------");
            return;
        }
        String orderNo = order.getOrderNo();
     /*
      处于一个事务，用户状态可能未来及更新提交，查出来的状态还是待激活
      if (pushUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }*/
        //一代直推奖
//        this.reloadUserGrade(pushUser.getFirstReferrer());
        User parentUser = this.readById(pushUser.getFirstReferrer());
        User grandfatherUser = this.readById(pushUser.getSecondReferrer());
        if (parentUser == null) {
            return;
        }
        Double pushRewardBaseAmt = order.getAmt();
        if (pushRewardBaseAmt == null || pushRewardBaseAmt.doubleValue() == 0) {
            logger.error("--------------------保单金额为空，数据错误 " + order + "----------------------");
            return;
        }
        Boolean isOrignal = OrderType.INSURANCE_ORIGIN.getCode() == order.getSource();
        if (isOrignal) {
            //如果是补差升级
            if (order.getCardGrade() == null) {
                logger.error("--------------------补差升级，数据错误，用户目标卡等级数据没有写入 " + order + "----------------------");
            }
        }
        Boolean isHasPushFirst = true;
        Boolean isHasPushSecond = true;

        if (parentUser.getSumProfits() > parentUser.getMaxProfits()) {
            logger.error("用户累计收益已超过最大收益，不能继续领取");
            return;
        }
        //一代推荐人必须也是激活状态
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            if (isOrignal) {
                if (order.getCardGrade() >= parentUser.getCardGrade()) {
                    logger.error("--------------------补差升级，升级用户的卡等级高过上级，无奖励" + order + "----------------------");
                    isHasPushFirst = false;
                }
            }
            if (isHasPushFirst) {
                //保单等级以最小为准
                Integer cardLevel = parentUser.getCardGrade() > pushUser.getCardGrade() ? pushUser.getCardGrade() : parentUser.getCardGrade();
                CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                if (isOrignal) {
                    //如果是补差升级，就按差价算
                    cardGrade.setInsuranceAmt(pushRewardBaseAmt);
                }
                //直推收益按两者最小保单金额*6%
                double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHFIRSTREFERRERSCALE)), 1d);
           /*存入一代推荐人的三种钱包中  需要做完7次任务才可以分4次领取
            userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.PUSH, orderNo);*/
                // TODO 记录一代直推奖记录
                String remark = "直推奖：会员" + pushUser.getUid() + "报单一代奖励";
                orderNo = order.getOrderNo() + "-" + RecordType.PUSH.toString() + "-" + 1;
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.PUSH, -1, -1, remark, OrderStatus.HANDING.getCode());
            }
        }

        //二代直推奖

        if (grandfatherUser == null) {
            return;
        }
        if (grandfatherUser.getSumProfits() > grandfatherUser.getMaxProfits()) {
            logger.error("用户累计收益已超过最大收益，不能继续领取");
            return;
        }
        //二代推荐人必须是激活状态
        if (grandfatherUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            if (isOrignal) {
                if (order.getCardGrade() >= grandfatherUser.getCardGrade()) {
                    logger.error("--------------------补差升级，升级用户的卡等级高过上级，无奖励" + order + "----------------------");
                    isHasPushSecond = false;
                }
            }
            if (isHasPushSecond) {
                grandfatherUser = this.readById(pushUser.getSecondReferrer());
                //二代推荐人下属部门必须有三个以上是激活状态的
                User selModel = new User();
                selModel.setFirstReferrer(grandfatherUser.getId());
                selModel.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
                Integer count = this.readCount(selModel);
                if (count >= 3) {
                    //保单等级以最小为准
                    Integer cardLevel = grandfatherUser.getCardGrade() > pushUser.getCardGrade() ? pushUser.getCardGrade() : grandfatherUser.getCardGrade();
                    CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                    if (isOrignal) {
                        //如果是补差升级，就按差价算
                        cardGrade.setInsuranceAmt(pushRewardBaseAmt);
                    }
                    //直推收益按两者最小保单金额*4%
                    double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHSECONDREFERRERSCALE)), 1d);
                    //存入二代推荐人的三种钱包中
//                userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.PUSH, orderNo);
                    // TODO 记录二代直推奖记录
                    String remark = "直推奖：会员" + pushUser.getUid() + "报单二代奖励";
                    orderNo = order.getOrderNo() + "-" + RecordType.PUSH.toString() + "-" + 2;
                    this.addTradeOrder(pushUserId, grandfatherUser.getId(), orderNo, incomeAmt, RecordType.PUSH, -1, -1, remark, OrderStatus.HANDING.getCode());
                }
            }

        }
    }

    /**
     * 用户动态收益-管理奖   A--B--C
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    @Transactional
    public Double manageBonus(String pushUserId, TradeOrder order, Double weekAmt) throws Exception {
        logger.error("--------------------开始计算管理奖：" + order + "----------------------");
        Double manage = 0d;
        User pushUser = this.readById(pushUserId);
        if (pushUser == null) {
            System.out.println("找不到用户....");
            return 0d;
        }
        //一代管理奖
//        this.reloadUserGrade(pushUser.getFirstReferrer());
        User parentUser = this.readById(pushUser.getFirstReferrer());
//        this.reloadUserGrade(parentUser);
        this.readById(pushUser.getFirstReferrer());
        if (parentUser == null) {
            return 0d;
        }
        Integer targetCardGrade = 1;
        Integer cardLevel = pushUser.getCardGrade();
        CardGrade cardGrade = null;
        //一代推荐人必须也是激活状态
        logger.error("--------------------开始计算管理奖一代：" + order.getOrderNo() + "----------------------");

        if (parentUser.getSumProfits() > parentUser.getMaxProfits()) {
            logger.error("用户累计收益已超过最大收益，不能继续领取");
            return 0d;
        }
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            //保单等级以最小为准
            cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            if (cardGrade == null) {
                logger.error("卡参数等级错误");
                return 0d;
            }
            //管理收益按两者最小保单金额*2%
            if (cardLevel > parentUser.getCardGrade()) {
                //直推用户较大，按上级算
                targetCardGrade = parentUser.getCardGrade();
            } else {
                targetCardGrade = cardLevel;
            }
            cardGrade = cardGradeService.getUserCardGrade(targetCardGrade);
            if (cardGrade == null) {
                logger.error("卡参数等级错误");
                return 0d;
            }
            Double manageFirstScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.MANAGEFIRSTREFERRERSCALE));
            double incomeAmt = CurrencyUtil.getPoundage(weekAmt * cardGrade.getReleaseScale() * manageFirstScale, 1d);
            //存入一代推荐人的三种钱包中
//            userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.MANAGE, orderNo);
            // TODO 记录一代管理奖记录
            String remark = "管理奖：会员" + pushUser.getUid() + "静态收益，一代管理奖励" + incomeAmt;
            String orderNo = order.getOrderNo() + "-" + RecordType.MANAGE.toString() + "-" + 1;
            manage += incomeAmt;
            this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.MANAGE, 1, -1, remark, OrderStatus.HANDING.getCode());
//            userSignService.addUserSign(parentUser.getId(), incomeAmt, SignType.WAITING_SIGN, remark);
        }
        //二代管理奖
        User grandfatherUser = this.readById(pushUser.getSecondReferrer());
        if (grandfatherUser == null) {
            return manage;
        }
        if (grandfatherUser.getSumProfits() > grandfatherUser.getMaxProfits()) {
            logger.error("用户累计收益已超过最大收益，不能继续领取");
            return 0d;
        }
        //二代推荐人必须是激活状态
        logger.error("--------------------开始计算管理奖二代：" + order + "----------------------");
        if (grandfatherUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
//            this.reloadUserGrade(pushUser.getSecondReferrer());
            grandfatherUser = this.readById(pushUser.getSecondReferrer());
            if (grandfatherUser.getCardGrade() > cardLevel) {
                //A>C
                targetCardGrade = cardLevel;
            } else {
                targetCardGrade = grandfatherUser.getCardGrade();
            }
            cardGrade = cardGradeService.getUserCardGrade(targetCardGrade);
            if (cardGrade == null) {
                logger.error("卡参数等级错误");
                return manage;
            }
            //二代推荐人下属部门必须有三个以上是激活状态的
            User selModel = new User();
            selModel.setFirstReferrer(grandfatherUser.getId());
            selModel.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
            Integer count = this.readCount(selModel);
            if (count >= 3) {
                //保单等级以最小为准
                //管理收益按两者最小保单金额*4%
                Double manageSecondReferrerScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.MANAGESECONDREFERRERSCALE));
                double incomeAmt = CurrencyUtil.getPoundage(weekAmt * cardGrade.getReleaseScale() * manageSecondReferrerScale, 1d);
                //存入二代推荐人的三种钱包中
//                userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.MANAGE, orderNo);
                // TODO 记录二代管理奖记录
//                this.addTradeOrder(pushUserId, grandfatherUser.getId(), orderNo, incomeAmt, RecordType.MANAGE, 0, 0);
                String remark = "管理奖：会员" + pushUser.getUid() + "静态收益，二代管理奖励" + incomeAmt;
                String orderNo = order.getOrderNo() + "-" + RecordType.MANAGE.toString() + "-" + 2;
                manage += incomeAmt;
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.MANAGE, 1, -1, remark, OrderStatus.HANDING.getCode());
//                userSignService.addUserSign(parentUser.getId(), incomeAmt, SignType.WAITING_SIGN, remark);
            }
        }
        logger.error("--------------------结束计算管理奖：" + order + "----------------------");
        return manage;
    }


    /**
     * 用户动态收益-级差奖
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    public void differnceBonus(String pushUserId, TradeOrder order) throws Exception {
        if (order == null) {
            logger.error("--------------------找不到订单 " + order + "----------------------");
            return;
        }
        logger.error("--------------------开始计算极差奖：" + order.getOrderNo() + "----------------------");
        User activeUser = this.readById(pushUserId);
        if (activeUser == null) {
            logger.error("找不到激活保单的用户....");
            return;
        }

        User user = this.readById(activeUser.getFirstReferrer());
        if (user == null) {
            logger.error("找不到激活保单的用户的直接推荐人....");
            return;
        }
        String orderNo = order.getOrderNo();
        double insuranceAmt = order.getConfirmAmt();
        //用户所处的等级所能拿到奖金的比例
        double bonusScale = 0d;
        Double differRewardBaseAmt = order.getAmt();
        if (differRewardBaseAmt == null || differRewardBaseAmt.doubleValue() == 0) {
            logger.error("--------------------计算级差奖 保单金额为空，数据错误 " + order + "----------------------");
            return;
        }
        Boolean isOrignal = OrderType.INSURANCE_ORIGIN.getCode() == order.getSource();
        if (isOrignal) {
            //如果是补差升级
            if (order.getCardGrade() == null) {
                logger.error("--------------------计算级差奖 补差升级，数据错误，用户目标卡等级数据没有写入 " + order + "----------------------");
            }
        }

        User childUser = user;
        //1星到5星的奖励比率为 2% < 4% < 6% < 8% < 12% < 16% < 20% 平级奖 2% 最多领取22%
        for (int i = 0; i < 7; i++) {
            logger.error("--------------------开始计算极差奖：" + order + "-------第几级比较  " + i + " ---------------");
//            this.reloadUserGrade(childUser.getFirstReferrer());
            User parentUser = this.readById(childUser.getFirstReferrer());
            if (parentUser == null) {
                return;
            }
            Grade parentGrade = gradeService.getGradeDetail(parentUser.getGrade());
            if (parentGrade != null) {
                bonusScale = parentGrade.getRewardScale();
            } else {
                parentUser.setGrade(0);
                logger.error("第 " + i + " 次计算根据用户等级参数表获取级差奖结算比例错误....");
                return;
            }
//            bonusScale = parentGrade.getRewardScale();
            if (parentUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
                logger.error("用户未激活保单");
                return;
            }
            if (parentUser.getGrade() == null || parentUser.getGrade().intValue() < 1) {
                logger.error("--------------------用户业绩未达标,等级小于1，不能领取级差奖---------------");
                //中断级差
                return;
            }
            //上级小于下级
            if (parentUser.getGrade().intValue() < childUser.getGrade().intValue()) {
                logger.error("--------------------中断级差---------------");
                //中断级差
                return;
            }
            //上下级平级且等级都超过二星
            if (parentUser.getGrade().intValue() > GradeType.GRADE1.getCode().intValue() && parentUser.getGrade().intValue() == childUser.getGrade().intValue()) {
                //领取平级奖  保单金额*2%
                Double sameRewradScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.SAMEREWARDSCALESCALE));
                double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * sameRewradScale, 1d);
//                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.SAME, orderNo);
                //领取平级奖记录
                orderNo = order.getOrderNo() + "-" + RecordType.SAME.toString() + "-" + activeUser.getGrade() + "-" + (i + 1) + "-" + parentUser.getGrade();
                if (parentUser.getSumProfits() > parentUser.getMaxProfits()) {
                    logger.error("用户累计收益已超过最大收益，不能继续领取");
                    return ;
                }
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.SAME, -1, -1, "", OrderStatus.HANDING.getCode());
                logger.error("--------------------上下级平级且等级都超过二星，领取平级奖,此单金额为：" + insuranceAmt + ",平级奖奖金比例为:" + sameRewradScale + ",计算后金额为：" + incomeAmt + "---------------");
                //中断级差
                return;
            }
            //上下级形成级差
            if (parentUser.getGrade().intValue() > childUser.getGrade().intValue()) {
                //第一级由激活保单的用户自己领取
                logger.error("------------------此处有极差形成,计算奖金比例-----------------");
                if (bonusScale > 0 && i == 0) {
                    double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * bonusScale, 1d);
                    orderNo = order.getOrderNo() + RecordType.DIFFERENT.toString() + "-" + (i + 1) + childUser.getGrade() + "-" + parentUser.getGrade();
//                    this.userIncomeAmt(incomeAmt, user.getId(), RecordType.DIFFERENT, orderNo);
                    logger.error("--------------------第一一个极差，领取平级奖,此单金额为：" + insuranceAmt + ",平级奖奖金比例为:" + bonusScale + ",计算后金额为：" + incomeAmt + "---------------");
                    if (parentUser.getSumProfits() > parentUser.getMaxProfits()) {
                        logger.error("用户累计收益已超过最大收益，不能继续领取");
                        return ;
                    }
                    this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, -1, -1, "", OrderStatus.HANDING.getCode());
                } else {
                    parentGrade = gradeService.getGradeDetail(parentUser.getGrade());
                    Grade childGrade = gradeService.getGradeDetail(childUser.getGrade());
                    //当前可领取的比率减去上一级的比率
                    double scale = CurrencyUtil.getPoundage(parentGrade.getRewardScale() - childGrade.getRewardScale(), 1d);
                    if (scale <= 0) {
                        logger.error("---------------" + (i + 1) + " 个极差 ,此处计算奖金，前面的等级已经分完，比例为 0 了 ，直接返回----------------");
                        return;
                    }
                    double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * scale, 1d);
//                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.DIFFERENT, orderNo);
                    bonusScale = parentGrade.getRewardScale();
                    //领取级差奖
                    logger.error("--------------------第 " + (i + 1) + " 个极差，领取平级奖,此单金额为：" + insuranceAmt + ",平级奖奖金比例为:" + bonusScale + ",计算后金额为：" + incomeAmt + "---------------");
                    String remark = "级差奖：会员" + activeUser.getUid() + "报单平级奖励";
                    orderNo = order.getOrderNo() + "-" + RecordType.DIFFERENT.toString() + "-" + (i + 1) + user.getGrade() + "-" + parentUser.getGrade();
//                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, 0, 0);
                    if (parentUser.getSumProfits() > parentUser.getMaxProfits()) {
                        logger.error("用户累计收益已超过最大收益，不能继续领取");
                        return ;
                    }
                    this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, -1, -1, "", OrderStatus.HANDING.getCode());
                }
            }
            childUser = parentUser;
        }
        logger.error("--------------------结束计算极差奖：" + order + "----------------------");
    }


    /**
     * 用户动态收益-级差奖  新的
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    public void differnceBonusNew(String pushUserId, TradeOrder order) throws Exception {
        User user = this.readById(pushUserId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        String orderNo = order.getOrderNo();
        //用户激活的保单等级
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        double insuranceAmt = order.getConfirmAmt();
        //用户所处的等级所能拿到奖金的比例
        double bonusScale = 0d;
        User childUser = user;
        //1星到5星的奖励比率为 2% < 4% < 6% < 8% < 12% < 16% < 20% 平级奖 2% 最多领取22%
        for (int i = 0; i < 7; i++) {
//            this.reloadUserGrade(childUser.getFirstReferrer());
            User parentUser = this.readById(childUser.getId());
            if (parentUser == null) {
                return;
            }
            Grade parentGrade = gradeService.getGradeDetail(parentUser.getGrade());
            if (parentGrade != null) {
                bonusScale = parentGrade.getRewardScale();
            } else {
                parentUser.setGrade(0);
            }
            if (parentUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
                logger.error("用户未激活保单");
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
//            CardGrade cardGrade1=cardGradeService.getUserCardGrade(parentUser.getCardGrade());

            //上下级平级且等级都超过二星
            if (parentUser.getGrade().intValue() > GradeType.GRADE1.getCode().intValue() && parentUser.getGrade().intValue() == user.getGrade().intValue()) {
                // TODO: 2017/7/17 领取平级奖  保单金额*1%
                Double sameRewradScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.SAMEREWARDSCALESCALE));
                double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * sameRewradScale, 1d);
//                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.SAME, orderNo);
                //领取平级奖记录
                String remark = "平级奖：会员" + user.getUid() + "报单平级奖励";
                orderNo = order.getOrderNo() + RecordType.SAME.toString() + "-" + (i + 1) + user.getGrade() + "-" + parentUser.getGrade();
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.SAME, -1, -1, remark, OrderStatus.HANDING.getCode());
                //中断级差
                return;
            }
            //上下级形成级差
            if (parentUser.getGrade().intValue() > user.getGrade().intValue()) {
                //第一级由激活保单的用户自己领取
                if (bonusScale > 0 && i == 0) {
                    double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * bonusScale, 1d);
                    this.userIncomeAmt(incomeAmt, user.getId(), RecordType.DIFFERENT, orderNo);
                    String remark = "级差奖：会员" + user.getUid() + "报单一代奖励";
                    orderNo = order.getOrderNo() + RecordType.DIFFERENT.toString() + "-" + user.getGrade() + "-" + (i + 1) + "-" + parentUser.getGrade();
                    this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, -1, -1, remark, OrderStatus.HANDING.getCode());
                }
                // TODO: 2017/7/17 领取级差奖  保单金额*1%
                parentGrade = gradeService.getGradeDetail(parentUser.getGrade());
                //当前可领取的比率减去上一级的比率
                double scale = CurrencyUtil.getPoundage(parentGrade.getRewardScale() - bonusScale, 1d);
                double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * scale, 1d);
                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.DIFFERENT, orderNo);
                bonusScale = parentGrade.getRewardScale();
                //领取级差奖
//                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, 0, 0);
                String remark = "级差奖：会员" + user.getUid() + "报单奖励";
                orderNo = order.getOrderNo() + RecordType.DIFFERENT.toString() + "-" + (i + 1) + user.getGrade() + "-" + parentUser.getGrade();
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, -1, -1, remark, OrderStatus.HANDING.getCode());
            }
            childUser = parentUser;
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
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        if (user.getGrade() == null || "".equals(user.getGrade())) {
            user.setGrade(0);
        }
        Grade userGrade = gradeService.getUserGrade(user.getId());
        if (userGrade != null && userGrade.getGrade().intValue() > user.getGrade().intValue()) {
            User model = new User();
            model.setGrade(userGrade.getGrade());
            this.updateUserGradeByUserId(user.getId(), userGrade.getGrade());
            // TODO: 2017/7/17 记录会员等级升级记录
            Integer historyGrade = user.getGrade();
            user = this.readById(user.getId());
            UserDepartment userDepartment = new UserDepartment();
            model.setGrade(userGrade.getGrade());
            userDepartmentService.updateById(user.getId(), userDepartment);
            userGradeRecordService.addGradeRecord(user, GradeRecordType.GRADEUPDATE, historyGrade, userGrade.getGrade(), UpGradeType.STARGRADE.getCode(), OrderNoUtil.get());
        }
    }

    /**
     * 更新用户等级
     *
     * @param userId
     */
    @Override
    public void reloadUserGrade(String userId) throws Exception {
        logger.error("重新计算用户等级，用户id ：" + userId);
        User user = this.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        if (user.getGrade() == null || "".equals(user.getGrade())) {
            user.setGrade(0);
        }
        this.reloadUserGrade(user);
    }


    //新增用户管理奖记录
    private void addWeekLyTradeOrder(String sendUserId, String receviceUserId, String order, Double amt, RecordType recordType) {
        TradeOrder model = new TradeOrder();
        model.setSendUserId(sendUserId);
        model.setReceviceUserId(receviceUserId);
        model.setOrderNo(order);
        model.setAmt(amt);
        model.setConfirmAmt(amt);
        model.setPoundage(0d);
        model.setTaskCycle(0);
        model.setSignCycle(0);
        model.setSource(recordType.getCode());
        model.setRemark(recordType.getMsg());
        model.setStatus(OrderStatus.HANDING.getCode());
        model.setStatus(model.getStatus() == null ? 0 : model.getStatus());
        tradeOrderService.create(model);
    }


    //新增用户收入记录
    private TradeOrder addTradeOrder(String sendUserId, String receviceUserId, String order, Double amt, RecordType recordType, Integer rewardInterval, Integer taskInterval, String remark, Integer status) {
        User newUser = this.readById(sendUserId);
        TradeOrder model = new TradeOrder();
        model.setSendUserId(sendUserId);
        model.setReceviceUserId(receviceUserId);
        model.setOrderNo(order);
        model.setAmt(amt);
        model.setConfirmAmt(amt);
        model.setPoundage(0d);
        model.setSource(recordType.getCode());
        if (ToolUtil.isNotEmpty(remark)) {
            model.setRemark(remark);
        } else {
            model.setRemark(recordType.getMsg());
        }
        model.setStatus(status == null ? 0 : status);
        try {
            if (taskInterval >= 0) {
                model.setTaskCycle(taskInterval);
            } else {
                model.setTaskCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
            }
            if (rewardInterval > 0) {
                model.setSignCycle(rewardInterval);
            } else {
                model.setSignCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)));
            }

            if (recordType.getCode().intValue() == RecordType.RELASE.getCode().intValue()) {
                model.setStatus(OrderStatus.HANDING.getCode());
                model.setSignCycle(0);
                model.setTaskCycle(0);
            } else if (recordType.getCode().intValue() == RecordType.MANAGE.getCode().intValue()) {
                model.setStatus(OrderStatus.HANDING.getCode());
                model.setSignCycle(1);
                model.setTaskCycle(7);
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.setTaskCycle(7);
            model.setSignCycle(7);
        }

        tradeOrderService.create(model);
        return model;
    }

    /**
     * 点位升级  补差价升级
     *
     * @param userId            用户ID
     * @param targetCardGradeNo 升级等级
     */
    @Override
    @Transactional
    public void originUpgrade(String userId, Integer targetCardGradeNo) throws Exception {
        User user = this.readById(userId);
        if (user == null) {
            logger.error("找不到用户....");
            return;
        }
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            logger.error("用户未激活");
            return;
        }
        if (user.getCardGrade() >= targetCardGradeNo) {
            logger.error("点位升级只能从低往高升级！！！");
            return;
        }
        CardGrade targetCardGrade = cardGradeService.getUserCardGrade(targetCardGradeNo);
        if (targetCardGrade == null) {
            logger.error("用户补差价升级，目标等级不存在！！！");
            return;
        }
        CardGrade userCurrentCardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());

        //计算差价
        Integer differRegisterNo = targetCardGrade.getRegisterCodeNo() - userCurrentCardGrade.getRegisterCodeNo();
        Integer differActiceNo = targetCardGrade.getActiveCodeNo() - userCurrentCardGrade.getActiveCodeNo();
        Double insuranceAmt = targetCardGrade.getInsuranceAmt() - userCurrentCardGrade.getInsuranceAmt();
        Double differPayRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 4);
        Double differTradeRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 4);
        //人民币兑换支付币汇率
        Double payScale = parameterService.getScale(Constant.RMB_CONVERT_PAY_SCALE);
        //人民币兑换交易币汇率
        Double tradeScale = parameterService.getScale(Constant.RMB_CONVERT_TRADE_SCALE);
        //计算实际需要的币数量
        Double differPayAmt = CurrencyUtil.getPoundage(differPayRmbAmt, payScale, 4);
        Double differTradeAmt = CurrencyUtil.getPoundage(differTradeRmbAmt, tradeScale, 4);


        //更新用户相关信息
        User updateModel = new User();
        updateModel.setId(userId);
        updateModel.setCardGrade(targetCardGradeNo);
        updateModel.setRemark("用户补差价升级，由" + CardLevelType.fromCode(user.getCardGrade()).getMsg() + "升级到" + CardLevelType.fromCode(targetCardGradeNo).getMsg());
        this.updateById(userId, updateModel);

        //更新用户详情冻结信息
        UserDetail userDetail = userDetailService.readById(userId);
        UserDetail userDetailUpdateModel = new UserDetail();
        userDetailUpdateModel.setForzenTradeAmt(userDetail.getForzenTradeAmt() + differTradeAmt);
        userDetailUpdateModel.setForzenPayAmt(userDetail.getForzenPayAmt() + differPayAmt);
        userDetailService.updateById(userId, userDetailUpdateModel);

        //冻结币
        this.updateTradeAmtByUserId(userId, -differTradeAmt);
        this.updatePayAmtByUserId(userId, -differPayAmt);
        //TODO 写入冻结记录，后面完善，需要新定义枚举 WalletOrderType

        //扣除激活码
        activeCodeService.useActiveCode(userId, differActiceNo, "用户进行补差价升级使用消费码");

        //生成保单
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderNo(OrderNoUtil.get());
        tradeOrder.setAmt(insuranceAmt);
        tradeOrder.setSendUserId(userId);
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE_ORIGIN.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE_ORIGIN.getMsg());
        tradeOrder.setPayAmtScale(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)));
        tradeOrder.setTradeAmtScale(ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)));
        tradeOrder.setEquityAmtScale(0d);
        tradeOrder.setConfirmAmt(insuranceAmt);
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        try {
            tradeOrder.setTaskCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
            tradeOrder.setSignCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)));
        } catch (Exception e) {
            e.printStackTrace();
            tradeOrder.setTaskCycle(0);
            tradeOrder.setSignCycle(0);
        }
        tradeOrder.setCardGrade(targetCardGradeNo);
        tradeOrderService.create(tradeOrder);
        walletOrderService.addWalletOrder(userId,Constant.SYSTEM_USER_ID,WalletOrderType.TRADE_COIN_ACTIVE,-differTradeAmt,-differTradeAmt,0d,WalletOrderStatus.SUCCESS);
        walletOrderService.addWalletOrder(userId,Constant.SYSTEM_USER_ID,WalletOrderType.PAY_COIN_ACTIVE,-differPayAmt,-differPayAmt,0d,WalletOrderStatus.SUCCESS);
        // TODO: 2017/8/3 点位升级成功后的记录
        userGradeRecordService.addGradeRecord(user, GradeRecordType.CARDUPDATE, user.getCardGrade(), targetCardGradeNo, UpGradeType.ORIGINUPGRADE.getCode(), tradeOrder.getOrderNo());
    }

    /**
     * 覆盖升级
     *
     * @param userId            用户ID
     * @param targetCardGradeNo 升级等级
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void coverageUpgrade(String userId, Integer targetCardGradeNo) throws Exception {
        User user = this.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活");
            return;
        }
        CardGrade targetCardGrade = cardGradeService.getUserCardGrade(targetCardGradeNo);
        CardGrade userCurrentCardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());

        if (targetCardGrade == null) {
            logger.error("覆盖升级升级查询目标等级错误");
            return;
        }
        if (user.getCardGrade() >= targetCardGradeNo) {
            logger.error("覆盖升级只能从低往高升级！！！");
            return;
        }

        //计算差价
        Integer differRegisterNo = targetCardGrade.getRegisterCodeNo();
        Integer differActiceNo = targetCardGrade.getActiveCodeNo();
        Double insuranceAmt = targetCardGrade.getInsuranceAmt();
        Double differPayRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 4);
        Double differTradeRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 4);
        //人民币兑换支付币汇率
        Double payScale = parameterService.getScale(Constant.RMB_CONVERT_PAY_SCALE);
        //人民币兑换交易币汇率
        Double tradeScale = parameterService.getScale(Constant.RMB_CONVERT_TRADE_SCALE);
        //计算实际需要的币数量
        Double differPayAmt = CurrencyUtil.getPoundage(differPayRmbAmt, payScale, 4);
        Double differTradeAmt = CurrencyUtil.getPoundage(differTradeRmbAmt, tradeScale, 4);

        //更新相关信息
        User updateModel = new User();
        updateModel.setCardGrade(targetCardGrade.getGrade());
        updateModel.setRemark("用户覆盖升级，由" + CardLevelType.fromCode(user.getCardGrade()).getMsg() + "升级到" + CardLevelType.fromCode(targetCardGradeNo));
        this.updateById(userId, updateModel);

        //冻结币
        this.updateTradeAmtByUserId(userId, -differTradeAmt);
        this.updatePayAmtByUserId(userId, -differPayAmt);
        //TODO 写入冻结记录，后面完善，需要新定义枚举 WalletOrderType


        //更新用户详情冻结信息
        UserDetail userDetail = userDetailService.readById(userId);
        UserDetail userDetailUpdateModel = new UserDetail();
        userDetailUpdateModel.setForzenTradeAmt(userDetail.getForzenTradeAmt() + differTradeAmt);
        userDetailUpdateModel.setForzenPayAmt(userDetail.getForzenPayAmt() + differPayAmt);
        userDetailService.updateById(userId, userDetailUpdateModel);

        //扣除激活码
        activeCodeService.useActiveCode(userId, differActiceNo, "用户进行覆盖升级使用消费码");

        // TODO: 2017/8/3 覆盖升级成功后的记录
        //生成保单
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderNo(OrderNoUtil.get());
        tradeOrder.setAmt(targetCardGrade.getInsuranceAmt());
        tradeOrder.setSendUserId(userId);
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE_COVER.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE_COVER.getMsg());
        double rewardPayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE), 0.1d);
        double rewardTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE), 0.1d);
        double rewardEqutyScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE), 0.1d);
        tradeOrder.setPayAmtScale(rewardPayScale);
        tradeOrder.setTradeAmtScale(rewardTradeScale);
        tradeOrder.setEquityAmtScale(rewardEqutyScale);
        tradeOrder.setConfirmAmt(0d);
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        try {
            tradeOrder.setTaskCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
            tradeOrder.setSignCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)));
        } catch (Exception e) {
            e.printStackTrace();
            tradeOrder.setTaskCycle(0);
            tradeOrder.setSignCycle(0);
        }
        //原来的保单需要失效
        TradeOrder con = new TradeOrder();
        con.setSendUserId(userId);
        con.setStatus(OrderStatus.HANDING.getCode());
        TradeOrder oldOrder = tradeOrderService.readOne(con);
        if (oldOrder != null) {
            TradeOrder update = new TradeOrder();
            update.setStatus(OrderStatus.DEL.getCode());
            update.setRemark("保单覆盖升级,此单失效");
            tradeOrderService.updateById(oldOrder.getId(), update);
        }
        tradeOrder.setCardGrade(targetCardGradeNo);
        tradeOrderService.create(tradeOrder);
        walletOrderService.addWalletOrder(userId,Constant.SYSTEM_USER_ID,WalletOrderType.TRADE_COIN_ACTIVE,-differTradeAmt,-differTradeAmt,0d,WalletOrderStatus.SUCCESS);
        walletOrderService.addWalletOrder(userId,Constant.SYSTEM_USER_ID,WalletOrderType.PAY_COIN_ACTIVE,-differPayAmt,-differPayAmt,0d,WalletOrderStatus.SUCCESS);
        userGradeRecordService.addGradeRecord(user, GradeRecordType.CARDUPDATE, user.getCardGrade(), targetCardGradeNo, UpGradeType.COVERAGEUPGRADE.getCode(), tradeOrder.getOrderNo());
    }

    @Override
    public void updateUserRegisterCode(User loginUser, CardGrade cardGrade) {
        User updateUser = new User();
        updateUser.setId(loginUser.getId());
        updateUser.setRegisterCodeNo(loginUser.getRegisterCodeNo() - cardGrade.getRegisterCodeNo());
        this.updateById(updateUser.getId(), updateUser);
    }

    @Override
    public void updateUserActiveCode(String userId, Integer activeNo) {
        userMapper.updateUserActiveCode(userId, activeNo);
    }


    @Override
    public void updateUserRegisterCode(String userId, Integer registerNo) {
        userMapper.updateUserRegisterCode(userId, registerNo);
    }


    /**
     * @param user
     * @param cardGrade
     * @param loginUser
     * @param inviterUser 接点人
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public User createRegisterUser(User user, CardGrade cardGrade, User loginUser, User inviterUser) throws Exception {
        UserDetail inviterUserDetail = userDetailService.readById(inviterUser.getId());
        user.setCreateType(UserType.INNER.getCode());
        user.setGrade(GradeType.GRADE0.getCode());
        user.setCardGrade(cardGrade.getGrade());
        user.setStatus(UserStatusType.INNER_REGISTER_SUCCESSED.getCode());
        String payAddress = "";
        String equityAddress = "";
        String tradeAddress = "";


        user.setFirstReferrer(loginUser.getId());
        user.setRemainTaskNo(0);
        user.setSecondReferrer(loginUser.getFirstReferrer());
        user.setContactUserId(inviterUser.getId());
        user.setPassword(Md5Util.MD5Encode(user.getPassword(), DateUtils.currentDateToggeter()));
        user.setPayWord(Md5Util.MD5Encode(user.getPayWord(), DateUtils.currentDateToggeter()));
        user.setSalt(DateUtils.currentDateToggeter());
        user.setStatus(UserStatusType.INNER_REGISTER_SUCCESSED.getCode());
        String creatUserId = ToolUtil.getUUID();
        user.setId(creatUserId);
        user.setEquityAmt(0d);
        user.setTradeAmt(0d);
        user.setPayAmt(0d);
        user.setSumProfits(0d);
        user.setInsuranceAmt(cardGrade.getInsuranceAmt());
        user.setCashOutProfits(0d);
        user.setRegisterCodeNo(0);
        user.setActiveCodeNo(0);
        user.setMaxProfits(cardGrade.getInsuranceAmt() * cardGrade.getOutMultiple());
        this.create(user);
        user = this.readById(creatUserId);
        UserDetail innerUserDetail = userDetailService.readById(loginUser.getId());
        UserDetail userDetail = new UserDetail();
        userDetail.setId(user.getId());
        userDetail.setForzenEquityAmt(0d);
        userDetail.setForzenPayAmt(0d);
        userDetail.setForzenTradeAmt(0d);
        userDetail.setStatus(user.getStatus());
        userDetail.setLevles(innerUserDetail.getLevles() + 1);
        userDetail.setInEquityAddress(equityAddress);
        userDetail.setInTradeAddress(tradeAddress);
        userDetail.setInPayAddress(payAddress);
        userDetail.setLevles(inviterUserDetail.getLevles() + 1);
        userDetailService.create(userDetail);
        return user;
    }

    @Override
    @Transactional
    public User innerRegister(User innerUser, User inviterUser, User createUser, CardGrade cardGrade) throws Exception {
        /**创建用户账号**/
        createUser = this.createRegisterUser(createUser, cardGrade, innerUser, inviterUser);

        /**建立部门关系**/
        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setId(createUser.getId());
        userDepartment.setParentUserId(inviterUser.getId());
        userDepartment.setUid(createUser.getUid());
        userDepartment.setUserId(createUser.getId());
        userDepartment.setPerformance(0d);
        userDepartment.setTeamPerformance(0d);
        userDepartment.setStatus(StatusType.TRUE.getCode());
        userDepartment.setGrade(GradeType.GRADE0.getCode());
        userDepartmentService.createUserDepartment(userDepartment);
        /**扣注册码**/
//        activeCodeService.useRegisterCode(innerUser.getId(), cardGrade.getRegisterCodeNo(), "内部注册，推荐会员" + createUser.getUid() + "，使用" + cardGrade.getRegisterCodeNo() + "个注册码");
        /**扣激活码**/
//        activeCodeService.useActiveCode(innerUser.getId(), cardGrade.getActiveCodeNo(), "内部注册，推荐会员" + createUser.getUid() + "，使用" + cardGrade.getActiveCodeNo() + "个激活码");
        /**扣注册码**/
//        activeCodeService.useRegisterCode(innerUser.getId(), cardGrade.getRegisterCodeNo(), "内部注册，推荐会员" + createUser.getUid() + "，使用" + cardGrade.getRegisterCodeNo() + "个注册码");
        /**扣消费码**/
        activeCodeService.useActiveCode(innerUser.getId(), cardGrade.getActiveCodeNo(), "内部注册，推荐会员" + createUser.getUid() + "，使用" + cardGrade.getActiveCodeNo() + "个消费码");
        return createUser;
    }

    @Override
    public void updateUserStatus(String userId, Integer status) {
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(status);
        this.updateById(updateUser.getId(), updateUser);
    }

    @Override
    @Transactional
    public JsonResult innerActicveUser(User activeUser, CardGrade cardGrade) throws Exception {
        //人民币兑换支付币汇率
        Double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
        //人民币兑换交易币汇率
        Double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
        Double payRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 2);
        Double payCoinAmt = payRmbAmt * payScale;
        if (activeUser.getPayAmt() < payCoinAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付币数量不足，无法激活账号");
        }

        Double tradeRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 2);
        Double tradeCoinAmt = tradeRmbAmt * tradeScale;
        if (activeUser.getTradeAmt() < tradeCoinAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "交易币数量不足，无法激活账号");
        }
        // 扣除虚拟币
        this.updatePayAmtByUserId(activeUser.getId(), -payCoinAmt);
        this.updateTradeAmtByUserId(activeUser.getId(), -tradeCoinAmt);
        //写入冻结
        userDetailService.updateForzenPayAmtByUserId(activeUser.getId(), payCoinAmt);
        userDetailService.updateForzenTradeAmtByUserId(activeUser.getId(), tradeCoinAmt);
        User updateActiveUser = new User();
        updateActiveUser.setId(activeUser.getId());
        updateActiveUser.setInsuranceAmt(cardGrade.getInsuranceAmt());
        updateActiveUser.setStatus(UserStatusType.WAITACTIVATE.getCode());
        //修改用户状态
        this.updateById(updateActiveUser.getId(), updateActiveUser);
        //生成保单
        tradeOrderService.createInsuranceTradeOrder(activeUser, cardGrade);
        walletOrderService.addWalletOrder(updateActiveUser.getId(),Constant.SYSTEM_USER_ID,WalletOrderType.TRADE_COIN_ACTIVE,-tradeCoinAmt,-tradeCoinAmt,0d,WalletOrderStatus.SUCCESS);
        walletOrderService.addWalletOrder(updateActiveUser.getId(),Constant.SYSTEM_USER_ID,WalletOrderType.PAY_COIN_ACTIVE,-tradeCoinAmt,-tradeCoinAmt,0d,WalletOrderStatus.SUCCESS);
        return new JsonResult(ResultCode.SUCCESS.getCode(), UserStatusType.WAITACTIVATE.getMsg());
    }

    @Override
    public Integer updateEquityAmtByUserId(String userId, Double amt) {
        return userMapper.updateEquityAmtByUserId(userId, amt);
    }

    @Override
    public Integer updatePayAmtByUserId(String userId, Double amt) {
        return userMapper.updatePayAmtByUserId(userId, amt);
    }

    @Override
    public Integer updateTradeAmtByUserId(String userId, Double amt) {
        return userMapper.updateTradeAmtByUserId(userId, amt);
    }

    @Override
    public Integer updateMaxProfitsByUserId(String userId, Double maxProfits) {
        return userMapper.updateMaxProfitsByUserId(userId, maxProfits);
    }

    @Override
    public Integer updateMaxProfitsCoverByUserId(String userId, Double maxProfits) {
        return userMapper.updateMaxProfitsCoverByUserId(userId, maxProfits);
    }

    @Override
    public Integer updateSumProfitsByUserId(String userId, Double sumProfits) {
        return userMapper.updateSumProfitsByUserId(userId, sumProfits);
    }

    @Override
    public Integer updateInsuranceAmtByUserId(String userId, Double insuranceAmt) {
        return userMapper.updateInsuranceAmtByUserId(userId, insuranceAmt);
    }

    @Override
    public Integer updateSumProfitsCoverByUserId(String userId, Double sumProfits) {
        return userMapper.updateSumProfitsCoverByUserId(userId, sumProfits);
    }

    @Override
    public Integer updateInsuranceAmtCoverByUserId(String userId, Double insuranceAmt) {
        return userMapper.updateInsuranceAmtCoverByUserId(userId, insuranceAmt);
    }

    @Override
    public Integer updateCardGradeByUserId(String userId, Integer cardGrade) {
        return userMapper.updateCardGradeByUserId(userId, cardGrade);
    }

    @Override
    public Integer updateRemainTaskNoByUserId(String userId, Integer remainTaskNo) {
        return userMapper.updateUserRemainTaskNo(userId, remainTaskNo);
    }

    @Override
    public Integer updateUserStatusByUserId(String userId, Integer status) {
        return userMapper.updateUserStatusByUserId(userId, status);
    }

    @Override
    public Integer updateUserGradeByUserId(String userId, Integer grade) {
        return userMapper.updateUserGradeByUserId(userId, grade);
    }

    @Override
    public Integer updateUserCardGradeByUserId(String userId, Integer cardGrade) {
        return userMapper.updateUserCardGradeByUserId(userId, cardGrade);
    }

    @Override
    public Integer clearUnActiveUserWithIds(List<String> list) {
        return userMapper.clearUnActiveUserWithIds(list);
    }

    @Override
    public User readUserByLoginName(String loginName) {
        return userMapper.readUserByLoginName(loginName);
    }


    @Override
    public User readUserByUid(Integer uid) {
        return userMapper.readUserByUid(uid);
    }

    @Override
    public Boolean intervalActice(String userId) {
        TransferCode transferCode = new TransferCode();
        User user = this.readById(userId);
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        this.updateUserActiveCode(userId, -cardGrade.getActiveCodeNo());
        transferCode.setType(CodeType.OUT_USE_ACTIVECODE.getCode());
        transferCode.setRemark("用户重新激活,使用 " + cardGrade.getActiveCodeNo() + "个消费码");
        transferCode.setSendUserId(userId);
        transferCode.setReceviceUserId(Constant.SYSTEM_USER_ID);
        transferCode.setTransferNo(-cardGrade.getActiveCodeNo());
        transferCodeService.create(transferCode);
        this.updateUserStatus(userId, UserStatusType.ACTIVATESUCCESSED.getCode());
        this.updateRemainTaskNoByUserId(userId,7);
        return true;
    }


    /**
     * 用户升级
     *
     * @param loginUser
     * @param targetCardGrade
     * @param upGradeType
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public Boolean upGrade(User loginUser, CardGrade targetCardGrade, UpGradeType upGradeType) throws Exception {
        try {
            if (upGradeType.getCode() == UpGradeType.ORIGINUPGRADE.getCode().intValue()) {
                this.originUpgrade(loginUser.getId(), targetCardGrade.getGrade());
            } else if (upGradeType.getCode() == UpGradeType.COVERAGEUPGRADE.getCode().intValue()) {
                this.coverageUpgrade(loginUser.getId(), targetCardGrade.getGrade());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断邀请人和节点人是否在一条线
     *
     * @param inviterUid
     * @param concatUid
     * @return
     * @throws Exception
     */
    @Override
    public Boolean isVrticalLine(Integer inviterUid, Integer concatUid) throws Exception {


        return null;
    }


    @Override
    public Boolean isVrticalLine(String inviterUserId, String concatUserId) throws Exception {
        if (ToolUtil.isEmpty(inviterUserId) || ToolUtil.isEmpty(concatUserId)) {
            return false;
        }
        User inviter = this.readById(inviterUserId);
        if (inviter == null) {
            return false;
        }
        User concatUser = this.readById(concatUserId);
        if (concatUser == null) {
            return false;
        }
        UserDetail inviterDetail = userDetailService.readById(inviterUserId);
        Integer inviterlevle = inviterDetail.getLevles();
        UserDetail concatUserDetail = userDetailService.readById(concatUserId);
        Integer concatlevle = concatUserDetail.getLevles();
        if (concatlevle == null || inviterlevle == null || concatlevle <= inviterlevle) {
            return false;
        }
        //获取层级差
        Integer levelDiffer = concatlevle - inviterlevle + 1;
        User referUser = this.readById(concatUser.getFirstReferrer());
        if (referUser == null) {
            return false;
        }
        if (referUser.getId().equals(inviterUserId)) {
            return true;
        }
        for (int i = 0; i < levelDiffer; i++) {
            referUser = this.readById(referUser.getFirstReferrer());
            if (referUser == null) {
                return false;
            }
            if (referUser.getId().equals(inviterUserId)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public List<User> readUnActiceMoreThanDays() {
        List<User> users = new ArrayList<>();
        users = userMapper.readUnActiceMoreThanDays();
        if (ToolUtil.isEmpty(users)) {
            users = Collections.emptyList();
        }
        return users;
    }


    @Override
    public Boolean regularlyClearUnActiveUser() throws Exception {
        List<User> users = this.readUnActiceMoreThanDays();
        if (ToolUtil.isEmpty(users)) {
            return true;
        }
        List<String> ids = new ArrayList<>();
        for (User user : users) {
            ids.add(user.getId());
        }
        Integer count = this.clearUnActiveUserWithIds(ids);
        if (count >= 0) {
            logger.error("成功清除未激活用户" + count + "个");
            return true;
        }
        return false;
    }


    /**
     * 分享注册
     *
     * @param createUser
     * @param inviterUser
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public User shareRegister(User createUser, User inviterUser) throws Exception {
        /**创建用户账号**/
        createUser = this.shareRegisterCreateUser(createUser, inviterUser);

        /**建立部门关系**/
        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setId(createUser.getId());
        userDepartment.setParentUserId(inviterUser.getId());
        userDepartment.setUid(createUser.getUid());
        userDepartment.setUserId(createUser.getId());
        userDepartment.setPerformance(0d);
        userDepartment.setTeamPerformance(0d);
        userDepartment.setStatus(StatusType.TRUE.getCode());
        userDepartment.setGrade(GradeType.GRADE0.getCode());
        userDepartmentService.createUserDepartment(userDepartment);
        return createUser;
    }


    /**
     * 分享注册创建用户和用户子表
     *
     * @param creatUser
     * @param inviterUser
     * @return
     * @throws Exception
     */
    @Override
    @Transactional
    public User shareRegisterCreateUser(User creatUser, User inviterUser) throws Exception {

        creatUser.setCreateType(UserType.OUT.getCode());
        creatUser.setGrade(GradeType.GRADE0.getCode());
        creatUser.setCardGrade(CardLevelType.UNCHOOSE.getCode());
        creatUser.setMaxProfits(0d);
        creatUser.setInsuranceAmt(0d);
        creatUser.setStatus(UserStatusType.OUT_SHARE_REGISTER_SUCCESSED.getCode());
        String payAddress = "";
        String equityAddress = "";
        String tradeAddress = "";


        creatUser.setFirstReferrer(inviterUser.getId());
        creatUser.setRemainTaskNo(0);
        creatUser.setSecondReferrer(inviterUser.getSecondReferrer());
        creatUser.setContactUserId(inviterUser.getId());
        creatUser.setPassword(Md5Util.MD5Encode(creatUser.getPassword(), DateUtils.currentDateToggeter()));
        creatUser.setPayWord(Md5Util.MD5Encode(creatUser.getPayWord(), DateUtils.currentDateToggeter()));
        creatUser.setSalt(DateUtils.currentDateToggeter());
        String creatUserId = ToolUtil.getUUID();
        creatUser.setId(creatUserId);
        creatUser.setEquityAmt(0d);
        creatUser.setTradeAmt(0d);
        creatUser.setPayAmt(0d);
        creatUser.setSumProfits(0d);

        creatUser.setCashOutProfits(0d);
        creatUser.setRegisterCodeNo(0);
        creatUser.setActiveCodeNo(0);

        this.create(creatUser);
        creatUser = this.readById(creatUserId);
        UserDetail inviterUserDetail = userDetailService.readById(inviterUser.getId());
        UserDetail userDetail = new UserDetail();
        userDetail.setId(creatUser.getId());
        userDetail.setForzenEquityAmt(0d);
        userDetail.setForzenPayAmt(0d);
        userDetail.setForzenTradeAmt(0d);
        userDetail.setStatus(creatUser.getStatus());
        userDetail.setLevles(inviterUserDetail.getLevles() + 1);
        userDetail.setInEquityAddress(equityAddress);
        userDetail.setInTradeAddress(tradeAddress);
        userDetail.setInPayAddress(payAddress);
        userDetail.setLevles(inviterUserDetail.getLevles() + 1);
        userDetailService.create(userDetail);
        return creatUser;
    }


    @Override
    @Transactional
    public Integer updateUserCardGrade(User creatUser, CardGrade cardGrade) {
        try {
            creatUser.setCardGrade(cardGrade.getGrade());
            creatUser.setMaxProfits(cardGrade.getInsuranceAmt() * cardGrade.getOutMultiple());
            creatUser.setInsuranceAmt(cardGrade.getInsuranceAmt());
            User updateModel = new User();
            updateModel.setInsuranceAmt(cardGrade.getInsuranceAmt());
            updateModel.setMaxProfits(CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), cardGrade.getOutMultiple(), 2));
            updateModel.setCardGrade(cardGrade.getGrade());
            this.updateUserStatusByUserId(creatUser.getId(),UserStatusType.INNER_REGISTER_SUCCESSED.getCode());
            this.updateById(creatUser.getId(), updateModel);
            logger.error("更新分享注册用户的卡等级成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    @Transactional
    public JsonResult shareActicveUser(String userId) throws Exception {
        User user = this.readById(userId);
        if (user==null){
            return  new JsonResult(ResultCode.ERROR.getCode(),"找不到用户");
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        if (cardGrade==null){
            return  new JsonResult(ResultCode.ERROR.getCode(),"用户卡等级有误");
        }
        //人民币兑换支付币汇率
        Double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
        //人民币兑换交易币汇率
        Double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
        Double payRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 2);
        Double payCoinAmt = payRmbAmt * payScale;
        if (user.getRegisterCodeNo() < cardGrade.getRegisterCodeNo()) {
//            return new JsonResult(ResultCode.ERROR.getCode(), "注册码数量不足，无法激活账号");
        }
        if (user.getActiveCodeNo() < cardGrade.getActiveCodeNo()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "消费码数量不足，无法激活账号");
        }
        if (user.getPayAmt() < payCoinAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付币数量不足，无法激活账号");
        }

        Double tradeRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 2);
        Double tradeCoinAmt = tradeRmbAmt * tradeScale;
        if (user.getTradeAmt() < tradeCoinAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "交易币数量不足，无法激活账号");
        }
        // 扣除虚拟币
        this.updatePayAmtByUserId(user.getId(), -payCoinAmt);
        this.updateTradeAmtByUserId(user.getId(), -tradeCoinAmt);
        //写入冻结
        userDetailService.updateForzenPayAmtByUserId(user.getId(), payCoinAmt);
        userDetailService.updateForzenTradeAmtByUserId(user.getId(), tradeCoinAmt);
        User updateActiveUser = new User();
        updateActiveUser.setId(user.getId());
        updateActiveUser.setInsuranceAmt(cardGrade.getInsuranceAmt());
        updateActiveUser.setStatus(UserStatusType.WAITACTIVATE.getCode());
        //修改用户状态
        this.updateById(updateActiveUser.getId(), updateActiveUser);
        //生成保单
        tradeOrderService.createInsuranceTradeOrder(user, cardGrade);

        TransferCode transferCode = new TransferCode();
        this.updateUserActiveCode(userId, -cardGrade.getActiveCodeNo());
        transferCode.setType(CodeType.REGISTER_USE_ACTIVECODE.getCode());
        transferCode.setRemark("分享激活,使用 " + cardGrade.getActiveCodeNo() + "个消费码");
        transferCode.setSendUserId(userId);
        transferCode.setReceviceUserId(Constant.SYSTEM_USER_ID);
        transferCode.setTransferNo(-cardGrade.getActiveCodeNo());
        transferCodeService.create(transferCode);
//        this.updateUserRegisterCode(userId, -cardGrade.getRegisterCodeNo());
//        transferCode.setType(CodeType.REGISTER_USE_REGISTERCODE.getCode());
//        transferCode.setRemark("分享注册,使用 " + cardGrade.getRegisterCodeNo() + "个注册码");
//        transferCode.setSendUserId(userId);
//        transferCode.setReceviceUserId(Constant.SYSTEM_USER_ID);
//        transferCode.setTransferNo(-cardGrade.getRegisterCodeNo());
//        transferCodeService.create(transferCode);
        this.updateUserStatus(userId, UserStatusType.ACTIVATESUCCESSED.getCode());
        return new JsonResult(ResultCode.SUCCESS.getCode(), UserStatusType.WAITACTIVATE.getMsg());
    }
}
