package hansen.tradecurrency.trade.dao;

import hansen.tradecurrency.trade.model.Transaction;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TransactionDao {
    public Boolean insert(Transaction transaction);

    public Transaction selectById(Integer id);

    public List<Transaction> listByTransactionTime(@Param("start") Long start, @Param("end") Long end, @Param("userId") Integer userId);

    public List<Transaction> listByStartToEnd(@Param("start") Long start, @Param("end") Long end);

    public Boolean updateTransactionStatusByTxId(String txId, Integer confirmations, String transactionStatus);
}
