package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.TradeRecordDetailMapper;
import com.hansen.service.TradeRecordDetailService;
import com.model.TradeRecordDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeRecordDetailServiceImpl extends CommonServiceImpl<TradeRecordDetail> implements TradeRecordDetailService {
    @Autowired
    private TradeRecordDetailMapper baseUserDao;
    @Override
    protected CommonDao<TradeRecordDetail> getDao() {
        return baseUserDao;
    }

    @Override
    protected Class<TradeRecordDetail> getModelClass() {
        return TradeRecordDetail.class;
    }

}
