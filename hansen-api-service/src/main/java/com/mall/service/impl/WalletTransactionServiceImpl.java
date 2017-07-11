package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.WalletTransactionMapper;
import com.mall.model.WalletTransaction;
import com.mall.service.WalletTransactionService;
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
