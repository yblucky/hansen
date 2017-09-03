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
import ru.paradoxs.bitcoin.client.BitcoinClient;

import java.util.Date;
import java.util.Map;

import static com.service.WalletUtil.getBitCoinClient;


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
     * 用户每周收益   此方法作废，现在改为做完7次任务后获取奖励(后台参数维护)
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
        this.addTradeOrder(user.getId(), user.getId(), orderNo, incomeAmt, RecordType.RELASE, 0, 0);
    }

    /**
     * 用户动态收益-直推奖
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    @Transactional
    public void pushBonus(String pushUserId, TradeOrder order) throws Exception {
        User pushUser = this.readById(pushUserId);
        if (pushUser == null) {
            System.out.println("找不到用户....");
            return;
        }
     /*
      处于一个事务，用户状态可能未来及更新提交，查出来的状态还是待激活
      if (pushUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }*/
        //一代直推奖
        User parentUser = this.readById(pushUser.getFirstReferrer());
        //一代推荐人必须也是激活状态
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            String orderNo = order.getOrderNo();
            //保单等级以最小为准
            Integer cardLevel = parentUser.getCardGrade() > pushUser.getCardGrade() ? pushUser.getCardGrade() : parentUser.getCardGrade();
            CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            //直推收益按两者最小保单金额*6%
            double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHFIRSTREFERRERSCALE)), 1d);
           /*存入一代推荐人的三种钱包中  需要做完7次任务才可以分4次领取
            userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.PUSH, orderNo);*/
            // TODO: 2017/7/14 记录一代直推奖记录
            this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.PUSH, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
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
                String orderNo = order.getOrderNo();
                //保单等级以最小为准
                Integer cardLevel = grandfatherUser.getCardGrade() > pushUser.getCardGrade() ? pushUser.getCardGrade() : grandfatherUser.getCardGrade();
                CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
                //直推收益按两者最小保单金额*4%
                double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHSECONDREFERRERSCALE)), 1d);
                //存入二代推荐人的三种钱包中
//                userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.PUSH, orderNo);
                // TODO: 2017/7/14 记录二代直推奖记录
                this.addTradeOrder(pushUserId, grandfatherUser.getId(), orderNo, incomeAmt, RecordType.PUSH, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
            }
        }
    }

    /**
     * 用户动态收益-管理奖
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    @Transactional
    public void manageBonus(String pushUserId, TradeOrder order) throws Exception {
        User pushUser = this.readById(pushUserId);
        if (pushUser == null) {
            System.out.println("找不到用户....");
            return;
        }
     /*   if (pushUser.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }*/
        //一代管理奖
        User parentUser = this.readById(pushUser.getFirstReferrer());
        //一代推荐人必须也是激活状态并且一代推荐人的保单等级大于或等于推荐人
        if (parentUser.getStatus().intValue() == UserStatusType.ACTIVATESUCCESSED.getCode() && parentUser.getCardGrade().intValue() >= pushUser.getCardGrade()) {
            String orderNo = order.getOrderNo();
            //保单等级以最小为准
            Integer cardLevel = pushUser.getCardGrade();
            CardGrade cardGrade = cardGradeService.getUserCardGrade(cardLevel);
            //管理收益按两者最小保单金额*2%
            Double manageFirstScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.MANAGEFIRSTREFERRERSCALE));
            double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * manageFirstScale, 1d);
            //存入一代推荐人的三种钱包中
//            userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.MANAGE, orderNo);
            // TODO: 2017/7/14 记录一代管理奖记录
            this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.MANAGE, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
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
                Double manageFirstScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.MANAGESECONDREFERRERSCALE));
                double incomeAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt() * manageFirstScale, 1d);
                //存入二代推荐人的三种钱包中
//                userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.MANAGE, orderNo);
                // TODO: 2017/7/14 记录二代管理奖记录
//                this.addTradeOrder(pushUserId, grandfatherUser.getId(), orderNo, incomeAmt, RecordType.MANAGE, 0, 0);
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.MANAGE, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
            }
        }
    }


    /**
     * 用户动态收益-级差奖
     *
     * @param pushUserId 新推荐的用户id
     */
    @Override
    public void differnceBonus(String pushUserId, TradeOrder order) throws Exception {
        User user = this.readById(pushUserId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        //用户激活的保单等级
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        double insuranceAmt = cardGrade.getInsuranceAmt();
        //用户所处的等级所能拿到奖金的比例
        double bonusScale = 0d;
       /* Grade grade = gradeService.getGradeDetail(user.getGrade());
        if (grade != null) {
            bonusScale = grade.getRewardScale();
        } else {
            user.setGrade(0);
        }*/
        User childUser = user;
        //1星到5星的奖励比率为 2% < 4% < 6% < 8% < 12% < 16% < 20% 平级奖 2% 最多领取22%
        for (int i = 0; i < 7; i++) {
            User parentUser = this.readById(childUser.getId());
            Grade parentGrade = gradeService.getGradeDetail(parentUser.getGrade());
            if (parentGrade != null) {
                bonusScale = parentGrade.getRewardScale();
            } else {
                parentUser.setGrade(0);
            }
            bonusScale = parentGrade.getRewardScale();
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
            //上下级平级且等级都超过二星
            if (parentUser.getGrade().intValue() > GradeType.GRADE1.getCode().intValue() && parentUser.getGrade().intValue() == user.getGrade().intValue()) {
                // TODO: 2017/7/17 领取平级奖  保单金额*1%
                String orderNo = order.getOrderNo();
                Double sameRewradScale = Double.valueOf(ParamUtil.getIstance().get(Parameter.SAMEREWARDSCALESCALE));
                double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * sameRewradScale, 1d);
//                this.userIncomeAmt(incomeAmt, parentUser.getId(), RecordType.SAME, orderNo);
                //领取平级奖记录
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.SAME, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
                //中断级差
                return;
            }
            //上下级形成级差
            if (parentUser.getGrade().intValue() > user.getGrade().intValue()) {
                String orderNo = order.getOrderNo();
                //第一级由激活保单的用户自己领取
                if (bonusScale > 0 && i == 0) {
                    double incomeAmt = CurrencyUtil.getPoundage(insuranceAmt * bonusScale, 1d);
                    this.userIncomeAmt(incomeAmt, user.getId(), RecordType.DIFFERENT, orderNo);
                    this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.SAME, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
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
                this.addTradeOrder(pushUserId, parentUser.getId(), orderNo, incomeAmt, RecordType.DIFFERENT, Integer.valueOf(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
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
            this.updateById(user.getId(), model);
            // TODO: 2017/7/17 记录会员等级升级记录
            Integer historyGrade = user.getGrade();
            user = this.readById(user.getId());
            userGradeRecordService.addGradeRecord(user, GradeRecordType.GRADEUPDATE, historyGrade, null, OrderNoUtil.get());
        }
    }


    //新增用户收入记录
    private void addTradeOrder(String sendUserId, String receviceUserId, String order, Double amt, RecordType recordType, Integer rewardInterval, Integer taskInterval) {
        TradeOrder model = new TradeOrder();
        model.setSendUserId(sendUserId);
        model.setReceviceUserId(receviceUserId);
        model.setOrderNo(order);
        model.setAmt(amt);
        model.setConfirmAmt(amt);
        model.setPoundage(0d);
        model.setTaskCycle(taskInterval);
        model.setSignCycle(rewardInterval);
        model.setSource(recordType.getCode());
        model.setRemark(recordType.getMsg());
        tradeOrderService.create(model);
    }

    /**
     * 点位升级
     *
     * @param userId    用户ID
     * @param cardGrade 升级等级
     */
    @Override
    public void originUpgrade(String userId, Integer cardGrade) {
        User user = this.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }
        CardGrade model = new CardGrade();
        model.setGrade(cardGrade);
        CardGrade grade = cardGradeService.readOne(model);
        if (user.getCardGrade() >= grade.getGrade()) {
            System.out.println("点位升级只能从低往高升级！！！");
            return;
        }
        User updateModel = new User();
        updateModel.setGrade(grade.getGrade());
        updateModel.setInsuranceAmt(grade.getInsuranceAmt());
        updateModel.setMaxProfits(CurrencyUtil.multiply(grade.getOutMultiple(), grade.getInsuranceAmt(), 4));
        this.updateById(userId, updateModel);

        //生成保单
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderNo(OrderNoUtil.get());
        tradeOrder.setAmt(grade.getInsuranceAmt());
        tradeOrder.setSendUserId(userId);
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE.getMsg());
        tradeOrder.setPayAmtScale(0.5);
        tradeOrder.setTradeAmtScale(0.5);
        tradeOrder.setEquityAmtScale(0d);
        tradeOrder.setConfirmAmt(0d);
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        tradeOrderService.create(tradeOrder);
        // TODO: 2017/8/3 点位升级成功后的记录
        userGradeRecordService.addGradeRecord(updateModel, GradeRecordType.CARDUPDATE, user.getGrade(), UpGradeType.ORIGINUPGRADE.getCode(), tradeOrder.getOrderNo());
    }

    /**
     * 覆盖升级
     *
     * @param userId    用户ID
     * @param cardGrade 升级等级
     */
    @Override
    public void coverageUpgrade(String userId, Integer cardGrade) {
        User user = this.readById(userId);
        if (user == null) {
            System.out.println("找不到用户....");
            return;
        }
        if (user.getStatus().intValue() != UserStatusType.ACTIVATESUCCESSED.getCode()) {
            System.out.println("用户未激活保单");
            return;
        }
        CardGrade model = new CardGrade();
        model.setGrade(cardGrade);
        CardGrade grade = cardGradeService.readOne(model);
        if (user.getCardGrade() >= grade.getGrade()) {
            System.out.println("覆盖升级只能从低往高升级！！！");
            return;
        }
        User updateModel = new User();
        updateModel.setGrade(grade.getGrade());
        updateModel.setInsuranceAmt(grade.getInsuranceAmt());
        updateModel.setMaxProfits(CurrencyUtil.getPoundage(CurrencyUtil.multiply(grade.getOutMultiple(), grade.getInsuranceAmt(), 4) + user.getMaxProfits(), 1d));
        this.updateById(userId, updateModel);
        // TODO: 2017/8/3 覆盖升级成功后的记录
        //生成保单
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderNo(OrderNoUtil.get());
        tradeOrder.setAmt(grade.getInsuranceAmt());
        tradeOrder.setSendUserId(userId);
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE.getMsg());
        tradeOrder.setPayAmtScale(0.5);
        tradeOrder.setTradeAmtScale(0.5);
        tradeOrder.setEquityAmtScale(0d);
        tradeOrder.setConfirmAmt(0d);
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        tradeOrderService.create(tradeOrder);
        userGradeRecordService.addGradeRecord(updateModel, GradeRecordType.CARDUPDATE, user.getGrade(), UpGradeType.COVERAGEUPGRADE.getCode(), tradeOrder.getOrderNo());
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
    @Transactional
    public User createRegisterUser(User user, CardGrade cardGrade, User inviterUser) throws Exception {
        UserDetail inviterUserDetail = userDetailService.readById(inviterUser.getId());
        user.setFirstReferrer(inviterUser.getId());
        user.setCreateType(UserType.INNER.getCode());
        user.setSecondReferrer(inviterUser.getFirstReferrer());
        user.setGrade(cardGrade.getGrade());
        user.setStatus(UserStatusType.INNER_REGISTER_SUCCESSED.getCode());

        /**生成钱包地址**/
        String payAddress = WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.PAY), user.getLoginName());
        String equityAddress = WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.EQUITY), user.getLoginName());
        String tradeAddress = WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.TRADE), user.getLoginName());
        user.setFirstReferrer(inviterUser.getId());
        user.setSecondReferrer(inviterUser.getSecondReferrer());
        user.setContactUserId(null);
        user.setPassword(Md5Util.MD5Encode(user.getPassword(), DateUtils.currentDateToggeter()));
        user.setSalt(DateUtils.currentDateToggeter());
        user.setStatus(UserStatusType.INNER_REGISTER_SUCCESSED.getCode());
        this.create(user);
        UserDetail userDetail = new UserDetail();
        userDetail.setId(user.getId());
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
        createUser = this.createRegisterUser(createUser, cardGrade, inviterUser);
        /**建立部门关系**/
        UserDepartment userDepartment = new UserDepartment();
        userDepartment.setParentUserId(innerUser.getId());
        userDepartment.setUid(createUser.getUid());
        userDepartment.setUserId(createUser.getId());
        userDepartmentService.createUserDepartment(userDepartment);
        /**扣注册码**/
        activeCodeService.useRegisterCode(innerUser.getId(), -cardGrade.getRegisterCodeNo(), "内部注册，推荐会员" + createUser.getUid() + "，使用" + cardGrade.getRegisterCodeNo() + "个注册码");
        /**扣激活码**/
        activeCodeService.useActiveCode(innerUser.getId(), -cardGrade.getActiveCodeNo(), "内部注册，推荐会员" + createUser.getUid() + "，使用" + cardGrade.getActiveCodeNo() + "个激活码");
        return createUser;
    }

    @Override
    public void updateUserStatus(String userId, Integer status) {
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        this.updateById(updateUser.getId(), updateUser);
    }

    @Override
    @Transactional
    public JsonResult innerActicveUser(User activeUser, CardGrade cardGrade) throws Exception {
        //冻结账号虚拟币 激活账号
        double payRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 2);
        if (activeUser.getPayAmt() < payRmbAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "购物币数量不足，无法激活账号");
        }

        double tradeRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 2);
        if (activeUser.getTradeAmt() < tradeRmbAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "交易币数量不足，无法激活账号");
        }

        User updateActiveUser = new User();
        updateActiveUser.setId(activeUser.getId());
        updateActiveUser.setInsuranceAmt(cardGrade.getInsuranceAmt());
        updateActiveUser.setStatus(UserStatusType.WAITACTIVATE.getCode());
        //生成保单
        tradeOrderService.createInsuranceTradeOrder(activeUser, cardGrade);
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
    public Integer updateSumProfitsByUserId(String userId, Double sumProfits) {
        return userMapper.updateSumProfitsByUserId(userId, sumProfits);
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
    public void updateUserRegisterCode(String userId, Integer registerNo) {
        userMapper.updateUserRegisterCode(userId,registerNo);
    }
}
