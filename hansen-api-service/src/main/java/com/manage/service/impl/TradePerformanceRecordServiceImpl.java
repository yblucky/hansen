package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.TradePerformanceRecordMapper;
import com.manage.model.TradePerformanceRecord;
import com.manage.service.TradePerformanceRecordService;
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
