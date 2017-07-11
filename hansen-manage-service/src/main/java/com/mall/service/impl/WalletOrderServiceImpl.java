package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.WalletOrderMapper;
import com.mall.model.WalletOrder;
import com.mall.service.WalletOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletOrderServiceImpl extends CommonServiceImpl<WalletOrder> implements WalletOrderService {
    @Autowired
    private WalletOrderMapper walletOrderMapper;

    @Override
    protected CommonDao<WalletOrder> getDao() {
        return walletOrderMapper;
    }

    @Override
    protected Class<WalletOrder> getModelClass() {
        return WalletOrder.class;
    }

}
