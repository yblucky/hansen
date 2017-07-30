package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.WalletCurrencyMapper;
import com.hansen.model.WalletCurrency;
import com.hansen.service.WalletCurrencyService;
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
