package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.WalletParameterMapper;
import com.hansen.service.WalletParameterService;
import com.model.WalletParameter;
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
