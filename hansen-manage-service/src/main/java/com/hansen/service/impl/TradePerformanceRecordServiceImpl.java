package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.TradePerformanceRecordMapper;
import com.hansen.service.TradePerformanceRecordService;
import com.model.TradePerformanceRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class TradePerformanceRecordServiceImpl extends CommonServiceImpl<TradePerformanceRecord> implements TradePerformanceRecordService {
    @Autowired
    private TradePerformanceRecordMapper tradePerformanceRecordMapper;

    @Override
    protected CommonDao<TradePerformanceRecord> getDao() {
        return tradePerformanceRecordMapper;
    }

    @Override
    protected Class<TradePerformanceRecord> getModelClass() {
        return TradePerformanceRecord.class;
    }

}
