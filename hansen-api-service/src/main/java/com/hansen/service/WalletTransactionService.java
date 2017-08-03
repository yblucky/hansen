package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.model.WalletTransaction;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface WalletTransactionService extends CommonService<WalletTransaction> {

    public List<WalletTransaction> listByStartToEnd(Long start, Long end);

    public List<WalletTransaction> listByTransactionTime(Long start, Long end, Integer account);
}
