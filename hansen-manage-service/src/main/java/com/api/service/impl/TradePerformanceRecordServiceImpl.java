package com.api.service.impl;

import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.TradePerformanceRecordMapper;
import com.api.model.TradePerformanceRecord;
import com.api.service.TradePerformanceRecordService;
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
