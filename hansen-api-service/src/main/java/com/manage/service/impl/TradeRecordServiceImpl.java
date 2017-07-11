package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.TradeRecordMapper;
import com.manage.model.TradeRecord;
import com.manage.service.TradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeRecordServiceImpl extends CommonServiceImpl<TradeRecord> implements TradeRecordService {
    @Autowired
    private TradeRecordMapper tradeRecordMapper;
    @Override
    protected CommonDao<TradeRecord> getDao() {
        return tradeRecordMapper;
    }

    @Override
    protected Class<TradeRecord> getModelClass() {
        return TradeRecord.class;
    }

}
