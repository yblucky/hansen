package com.task;

import com.base.task.BaseScheduleTask;
import com.constant.TransactionStatusType;
import com.service.WalletTransactionService;
import com.service.WalletUtil;
import com.utils.DateUtils.DateUtils;
import com.model.WalletTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import java.util.List;

import static com.service.WalletUtil.getBitCoinClient;

public class TransactionStatusTask extends BaseScheduleTask{
    @Autowired
    private WalletTransactionService transactionService;

    @Override
    protected void doScheduleTask() {
        System.out.println(DateUtils.currentDate());
        logger.error("---------------------TransactionTask start ---------------------------------------------------------");
        Long start = 1477365782253l;
        Long end = 1477365782253l;
        BitcoinClient bitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
        List<WalletTransaction> list = transactionService.listByStartToEnd(start, end);
        for (WalletTransaction transaction : list) {
            TransactionInfo transactionInfo = WalletUtil.getTransactionJSON(bitcoinClient,transaction.getTxtId());;
            if (transaction.getConfirmations() != transactionInfo.getConfirmations()) {
                Long confir = Long.valueOf(String.valueOf(Long.valueOf(transactionInfo.getConfirmations())));
                TransactionStatusType transactionStatusType = WalletUtil.checkTransactionStatus(confir);
                WalletTransaction updateModel = new WalletTransaction();
                updateModel.setId(null);
                updateModel.setTxtId(transactionInfo.getTxId());
                updateModel.setConfirmations(confir);
                transactionService.updateById(updateModel.getId(), updateModel);
            }
        }
        logger.error("----------------------TransactionTask stop --------------------------------------------------------");
    }
}
