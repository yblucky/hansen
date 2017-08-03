package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.common.constant.RecordType;
import com.hansen.mapper.TradeRecordDetailMapper;
import com.hansen.model.TradeRecordDetail;
import com.hansen.service.TradeRecordDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeRecordDetailServiceImpl extends CommonServiceImpl<TradeRecordDetail> implements TradeRecordDetailService {
    @Autowired
    private TradeRecordDetailMapper baseUserDao;
    @Override
    protected CommonDao<TradeRecordDetail> getDao() {
        return baseUserDao;
    }

    @Override
    protected Class<TradeRecordDetail> getModelClass() {
        return TradeRecordDetail.class;
    }

    @Override
    @Transactional
    public void addRecordDetail(String userId, Double amout, String orderNo,Double scale, RecordType recordType){
        TradeRecordDetail model = new TradeRecordDetail();
        model.setUserId(userId);
        model.setAmount(amout);
        model.setOrderNo(orderNo);
        model.setScale(scale);
        model.setRecordType(recordType.getCode());
        model.setRemark(recordType.getMsg());
        this.create(model);
    }
}