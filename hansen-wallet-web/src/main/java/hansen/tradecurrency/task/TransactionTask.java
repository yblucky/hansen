package hansen.tradecurrency.task;

import java.util.List;

import com.hansen.common.constant.ENumCode;
import hansen.bitcoin.client.TransactionInfo;
import hansen.tradecurrency.trade.model.Transaction;
import hansen.tradecurrency.trade.service.TransactionService;
import hansen.utils.WalletUtil;
import org.springframework.beans.factory.annotation.Autowired;

import net.sf.json.JSONObject;

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
