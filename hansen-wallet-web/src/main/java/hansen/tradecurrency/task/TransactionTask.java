package hansen.tradecurrency.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;
import ru.paradoxs.bitcoin.client.TransactionInfo;
import ru.paradoxs.utils.WalletUtil;
import hansen.model.ENumCode;
import hansen.wallet.trade.model.Transaction;
import hansen.wallet.trade.service.TransactionService;

public class TransactionTask extends BaseScheduleTask {
	@Autowired
	private TransactionService transactionService;

	@Override
	protected void doScheduleTask() {
		Long start = 0l;
		Long end = 1477365782253l;
		
		List<Transaction> list = transactionService.listByStartToEnd(start, end);
		for (Transaction transaction : list) {
			JSONObject jsonObject = WalletUtil.getTransactionJSON(transaction.getTxtId());
			TransactionInfo transactionInfo = WalletUtil.parseTransactionInfo(jsonObject);
			if (transaction.getConfirmations() != transactionInfo.getConfirmations()) {
				Integer confir = Integer.valueOf(String.valueOf(Long.valueOf(transactionInfo.getConfirmations())));
				ENumCode eNumCode = WalletUtil.checkTransactionStatus(confir);
				transactionService.updateTransactionStatusByTxId(transactionInfo.getTxId(), confir, eNumCode.toString());
			}
		}
	}
}
