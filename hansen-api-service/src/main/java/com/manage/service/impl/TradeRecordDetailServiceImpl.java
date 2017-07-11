package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.TradeRecordDetailMapper;
import com.manage.model.TradeRecordDetail;
import com.manage.service.TradeRecordDetailService;
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
