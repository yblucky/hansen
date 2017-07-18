package com.api.service;

import com.api.constant.RecordType;
import com.api.core.service.CommonService;
import com.api.model.TradeRecord;

/**
 * @date 2016年11月27日
 */
public interface TradeRecordService extends CommonService<TradeRecord> {


    void addRecord(String userId, Double amout, Double equityAmt, Double payAmt, Double tradeAmt, String orderNo, RecordType recordType);
}
