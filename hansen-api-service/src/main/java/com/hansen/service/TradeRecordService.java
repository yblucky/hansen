package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.common.constant.RecordType;
import com.hansen.model.TradeRecord;

/**
 * @date 2016年11月27日
 */
public interface TradeRecordService extends CommonService<TradeRecord> {


    void addRecord(String userId, Double amout, Double equityAmt, Double payAmt, Double tradeAmt, String orderNo, RecordType recordType);
}
