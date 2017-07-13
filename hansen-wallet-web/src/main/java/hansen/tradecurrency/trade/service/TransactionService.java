package hansen.tradecurrency.trade.service;

import java.util.List;

import hansen.wallet.trade.model.Transaction;

public interface TransactionService {
	public Boolean insert(Transaction transaction);

	public Transaction selectById(Integer id);

	public List<Transaction> listByStartToEnd(Long start, Long end);

	public List<Transaction> listByTransactionTime(Long start, Long end, Integer account);

	public Boolean updateTransactionStatusByTxId(String txId, Integer confirmations, String transactionStatus);
}
