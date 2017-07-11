package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.WalletOrderMapper;
import com.manage.model.WalletOrder;
import com.manage.service.WalletOrderService;
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
