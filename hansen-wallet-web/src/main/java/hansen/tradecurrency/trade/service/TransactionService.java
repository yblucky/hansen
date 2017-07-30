package hansen.tradecurrency.trade.service;

import hansen.tradecurrency.trade.model.Transaction;

import java.util.List;

public interface TransactionService {
	public Boolean insert(Transaction transaction);

	public Transaction selectById(Integer id);

	public List<Transaction> listByStartToEnd(Long start, Long end);

	public List<Transaction> listByTransactionTime(Long start, Long end, Integer account);

	public Boolean updateTransactionStatusByTxId(String txId, Integer confirmations, String transactionStatus);
}
