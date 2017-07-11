package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.WalletParameterMapper;
import com.mall.model.WalletParameter;
import com.mall.service.WalletParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletParameterServiceImpl extends CommonServiceImpl<WalletParameter> implements WalletParameterService {
    @Autowired
    private WalletParameterMapper walletParameterMapper;
    @Override
    protected CommonDao<WalletParameter> getDao() {
        return walletParameterMapper;
    }

    @Override
    protected Class<WalletParameter> getModelClass() {
        return WalletParameter.class;
    }

}
