package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.TradePerformanceRecordMapper;
import com.mall.model.TradePerformanceRecord;
import com.mall.service.TradePerformanceRecordService;
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
