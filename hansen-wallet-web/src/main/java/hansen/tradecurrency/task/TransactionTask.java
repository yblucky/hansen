package hansen.tradecurrency.task;

import com.hansen.common.constant.ENumCode;
import com.hansen.model.WalletTransaction;
import com.hansen.service.WalletTransactionService;
import hansen.utils.WalletUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TransactionTask extends BaseScheduleTask {
    @Autowired
    private WalletTransactionService transactionService;

    @Override
    protected void doScheduleTask() {
        Long start = 0l;
        Long end = 1477365782253l;

        List<WalletTransaction> list = transactionService.listByStartToEnd(start, end);
        for (WalletTransaction transaction : list) {
            JSONObject jsonObject = WalletUtil.getTransactionJSON(transaction.getTxtId());
            WalletTransaction transactionInfo = WalletUtil.parseTransactionInfo(jsonObject);
            if (transaction.getConfirmations() != transactionInfo.getConfirmations()) {
                Integer confir = Integer.valueOf(String.valueOf(Long.valueOf(transactionInfo.getConfirmations())));
                ENumCode eNumCode = WalletUtil.checkTransactionStatus(confir);
                WalletTransaction updateModel = new WalletTransaction();
                updateModel.setId(null);
                updateModel.setTxtId(transactionInfo.getTxtId());
                updateModel.setConfirmations(confir);
                transactionService.updateById(updateModel.getId(), updateModel);
            }
        }
    }
}
