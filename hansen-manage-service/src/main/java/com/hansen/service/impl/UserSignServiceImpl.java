package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.common.constant.RecordType;
import com.common.constant.SignType;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.mapper.UserSignMapper;
import com.hansen.service.TradeOrderService;
import com.hansen.service.TradeRecordService;
import com.hansen.service.UserService;
import com.hansen.service.UserSignService;
import com.model.User;
import com.model.UserSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

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
        if (user.getSumProfits() > user.getMaxProfits() || (user.getSumProfits() + sign.getAmt()) > user.getMaxProfits()) {
            return false;
        }
        Map<String, Double> map = tradeOrderService.getCoinNoFromRmb(sign.getAmt());
        Double payAmt=map.get("payAmt");
        Double tradeAmt=map.get("tradeAmt");
        Double equityAmt=map.get("equityAmt");
        userService.updatePayAmtByUserId(sign.getUserId(), payAmt);
        userService.updateTradeAmtByUserId(sign.getUserId(), tradeAmt);
        userService.updateEquityAmtByUserId(sign.getUserId(), equityAmt);
        userService.updateSumProfitsByUserId(sign.getUserId(), sign.getAmt());
        UserSign updateModel = new UserSign();
        updateModel.setSignTime(new Date());
        updateModel.setStatus(SignType.SIGNED.getCode());
        updateModel.setRemark("用户成功领取到" + sign.getAmt() + "红包");
        this.updateById(signId, updateModel);
        //写入币的收益记录
        tradeRecordService.addRecord(user.getId(),sign.getAmt(),equityAmt,payAmt,tradeAmt,"", RecordType.PUSH);
        return true;
    }


    @Override
    public UserSign addUserSign(String userId, Double amt, SignType signType, String remark) {
        UserSign model = new UserSign();
        model.setSignTime(new Date());
        model.setUserId(userId);
        model.setRemark(remark);
        model.setStatus(SignType.WAITING_SIGN.getCode());
        this.create(model);
        return model;
    }
}
