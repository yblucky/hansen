package hansen.tradecurrency.trade.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import hansen.wallet.trade.model.Transaction;

public interface TransactionDao {
	public Boolean insert(Transaction transaction);

	public Transaction selectById(Integer id);

	public List<Transaction> listByTransactionTime(@Param("start") Long start, @Param("end") Long end, @Param("userId") Integer userId);
	
	public List<Transaction> listByStartToEnd(@Param("start") Long start, @Param("end") Long end);

	public Boolean updateTransactionStatusByTxId(String txId, Integer confirmations, String transactionStatus);
}
