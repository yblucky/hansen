package com.hansen.mapper;

import com.base.dao.CommonDao;
import com.model.WalletTransaction;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletTransactionMapper extends CommonDao<WalletTransaction> {

/*    List<WalletTransaction> listByStartToEnd(@Param("start") Long start, @Param("end") Long end);

    public List<WalletTransaction> listByTransactionTime(@Param("start") Long start, @Param("end") Long end, @Param("userId") Integer userId);*/
}
