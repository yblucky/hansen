package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.TradeRecordMapper;
import com.mall.model.TradeRecord;
import com.mall.service.TradeRecordService;
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
