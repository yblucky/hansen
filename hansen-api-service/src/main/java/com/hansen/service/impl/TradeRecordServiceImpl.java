package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.RecordType;
import com.utils.ParamUtil;
import com.hansen.mappers.TradeRecordMapper;
import com.hansen.service.TradeRecordDetailService;
import com.hansen.service.TradeRecordService;
import com.model.Parameter;
import com.model.TradeRecord;
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
    public void addRecord(String userId, Double amout, Double equityAmt, Double payAmt, Double tradeAmt, String orderNo, RecordType recordType) throws  Exception{
     /*   TradeRecord record = new TradeRecord();
        record.setUserId(userId);
        record.setAmount(amout);
        record.setOrderNo(orderNo);
        record.setRecordType(recordType.getCode());
        record.setRemark(recordType.getMsg());
        this.create(record);*/
        Double payScale= Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE));
        Double tradeScale= Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE));
        Double equityScale= Double.valueOf(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE));
        //股币类型收益
        RecordType equityRecordType = RecordType.fromCode(recordType.getCode()+1);
        tradeRecordDetailService.addRecordDetail(userId,equityAmt,orderNo,equityScale,equityRecordType);
        //支付币类型收益
        RecordType payRecordType = RecordType.fromCode(recordType.getCode()+2);
        tradeRecordDetailService.addRecordDetail(userId,payAmt,orderNo,payScale,payRecordType);
        //交易币类型收益
        RecordType tradeRecordType = RecordType.fromCode(recordType.getCode()+3);
        tradeRecordDetailService.addRecordDetail(userId,tradeAmt,orderNo,tradeScale,tradeRecordType);
    }
}
