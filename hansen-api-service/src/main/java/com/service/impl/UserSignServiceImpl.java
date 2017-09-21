package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.Constant;
import com.constant.RecordType;
import com.constant.SignType;
import com.constant.UserStatusType;
import com.mapper.UserSignMapper;
import com.model.Parameter;
import com.model.User;
import com.model.UserSign;
import com.service.*;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @date 2016年11月27日
 */
@Service
public class UserSignServiceImpl extends CommonServiceImpl<UserSign> implements UserSignService {
    @Autowired
    private UserSignMapper userSignMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private TradeRecordService tradeRecordService;

    @Autowired
    private ParameterService parameterService;

    @Override
    protected CommonDao<UserSign> getDao() {
        return userSignMapper;
    }

    @Override
    protected Class<UserSign> getModelClass() {
        return UserSign.class;
    }

    @Override
    @Transactional
    public Boolean sign(String signId) throws Exception {
        Boolean isOut = false;
        if (ToolUtil.isEmpty(signId)) {
            return false;
        }
        UserSign sign = this.readById(signId);
        if (sign == null) {
            return false;
        }
        User user = userService.readById(sign.getUserId());
        if (user == null) {
            return false;
        }
        Double avaibleAmt = 0d;
        if (user.getSumProfits() > user.getMaxProfits()) {
            userService.updateUserStatusByUserId(user.getId(),UserStatusType.ORDER_OUT.getCode());
            return false;
        }
        if ((user.getSumProfits() + sign.getAmt()) > user.getMaxProfits()) {
            avaibleAmt=CurrencyUtil.subtract(user.getMaxProfits(),user.getSumProfits(),2);
            sign.setAmt(avaibleAmt);
            Boolean isSpilt = this.splitSign(signId,avaibleAmt);
            if (isSpilt){
                isOut=true;
            }
        }
//        Map<String, Double> map = tradeOrderService.getCoinNoFromRmb(sign.getAmt());
//        Double payAmt = map.get("payAmt");
//        Double tradeAmt = map.get("tradeAmt");
//        Double equityAmt = map.get("equityAmt");
        Double payAmt = 1d;
        Double tradeAmt = 1d;
        Double equityAmt = 1d;
        if (sign.getRmbCovertPayAmtScale() != null) {
            payAmt = sign.getRmbCovertPayAmtScale();
        }
        if (sign.getRmbCovertTradeAmtScale() != null) {
            tradeAmt = sign.getRmbCovertTradeAmtScale();
        }
        if (sign.getRmbCovertEquityScale() != null) {
            equityAmt = sign.getRmbCovertEquityScale();
        }
        Double rewardConvertPayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE), 0d);
        Double rewardConvertTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE), 0d);
        Double rewardConvertEquityScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE), 0d);
        payAmt = CurrencyUtil.multiply(payAmt, rewardConvertPayScale, 4);
        tradeAmt = CurrencyUtil.multiply(tradeAmt, rewardConvertTradeScale, 4);
        equityAmt = CurrencyUtil.multiply(equityAmt, rewardConvertEquityScale, 4);
        userService.updatePayAmtByUserId(sign.getUserId(), payAmt);
        userService.updateTradeAmtByUserId(sign.getUserId(), tradeAmt);
        userService.updateEquityAmtByUserId(sign.getUserId(), equityAmt);
        userService.updateSumProfitsByUserId(sign.getUserId(), sign.getAmt());
        UserSign updateModel = new UserSign();
        if (isOut){
            updateModel.setAmt(sign.getAmt());
        }
        updateModel.setSignTime(new Date());
        updateModel.setStatus(SignType.SIGNED.getCode());
        updateModel.setRemark("用户成功领取到" + sign.getAmt() + "红包");
        this.updateById(signId, updateModel);
        //写入币的收益记录
        tradeRecordService.addRecord(user.getId(), sign.getAmt(), equityAmt, payAmt, tradeAmt, "", RecordType.PUSH);
        return true;
    }


    @Override
    public UserSign addUserSign(String userId, Double amt, SignType signType, String remark) {
        if (ToolUtil.isEmpty(userId)) {
            return null;
        }
        User user = userService.readById(userId);
        if (user == null) {
            return null;
        }
        UserSign model = new UserSign();
        model.setUid(user.getUid());
        model.setSignTime(new Date());
        model.setUserId(userId);
        model.setRemark(remark);
        model.setAmt(amt);
        model.setStatus(SignType.WAITING_SIGN.getCode());
        this.setRmbConvertRateScale(model);
        this.create(model);
        return model;
    }

    private void setRmbConvertRateScale(UserSign model) {
        model.setRmbCovertEquityScale(parameterService.getScale(Constant.RMB_CONVERT_EQUITY_SCALE));
        model.setRmbCovertPayAmtScale(parameterService.getScale(Constant.RMB_CONVERT_PAY_SCALE));
        model.setRmbCovertTradeAmtScale(parameterService.getScale(Constant.RMB_CONVERT_TRADE_SCALE));
    }

    @Override
    public Double readSignCount(String userId) {
        Double signCount = userSignMapper.readSignCount(userId);
        if (signCount == null) {
            return 0d;
        }
        return signCount;
    }

    @Override
    public Double readSumFrozenCount(String userId) {
        Double signFrozenCount = userSignMapper.readSumFrozenCount(userId);
        if (signFrozenCount == null) {
            return 0d;
        }
        return signFrozenCount;
    }

    @Override
    public Boolean splitSign(String id,Double availableAmt) {
        if (ToolUtil.isEmpty(id)){
            return false;
        }
        UserSign sign =this.readById(id);
        if (sign==null){
            return false;
        }
        Double remainAmt = CurrencyUtil.subtract(sign.getAmt(),availableAmt,2);
        if (remainAmt>0){
            this.addUserSign(sign.getUserId(),remainAmt,SignType.WAITING_SIGN,"保单出局,拆分奖励发放记录");
        }
        userService.updateUserStatusByUserId(sign.getUserId(),UserStatusType.ORDER_OUT.getCode());
        return true;
    }
}
