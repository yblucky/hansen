package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.WalletTransactionMapper;
import com.manage.model.WalletTransaction;
import com.manage.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletTransactionServiceImpl extends CommonServiceImpl<WalletTransaction> implements WalletTransactionService {
    @Autowired
    private WalletTransactionMapper walletTransactionMapper;

    @Override
    protected CommonDao<WalletTransaction> getDao() {
        return walletTransactionMapper;
    }

    @Override
    protected Class<WalletTransaction> getModelClass() {
        return WalletTransaction.class;
    }

}
