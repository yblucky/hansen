package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mappers.WalletCurrencyMapper;
import com.service.WalletCurrencyService;
import com.model.WalletCurrency;
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
