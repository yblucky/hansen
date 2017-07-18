package com.api.service.impl;

import com.api.constant.RecordType;
import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.TradeRecordMapper;
import com.api.model.TradeRecord;
import com.api.service.TradeRecordDetailService;
import com.api.service.TradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeRecordServiceImpl extends CommonServiceImpl<TradeRecord> implements TradeRecordService {
    @Autowired
    private TradeRecordMapper tradeRecordMapper;
    @Autowired
    private TradeRecordDetailService tradeRecordDetailService;

    @Override
    protected CommonDao<TradeRecord> getDao() {
        return tradeRecordMapper;
    }

    @Override
    protected Class<TradeRecord> getModelClass() {
        return TradeRecord.class;
    }

    @Override
    @Transactional
    public void addRecord(String userId, Double amout, Double equityAmt, Double payAmt, Double tradeAmt, String orderNo, RecordType recordType) {
        TradeRecord record = new TradeRecord();
        record.setUserId(userId);
        record.setAmount(amout);
        record.setOrderNo(orderNo);
        record.setRecordType(recordType.getCode());
        record.setRemark(recordType.getMsg());
        this.create(record);
        //股币类型收益
        RecordType equityRecordType = RecordType.fromCode(recordType.getCode()+1);
        tradeRecordDetailService.addRecordDetail(userId,equityAmt,orderNo,0.07,equityRecordType);
        //支付币类型收益
        RecordType payRecordType = RecordType.fromCode(recordType.getCode()+2);
        tradeRecordDetailService.addRecordDetail(userId,payAmt,orderNo,0.02,payRecordType);
        //交易币类型收益
        RecordType tradeRecordType = RecordType.fromCode(recordType.getCode()+3);
        tradeRecordDetailService.addRecordDetail(userId,tradeAmt,orderNo,0.01,tradeRecordType);
    }
}
