package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.*;
import com.mapper.TradeOrderMapper;
import com.model.*;
import com.service.*;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.utils.numberutils.RandomUtil.getCode;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeOrderServiceImpl extends CommonServiceImpl<TradeOrder> implements TradeOrderService {
    @Autowired
    private TradeOrderMapper tradeOrderMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CardGradeService cardGradeService;
    @Autowired
    private UserDepartmentService userDepartmentService;
    @Autowired
    private UserGradeRecordService userGradeRecordService;
    @Autowired
    private GradeService gradeService;

    @Override
    protected CommonDao<TradeOrder> getDao() {
        return tradeOrderMapper;
    }

    @Override
    protected Class<TradeOrder> getModelClass() {
        return TradeOrder.class;
    }

    @Override
    public TradeOrder createInsuranceTradeOrder(User activeUser, CardGrade cardGrade) throws Exception {
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderNo(OrderNoUtil.get());
        tradeOrder.setAmt(cardGrade.getInsuranceAmt());
        tradeOrder.setSendUserId(activeUser.getId());
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE.getMsg());
        tradeOrder.setPushFirstReferrerScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHFIRSTREFERRERSCALE)));
        tradeOrder.setPushSecondReferrerScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHSECONDREFERRERSCALE)));
        tradeOrder.setPayAmtScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE)));
        tradeOrder.setTradeAmtScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE)));
        tradeOrder.setEquityAmtScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE)));
        tradeOrder.setConfirmAmt(cardGrade.getInsuranceAmt());
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        this.create(tradeOrder);
        return tradeOrder;
    }

    @Override
    @Transactional
    public Boolean handleInsuranceTradeOrder(String orderNo) throws Exception {
        if (ToolUtil.isEmpty(orderNo)) {
            return false;
        }
        TradeOrder orderModel = new TradeOrder();
        orderModel.setOrderNo(orderNo);
        TradeOrder tradeOrder = this.readOne(orderModel);
        User activeUser = userService.readById(tradeOrder.getSendUserId());
        CardGrade cardGradeMdel = new CardGrade();
        cardGradeMdel.setGrade(tradeOrder.getCardGrade());
        CardGrade cardGrade = cardGradeService.readOne(cardGradeMdel);

        Integer upGradeType = 0;
        //写入最大收益
        if (OrderType.INSURANCE.getCode() == tradeOrder.getSource()) {
//            upGradeType = UpGradeType.INSURANCE.getCode();
//            double payRmbAmt = CurrencyUtil.multiply(tradeOrder.getAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 2);
//            if (activeUser.getPayAmt() < payRmbAmt) {
//                logger.error("支付币数量不足，无法激活账号");
//            }
//
//
//            double tradeRmbAmt = CurrencyUtil.multiply(tradeOrder.getAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 2);
//            if (activeUser.getTradeAmt() < tradeRmbAmt) {
//                logger.error("交易币数量不足，无法激活账号");
//            }
//            double payAmt = CurrencyUtil.multiply(payRmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE)), 2);
//            double tradeAmt = CurrencyUtil.multiply(tradeRmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE)), 2);
//            userService.updatePayAmtByUserId(activeUser.getId(), -payAmt);
//            userService.updatePayAmtByUserId(activeUser.getId(), -tradeAmt);
            User updateActiveUser = new User();
            updateActiveUser.setId(activeUser.getId());
            updateActiveUser.setInsuranceAmt(tradeOrder.getAmt());
            updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
            updateActiveUser.setMaxProfits(cardGrade.getOutMultiple() * tradeOrder.getAmt());
            userService.updateById(updateActiveUser.getId(), updateActiveUser);
            UserDetail activeUserDetailContion = new UserDetail();
            activeUserDetailContion.setId(activeUser.getId());
            //写入冻结
//            userDetailService.updateForzenPayAmtByUserId(activeUser.getId(), payAmt);
//            userDetailService.updateForzenTradeAmtByUserId(activeUser.getId(), tradeAmt);
//            userDetailService.updateForzenEquityAmtByUserId(activeUser.getId(), 0d);
        } else if (OrderType.INSURANCE_COVER.getCode() == tradeOrder.getSource()) {
            upGradeType = UpGradeType.COVERAGEUPGRADE.getCode();
            userService.updateMaxProfitsByUserId(tradeOrder.getSendUserId(), cardGrade.getOutMultiple() * tradeOrder.getAmt());
        } else if (OrderType.INSURANCE_ORIGIN.getCode() == tradeOrder.getSource()) {
            upGradeType = UpGradeType.ORIGINUPGRADE.getCode();
            userService.updateMaxProfitsByUserId(tradeOrder.getSendUserId(), tradeOrder.getAmt() + activeUser.getInsuranceAmt());
        }
        //更新用户开卡级别
        userService.updateCardGradeByUserId(tradeOrder.getSendUserId(), tradeOrder.getCardGrade());
        //向上累加业绩
        int i;
        User refferUser = null;
        String referId = activeUser.getFirstReferrer();
        for (i = 0; i < 10000; i++) {
            if (ToolUtil.isEmpty(referId)){
                break;
            }
            if (Constant.SYSTEM_USER_ID.equals(referId)) {
                break;
            }
            refferUser = userService.readById(referId);
            if (refferUser == null  && UserStatusType.ACTIVATESUCCESSED.getCode()!=refferUser.getStatus()) {
                continue;
            }
            userDepartmentService.updatePerformance(referId, tradeOrder.getAmt());
            Integer historyGrade = refferUser.getGrade();
            //重新计算用户星级
            Grade grade = gradeService.getUserGrade(refferUser.getId());
            if (grade != null && grade.getGrade() != refferUser.getGrade()) {
                //更新用户星级
                userService.updateUserGradeByUserId(referId, grade.getGrade());
                //写入用户升级记录
                userGradeRecordService.addGradeRecord(refferUser, GradeRecordType.GRADEUPDATE, historyGrade, UpGradeType.COVERAGEUPGRADE.getCode(), orderNo);
            }
            referId = refferUser.getFirstReferrer();
        }
        //写入结算记录
        TradePerformanceRecord performanceRecord = new TradePerformanceRecord();
        performanceRecord.setAmt(tradeOrder.getAmt());
        performanceRecord.setOrderNo(tradeOrder.getOrderNo());
        performanceRecord.setSource(OrderType.fromCode(tradeOrder.getSource()).getCode());
        performanceRecord.setRemark(OrderType.fromCode(tradeOrder.getSource()).getMsg());
        //写入直推奖待做任务领取奖励记录，有则写
        userService.pushBonus(activeUser.getId(), tradeOrder);
        //写入管理奖待做任务领取奖励记录，有则写
        userService.manageBonus(activeUser.getId(), tradeOrder);
        //写入极差奖待做任务领取奖励记录，有则写
        userService.differnceBonus(activeUser.getId(), tradeOrder);
        //写入用户升级记录
        userGradeRecordService.addGradeRecord(activeUser, GradeRecordType.CARDUPDATE, activeUser.getGrade(), tradeOrder.getCardGrade(), tradeOrder.getOrderNo());
        // 更改订单的结算状态
        TradeOrder updateOrder = new TradeOrder();
        updateOrder.setStatus(OrderStatus.HANDLED.getCode());
        this.updateById(tradeOrder.getId(), updateOrder);
        return true;
    }

    @Override
    public List<TradeOrder> readRewardList(Date taskTime, Integer startRow, Integer pageSize) throws Exception {
        return tradeOrderMapper.readRewardList(taskTime, startRow, pageSize);
    }

    @Override
    public Integer batchUpdateSignCycle(List<String> idList) throws Exception {
        if (ToolUtil.isEmpty(idList)) {
            return 0;
        }
        return tradeOrderMapper.batchUpdateSignCycle(idList);
    }

    @Override
    public Integer batchUpdateTaskCycle(List<String> idList) throws Exception {
        if (ToolUtil.isEmpty(idList)) {
            return 0;
        }
        return tradeOrderMapper.batchUpdateTaskCycle(idList);
    }

    @Override
    public Map<String, Double> getCoinNoFromRmb(Double rmbAmt) throws Exception {
        Map<String, Double> map = new HashMap<>();
        Double payAmt = CurrencyUtil.multiply(rmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE)), 2);
        Double tradeAmt = CurrencyUtil.multiply(rmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE)), 2);
        Double equityAmt = CurrencyUtil.multiply(rmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTEQUITYSCALE)), 2);
        map.put("payAmt", payAmt);
        map.put("tradeAmt", tradeAmt);
        map.put("equityAmt", equityAmt);
        return map;
    }




    @Override
    public Integer batchUpdateTaskCycleDefault(List<String> idList, Integer taskCycle) throws Exception {
        if (ToolUtil.isEmpty(idList)) {
            return 0;
        }
        return tradeOrderMapper.batchUpdateTaskCycleDefault(idList, taskCycle);
    }

    @Override
    public Integer batchUpdateOrderStatus(List<String> idList) {
        return tradeOrderMapper.batchUpdateOrderStatus(idList);
    }
}
