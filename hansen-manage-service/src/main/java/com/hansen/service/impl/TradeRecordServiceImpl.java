package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.TradeRecordMapper;
import com.hansen.model.TradeRecord;
import com.hansen.service.TradeRecordService;
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
