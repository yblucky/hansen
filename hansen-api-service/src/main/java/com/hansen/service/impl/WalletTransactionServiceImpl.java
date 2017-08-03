package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.WalletTransactionMapper;
import com.hansen.model.WalletTransaction;
import com.hansen.service.WalletTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeMap;

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
