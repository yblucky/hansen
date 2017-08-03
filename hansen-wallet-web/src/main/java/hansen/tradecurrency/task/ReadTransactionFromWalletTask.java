package hansen.tradecurrency.task;

import com.hansen.model.WalletTransaction;
import com.hansen.service.WalletTransactionService;
import hansen.utils.WalletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import java.util.Date;
import java.util.List;

public class ReadTransactionFromWalletTask extends BaseScheduleTask {
    @Autowired
    private WalletTransactionService transactionService;

    @Override
    protected void doScheduleTask() {
        logger.error("readTransactionFromWalletTask  start.......time:");
        logger.error("readTransactionFromWalletTask  start.......time:");
        logger.error("readTransactionFromWalletTask  start.......time:");
        try {

            List<TransactionInfo> infolist = WalletUtil.listTransactions("", 10);
            for (TransactionInfo info: infolist) {
//				Config.LAST_WALLET_INSERT_TIME=info.getTime();
                WalletTransaction conditon = new WalletTransaction();
                conditon.setTxtId("");
                conditon.setCategory("send");
                int count = transactionService.readCount(conditon);
                if (count > 0) {
//					continue;
                    break;
                }
                WalletTransaction transaction = new WalletTransaction();
                transaction.setUserId(info.getOtherAccount());
                transaction.setAmount(info.getAmount().doubleValue());
                transaction.setCategory(info.getCategory());
                transaction.setConfirmations(Integer.valueOf(info.getConfirmations() + ""));
                transaction.setCreateTime(new Date());
                transaction.setFee(info.getFee().doubleValue());
                transaction.setPrepayId(info.getTo());
                transaction.setMessage(info.getMessage());
                transaction.setTransactionLongTime(info.getTime() * 1000);
                transaction.setTxtId(info.getTxId());
                transaction.setTransactionTime(new Date(info.getTime()*1000));
                if (info.getCategory().equals("immature") || info.getCategory().equals("generate")) {
                    transaction.setAddress("");
                } else {
                    transaction.setAddress(info.getOtherAccount());
                }
                transaction.setTransactionStatus(hansen.utils.WalletUtil.checkTransactionStatus(Integer.valueOf(info.getConfirmations() + "")).toString());
                transactionService.create(transaction);
                if (hansen.utils.ToolUtil.isNotEmpty(info.getTo())) {
//                    prepayService.updatePrepayId(info.getTo(), "HANDLED");
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
