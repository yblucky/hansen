package com.hansen.service;

import com.base.service.CommonService;
import com.common.constant.RecordType;
import com.model.TradeRecordDetail;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface TradeRecordDetailService extends CommonService<TradeRecordDetail> {


    @Transactional
    void addRecordDetail(String userId, Double amout, String orderNo, Double scale, RecordType recordType);
}
