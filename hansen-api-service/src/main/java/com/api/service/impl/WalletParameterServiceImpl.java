package com.api.service.impl;

import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.WalletParameterMapper;
import com.api.model.WalletParameter;
import com.api.service.WalletParameterService;
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
