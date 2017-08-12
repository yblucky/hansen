package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.common.constant.Constant;
import com.common.constant.OrderStatus;
import com.common.constant.OrderType;
import com.common.constant.UserStatusType;
import com.common.utils.ParamUtil;
import com.common.utils.numberutils.CurrencyUtil;
import com.common.utils.toolutils.OrderNoUtil;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.mapper.TradeOrderMapper;
import com.hansen.service.CardGradeService;
import com.hansen.service.TradeOrderService;
import com.hansen.service.UserDetailService;
import com.hansen.service.UserService;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Override
    protected CommonDao<TradeOrder> getDao() {
        return tradeOrderMapper;
    }

    @Override
    protected Class<TradeOrder> getModelClass() {
        return TradeOrder.class;
    }

    @Override
    public TradeOrder createInsuranceTradeOrder(User activeUser, CardGrade cardGrade)  throws Exception{
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
        String msg = "";
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

        double payRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 2);
        if (activeUser.getPayAmt() < payRmbAmt) {
            msg = "支付币数量不足，无法激活账号";
        }

        double tradeRmbAmt = CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 2);
        if (activeUser.getTradeAmt() < tradeRmbAmt) {
            msg = "交易币数量不足，无法激活账号";
        }
        double payAmt = CurrencyUtil.multiply(payRmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE)), 2);
        double tradeAmt = CurrencyUtil.multiply(tradeRmbAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE)), 2);
        userService.updatePayAmtByUserId(activeUser.getId(), -payAmt);
        userService.updatePayAmtByUserId(activeUser.getId(), -tradeAmt);

        //写入最大收益
        User updateActiveUser = new User();
        updateActiveUser.setId(activeUser.getId());
        updateActiveUser.setInsuranceAmt(cardGrade.getInsuranceAmt());
        updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        updateActiveUser.setMaxProfits(cardGrade.getOutMultiple() * cardGrade.getInsuranceAmt());
        userService.updateById(updateActiveUser.getId(), updateActiveUser);
        UserDetail activeUserDetailContion = new UserDetail();
        activeUserDetailContion.setUserId(activeUser.getId());
        UserDetail activeUserDetail = userDetailService.readOne(activeUserDetailContion);
        //写入冻结
        userDetailService.updateForzenPayAmtByUserId(activeUser.getId(), payAmt);
        userDetailService.updateForzenTradeAmtByUserId(activeUser.getId(), tradeAmt);
        userDetailService.updateForzenEquityAmtByUserId(activeUser.getId(), 0d);
        //TODO  更改订单的结算状态
        return true;
    }
}
