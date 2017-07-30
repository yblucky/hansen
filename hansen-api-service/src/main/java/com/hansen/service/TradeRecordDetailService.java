package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.common.constant.RecordType;
import com.hansen.model.TradeRecordDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface TradeRecordDetailService extends CommonService<TradeRecordDetail> {


    @Transactional
    void addRecordDetail(String userId, Double amout, String orderNo, Double scale, RecordType recordType);
}
