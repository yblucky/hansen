package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserMapper;
import com.mall.mapper.WalletCurrencyMapper;
import com.mall.model.BaseUser;
import com.mall.model.WalletCurrency;
import com.mall.service.BaseUserService;
import com.mall.service.WalletCurrencyService;
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
