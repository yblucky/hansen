package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.WalletTransactionMapper;
import com.hansen.service.WalletTransactionService;
import com.model.WalletTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletTransactionServiceImpl extends CommonServiceImpl<WalletTransaction> implements WalletTransactionService {


    @Override
    protected CommonDao<WalletTransaction> getDao() {
        return walletTransactionMapper;
    }

    @Override
    protected Class<WalletTransaction> getModelClass() {
        return WalletTransaction.class;
    }


    @Autowired
    private WalletTransactionMapper walletTransactionMapper;



    @Override
    public List<WalletTransaction> listByTransactionTime(Long start, Long end, Integer userId) {
        return walletTransactionMapper.listByTransactionTime(start, end,userId);
    }


    @Override
    public List<WalletTransaction> listByStartToEnd(Long start, Long end) {
        return walletTransactionMapper.listByStartToEnd(start, end);
    }

}
