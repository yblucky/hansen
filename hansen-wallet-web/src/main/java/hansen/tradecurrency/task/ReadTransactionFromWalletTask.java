package hansen.tradecurrency.task;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import ru.paradoxs.bitcoin.client.TransactionInfo;
import ru.paradoxs.utils.ToolUtil;
import ru.paradoxs.utils.WalletUtil;
import hansen.wallet.trade.model.Prepay;
import hansen.wallet.trade.model.Transaction;
import hansen.wallet.trade.service.PrepayService;
import hansen.wallet.trade.service.TransactionService;

public class ReadTransactionFromWalletTask extends BaseScheduleTask {
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private PrepayService prepayService;

	@Override
	protected void doScheduleTask() {
		logger.error("readTransactionFromWalletTask  start.......time:");
		logger.error("readTransactionFromWalletTask  start.......time:");
		logger.error("readTransactionFromWalletTask  start.......time:");
		try {
			List<TransactionInfo> infolist=WalletUtil.listTransactionsDefault();
			for (TransactionInfo info : infolist) {
//				Config.LAST_WALLET_INSERT_TIME=info.getTime();
				int count=transactionService.selectByTxtIdAndCategory(info.getTxId(),info.getCategory());
				if (count>0) {
//					continue;
					break;
				}
				Transaction transaction = new Transaction();
				transaction.setUserId(info.getAccount());
				transaction.setAmount(info.getAmount());
				transaction.setCategory(info.getCategory());
				transaction.setConfirmations(Integer.valueOf(info.getConfirmations()+""));
				transaction.setCreateTime(new Date());
				transaction.setFee(info.getFee());
				transaction.setPrepayId(info.getTo());
				transaction.setMessage(info.getMessage());
				transaction.setTransactionLongTime(info.getTime()*1000);
				transaction.setTxtId(info.getTxId());
				transaction.setTransactionTime(new Date(info.getTime()*1000));
				if (info.getCategory().equals("immature") || info.getCategory().equals("generate")) {
					transaction.setAddress("");
				}else{
					transaction.setAddress(info.getAddress());
				}
				transaction.setTransactionStatus(WalletUtil.checkTransactionStatus(Integer.valueOf(info.getConfirmations()+"")).toString());
				transactionService.insert(transaction);
				if (ToolUtil.isNotEmpty(info.getTo())) {
					prepayService.updatePrepayId(info.getTo(),Prepay.PrepayStatus.HANDLED.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.error("readTransactionFromWalletTask  end.......");
		logger.error("readTransactionFromWalletTask  end.......");
		logger.error("readTransactionFromWalletTask  end.......");
	}
}
