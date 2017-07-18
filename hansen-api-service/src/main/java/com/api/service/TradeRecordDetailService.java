package com.api.service;

import com.api.constant.RecordType;
import com.api.core.service.CommonService;
import com.api.model.TradeRecordDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface TradeRecordDetailService extends CommonService<TradeRecordDetail> {


    @Transactional
    void addRecordDetail(String userId, Double amout, String orderNo, Double scale, RecordType recordType);
}
