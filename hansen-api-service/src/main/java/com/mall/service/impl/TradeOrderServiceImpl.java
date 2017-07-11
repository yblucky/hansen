package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.TradeOrderMapper;
import com.mall.model.TradeOrder;
import com.mall.service.TradeOrderService;
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
