package hansen.tradecurrency.task;

import hansen.tradecurrency.trade.model.Prepay;
import hansen.utils.WalletUtil;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public class HandlePrepayTask extends BaseScheduleTask {
//    @Autowired
//    private PrepayService prepayService;

    @Override
    @Transactional
    protected void doScheduleTask() {
        logger.error("HandlePrepayTask  start.......time:");

        List<Prepay> prepayList = null;
//		    List<Prepay> prepayList = prepayService.listByStartToEnd(Config.START, Config.END);
        for (Prepay prepay : prepayList) {
            String txtId = "";
            try {
                txtId = WalletUtil.sendToAddress(prepay.getAddress(), prepay.getAmount(), prepay.getMessage(), prepay.getPrepayId());
                    /*try {
						prepayService.updatePrepayId(prepay.getPrepayId(),Prepay.PrepayStatus.HANDLED.toString());
						try {
							Transaction transaction = new Transaction();
							transaction.setUserId(prepay.getUserId());
							transaction.setAddress(prepay.getAddress());
							transaction.setAmount(prepay.getAmount());
							transaction.setCategory("send");
							transaction.setConfirmations(0);
							transaction.setCreateTime(new Date());
							transaction.setFee(new BigDecimal("0"));
							transaction.setMessage(prepay.getMessage());
							transaction.setTransactionLongTime(prepay.getTransactionLongTime());
							transaction.setTxtId(txtId);
							transaction.setTransactionTime(new Date());
							transaction.setTransactionStatus(ENumCode.UNCHECKED.toString());
							transaction.setPrepayId(prepay.getPrepayId());
							transactionService.insert(transaction);
						} catch (Exception e) {
							logger.error("HandlePrepayTask  start.......  prepay 预付单指令成功，更改订单状态成功，写入交易记录失败  prepayId=  "+prepay.getPrepayId()+"\n"+"txtId=  "+txtId);
						}
					} catch (Exception e) {
						logger.error("HandlePrepayTask  start.......  prepay 预付单指令成功，更改订单状态失败  prepayId=  "+prepay.getPrepayId());
					}
				*/
            } catch (Exception e) {
                logger.error("HandlePrepayTask  start.......  prepay 预付单发送指令失败  prepayId=  " + prepay.getPrepayId());
                e.printStackTrace();
            }

        }
        Config.START = System.currentTimeMillis() - Config.START_CHANGE;
        Config.END = System.currentTimeMillis();
        logger.error("HandlePrepayTask  end.......");
    }


}
