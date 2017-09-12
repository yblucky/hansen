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
        tradeOrder.setConfirmAmt(cardGrade.getInsuranceAmt());
        tradeOrder.setCardGrade(cardGrade.getGrade());
        tradeOrder.setSendUserId(activeUser.getId());
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE.getMsg());
        tradeOrder.setPushFirstReferrerScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHFIRSTREFERRERSCALE)));
        tradeOrder.setPushSecondReferrerScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.PUSHSECONDREFERRERSCALE)));
        tradeOrder.setPayAmtScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE)));
        tradeOrder.setTradeAmtScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE)));
        tradeOrder.setEquityAmtScale(Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE)));
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        tradeOrder.setTaskCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
        tradeOrder.setSignCycle(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.REWARDINTERVAL)));
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
        if (tradeOrder==null){
            logger.error(" ----------handleInsuranceTradeOrder  结算订单不存在-------- ");
        }
        //查出原点升级之前的保单
        TradeOrder model=null;
        if (OrderType.INSURANCE_ORIGIN.getCode().equals(tradeOrder.getSource())){
            TradeOrder con = new TradeOrder();
            con.setSendUserId(tradeOrder.getSendUserId());
            con.setStatus(OrderStatus.HANDING.getCode());
            model = this.readOne(con);
        }

        Integer upGradeType = 0;
        //写入最大收益
        if (OrderType.INSURANCE.getCode().intValue() == tradeOrder.getSource()) {
            User updateActiveUser = new User();
            updateActiveUser.setId(activeUser.getId());
            updateActiveUser.setInsuranceAmt(tradeOrder.getAmt());
            updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
            updateActiveUser.setRemainTaskNo(ToolUtil.parseInt(ParamUtil.getIstance().get(Parameter.TASKINTERVAL)));
            updateActiveUser.setMaxProfits(cardGrade.getOutMultiple() * tradeOrder.getAmt());
            userService.updateById(updateActiveUser.getId(), updateActiveUser);
            UserDetail activeUserDetailContion = new UserDetail();
            activeUserDetailContion.setId(activeUser.getId());
        } else if (OrderType.INSURANCE_COVER.getCode().intValue() == tradeOrder.getSource()) {
            upGradeType = UpGradeType.COVERAGEUPGRADE.getCode();
            userService.updateMaxProfitsByUserId(tradeOrder.getSendUserId(), cardGrade.getOutMultiple() * cardGrade.getInsuranceAmt());
            //更新用户的静态收益释放基数
//            userService.updateInsuranceAmtByUserId(tradeOrder.getSendUserId(),cardGrade.getInsuranceAmt());
            //不做叠加
            userService.updateInsuranceAmtCoverByUserId(tradeOrder.getSendUserId(), cardGrade.getInsuranceAmt());
        } else if (OrderType.INSURANCE_ORIGIN.getCode().intValue() == tradeOrder.getSource()) {
            //补差升级,结算成功后，合并保单
            userService.updateMaxProfitsCoverByUserId(tradeOrder.getSendUserId(), cardGrade.getOutMultiple() * cardGrade.getInsuranceAmt());
//            userService.updateMaxProfitsByUserId(tradeOrder.getSendUserId(), cardGrade.getOutMultiple() * cardGrade.getInsuranceAmt());
            //更新用户的静态收益释放基数
            userService.updateInsuranceAmtCoverByUserId(tradeOrder.getSendUserId(), cardGrade.getInsuranceAmt());
            if (model!=null){
                TradeOrder update = new TradeOrder();
                update.setStatus(OrderStatus.DEL.getCode());
                update.setRemark("补差升级，结算成功后，删除原保单");
                CardGrade cardGrade1=cardGradeService.getUserCardGrade(tradeOrder.getCardGrade());
                if (cardGrade==null){
                    logger.error(" ----------handleInsuranceTradeOrder  补差升级，用户的目标等级查询不到-------- ");
                }
                update.setAmt(cardGrade.getInsuranceAmt());
                update.setConfirmAmt(cardGrade.getInsuranceAmt());
                this.updateById(model.getId(),update);
            }
        }
        //向上累加业绩
        User refferUser = null;
        String referId = activeUser.getContactUserId();
        //保单激活，自己就是一个部门，给自己单独加上部门业绩
        userDepartmentService.updatePerformance(activeUser.getId(), tradeOrder.getAmt());
        UserDetail userDetail = userDetailService.readById(referId);
        if (userDetail == null) {
            logger.error("保单结束结算：" + orderNo + "无法查询到下单用户");
            return false;
        }
        for (int i = 0; i < userDetail.getLevles(); i++) {
            if (ToolUtil.isEmpty(referId)) {
                break;
            }
            refferUser = userService.readById(referId);
            if (refferUser == null && UserStatusType.ACTIVATESUCCESSED.getCode() != refferUser.getStatus()) {
                continue;
            }
//            userDepartmentService.updatePerformance(referId, tradeOrder.getAmt());
              userDepartmentService.updateDeparmentAndTeamPerformanceByUserId(referId,tradeOrder.getConfirmAmt(),tradeOrder.getConfirmAmt());
            Integer historyGrade = refferUser.getGrade();
//            //重新计算用户星级
//            Grade grade = gradeService.getUserGrade(refferUser.getId());
//            if (grade != null && grade.getGrade() != refferUser.getGrade()) {
//                //更新用户星级
//                userService.updateUserGradeByUserId(referId, grade.getGrade());
//                UserDepartment con = new UserDepartment();
//                con.setUserId(refferUser.getId());
//                UserDepartment userDepartment = userDepartmentService.readOne(con);
//                UserDepartment updateModel = new UserDepartment();
//                updateModel.setGrade(grade.getGrade());
//                userDepartmentService.updateById(userDepartment.getId(),updateModel);
//                //写入用户升级记录
//                userGradeRecordService.addGradeRecord(refferUser, GradeRecordType.GRADEUPDATE, historyGrade, grade.getGrade(), UpGradeType.STARGRADE.getCode(), orderNo);
//            }
            referId = refferUser.getContactUserId();
        }
        //写入结算记录
        TradePerformanceRecord performanceRecord = new TradePerformanceRecord();
        performanceRecord.setAmt(tradeOrder.getAmt());
        performanceRecord.setOrderNo(tradeOrder.getOrderNo());
        performanceRecord.setSource(OrderType.fromCode(tradeOrder.getSource()).getCode());
        performanceRecord.setRemark(OrderType.fromCode(tradeOrder.getSource()).getMsg());
        //写入直推奖待做任务领取奖励记录，有则写
        userService.pushBonus(activeUser.getId(), tradeOrder);
        //写入极差奖待做任务领取奖励记录，有则写
        userService.differnceBonus(activeUser.getId(), tradeOrder);
        // 更改订单的结算状态
        TradeOrder updateOrder = new TradeOrder();
        updateOrder.setStatus(OrderStatus.HANDING.getCode());
        this.updateById(tradeOrder.getId(), updateOrder);
        return true;
    }

    @Override
    public List<TradeOrder> readRewardList(String userId, Date taskTime, Integer startRow, Integer pageSize) throws Exception {
        return tradeOrderMapper.readRewardList(userId, taskTime, startRow, pageSize);
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


    @Override
    public List<TradeOrder> readRewardListByOrderType(String userId, List<Integer> source, Integer startRow, Integer pageSize) throws Exception {
        return tradeOrderMapper.readRewardListByOrderType(userId, source, startRow, pageSize);
    }

    @Override
    public Integer readRewardCountByOrderType(String userId, List<Integer> source) {
        return tradeOrderMapper.readRewardCountByOrderType(userId, source);
    }

    @Override
    public Double sumReadRewardByOrderType(String userId, List<Integer> source) {
        Double sum = tradeOrderMapper.sumReadRewardByOrderType(userId, source);
        if (sum == null) {
            sum = 0d;
        }
        return sum;
    }

    @Override
    public Double  readWaiteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(String userId, Integer signCycle, Integer source){
        Double sum=tradeOrderMapper.readWaiteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(userId, signCycle,source);
        if (sum==null){
            sum=0d;
        }
        return sum;
    }


    @Override
    public Double readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(String userId, Integer source) {
        Double sum=tradeOrderMapper.readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(userId,source);
        if (sum==null){
            sum=0d;
        }
        return sum;
    }

    @Override
    public Double readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(String userId, Integer signCycle, Integer source) {
        Double sum=tradeOrderMapper.readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(userId, signCycle,source);
        if (sum==null){
            sum=0d;
        }
        return sum;
    }

    @Override
    public Double readSumDynamicProfitsCount(String userId, List<Integer> source) {
        Double sum = tradeOrderMapper.readSumDynamicProfitsCount(userId, source);
        if (sum == null) {
            sum = 0d;
        }
        return sum;
    }

    @Override
    public List<TradeOrder> readWaitHandleList(Integer startRow, Integer pageSize) throws Exception {
        return tradeOrderMapper.readWaitHandleList(startRow, pageSize);
    }

    @Override
    public Integer readWaitHandleCount() throws Exception {
        Integer count = tradeOrderMapper.readWaitHandleCount();
        if (count == null) {
            count = 0;
        }
        return count;
    }
}
