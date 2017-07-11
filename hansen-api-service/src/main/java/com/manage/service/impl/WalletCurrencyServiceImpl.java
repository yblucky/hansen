package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.WalletCurrencyMapper;
import com.manage.model.WalletCurrency;
import com.manage.service.WalletCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletCurrencyServiceImpl extends CommonServiceImpl<WalletCurrency> implements WalletCurrencyService {
    @Autowired
    private WalletCurrencyMapper walletCurrencyMapper;
    @Override
    protected CommonDao<WalletCurrency> getDao() {
        return walletCurrencyMapper;
    }

    @Override
    protected Class<WalletCurrency> getModelClass() {
        return WalletCurrency.class;
    }

}
