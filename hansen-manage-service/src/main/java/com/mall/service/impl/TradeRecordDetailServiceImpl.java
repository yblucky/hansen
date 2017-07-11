package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.TradeRecordDetailMapper;
import com.mall.model.TradeRecordDetail;
import com.mall.service.TradeRecordDetailService;
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
