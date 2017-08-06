package com.hansen.service;

import com.base.service.CommonService;
import com.model.WalletTransaction;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface WalletTransactionService extends CommonService<WalletTransaction> {

    public List<WalletTransaction> listByStartToEnd(Long start, Long end);

    public List<WalletTransaction> listByTransactionTime(Long start, Long end, Integer account);
}
