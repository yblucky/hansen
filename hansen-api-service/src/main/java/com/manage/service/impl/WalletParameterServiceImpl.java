package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.WalletParameterMapper;
import com.manage.model.WalletParameter;
import com.manage.service.WalletParameterService;
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
