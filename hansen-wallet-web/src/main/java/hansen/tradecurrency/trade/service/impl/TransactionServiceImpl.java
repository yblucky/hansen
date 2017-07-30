package hansen.tradecurrency.trade.service.impl;

import hansen.tradecurrency.trade.dao.TransactionDao;
import hansen.tradecurrency.trade.model.Transaction;
import hansen.tradecurrency.trade.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {
	@Autowired
	private TransactionDao transactionDao;

	@Override
	public Boolean insert(Transaction transaction) {
		return transactionDao.insert(transaction);
	}

	@Override
	public Transaction selectById(Integer id) {
		return transactionDao.selectById(id);
	}

	@Override
	public List<Transaction> listByTransactionTime(Long start, Long end,Integer userId) {
		return transactionDao.listByTransactionTime(start, end,userId);
	}

	@Override
	public Boolean updateTransactionStatusByTxId(String txId, Integer confirmations,String transactionStatus) {
		return transactionDao.updateTransactionStatusByTxId(txId, confirmations,transactionStatus);
	}

	@Override
	public List<Transaction> listByStartToEnd(Long start, Long end) {
		// TODO Auto-generated method stub
		return transactionDao.listByStartToEnd(start, end);
	}

}
