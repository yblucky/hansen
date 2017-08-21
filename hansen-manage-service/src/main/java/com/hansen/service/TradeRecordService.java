package com.hansen.service;

import com.base.service.CommonService;
import com.common.constant.RecordType;
import com.model.TradeRecord;

/**
 * @date 2016年11月27日
 */
public interface TradeRecordService extends CommonService<TradeRecord> {


    void addRecord(String userId, Double amout, Double equityAmt, Double payAmt, Double tradeAmt, String orderNo, RecordType recordType) throws Exception;
}
