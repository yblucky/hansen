package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.RecordType;
import com.mapper.TradeRecordDetailMapper;
import com.service.TradeRecordDetailService;
import com.model.TradeRecordDetail;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeRecordDetailServiceImpl extends CommonServiceImpl<TradeRecordDetail> implements TradeRecordDetailService {
    @Autowired
    private TradeRecordDetailMapper tradeRecordDetailMapper;
    @Override
    protected CommonDao<TradeRecordDetail> getDao() {
        return tradeRecordDetailMapper;
    }

    @Override
    protected Class<TradeRecordDetail> getModelClass() {
        return TradeRecordDetail.class;
    }

    @Override
    @Transactional
    public void addRecordDetail(String userId, Double amout, String orderNo,Double scale, RecordType recordType){
        TradeRecordDetail model = new TradeRecordDetail();
        model.setId(ToolUtil.getUUID());
        model.setUserId(userId);
        model.setAmount(amout);
        model.setOrderNo(orderNo);
        model.setScale(scale);
        model.setRecordType(recordType.getCode());
        model.setRemark(recordType.getMsg());
        tradeRecordDetailMapper.create(model);
    }
}
