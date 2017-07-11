package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.TradeOrderMapper;
import com.manage.model.TradeOrder;
import com.manage.service.TradeOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class TradeOrderServiceImpl extends CommonServiceImpl<TradeOrder> implements TradeOrderService {
    @Autowired
    private TradeOrderMapper tradeOrderMapper;

    @Override
    protected CommonDao<TradeOrder> getDao() {
        return tradeOrderMapper;
    }

    @Override
    protected Class<TradeOrder> getModelClass() {
        return TradeOrder.class;
    }

}
