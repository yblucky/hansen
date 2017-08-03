package com.hansen.mapper;

import com.hansen.base.dao.CommonDao;
import com.hansen.model.WalletTransaction;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WalletTransactionMapper extends CommonDao<WalletTransaction> {

    public List<WalletTransaction> listByTransactionTime(@Param("start") Long start, @Param("end") Long end, @Param("userId") Integer userId);

    public List<WalletTransaction> listByStartToEnd(@Param("start") Long start, @Param("end") Long end);

    public Boolean updateTransactionStatusByTxId(String txId, Integer confirmations, String transactionStatus);
}
