package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.Constant;
import com.constant.CurrencyType;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.mapper.WalletTransactionMapper;
import com.model.SysUser;
import com.model.User;
import com.service.UserDetailService;
import com.service.UserService;
import com.service.WalletTransactionService;
import com.model.Parameter;
import com.model.WalletTransaction;
import com.service.WalletUtil;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service("transactionService")
public class WalletTransactionServiceImpl extends CommonServiceImpl<WalletTransaction> implements WalletTransactionService {

    @Autowired
    private WalletTransactionMapper walletTransactionMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Override
    protected CommonDao<WalletTransaction> getDao() {
        return walletTransactionMapper;
    }

    @Override
    protected Class<WalletTransaction> getModelClass() {
        return WalletTransaction.class;
    }


    @Override
    public List<WalletTransaction> listByTransactionTime(Long start, Long end, Integer userId) {
        return walletTransactionMapper.listByTransactionTime(start, end, userId);
    }


    @Override
    public List<WalletTransaction> listByStartToEnd(Long start, Long end) {
        return walletTransactionMapper.listByStartToEnd(start, end);
    }

    @Override
    public void listTransactionsByTag(String userId, Integer currencyType) throws Exception {
        BitcoinClient client = WalletUtil.getBitCoinClient(currencyType);
        if (client == null) {
            return;
        }
        this.createTransaction(userId, currencyType, client);
    }

    @Override
    public Boolean createTransaction(String userId, Integer currencyType, BitcoinClient client) {
        List<TransactionInfo> infolist = WalletUtil.listTransactions(client, userId, Parameter.WALLET_PAGE_SIZE);
        for (TransactionInfo info : infolist) {
            WalletTransaction conditon = new WalletTransaction();
            conditon.setTxtId(info.getTxId());
            int count = this.readCount(conditon);
            if (count > 0) {
                continue;
//                break;
            }
            WalletTransaction transaction = new WalletTransaction();
            transaction.setOrderType(currencyType);
            transaction.setUserId(info.getOtherAccount());
            transaction.setAmount(info.getAmount().doubleValue());
            transaction.setCategory(info.getCategory());
            transaction.setConfirmations(Long.valueOf(info.getConfirmations() + ""));
            transaction.setCreateTime(new Date());
            transaction.setFee(info.getFee().doubleValue());
            transaction.setPrepayId(info.getTo());
            transaction.setMessage(info.getMessage());
            transaction.setTransactionLongTime(info.getTime() * 1000);
            transaction.setTxtId(info.getTxId());
            transaction.setTransactionTime(new Date(info.getTime() * 1000));
            if (info.getCategory().equals("immature") || info.getCategory().equals("generate")) {
                transaction.setAddress("");
            } else {
                transaction.setAddress(info.getOtherAccount());
            }
            transaction.setTransactionStatus(WalletUtil.checkTransactionStatus(info.getConfirmations()).getCode());
            this.create(transaction);
        }
        return true;
    }

    @Override
    public void checkTransactionListStatus(String userId, Integer currencyType) throws Exception {
        WalletTransaction condition = new WalletTransaction();
        condition.setOrderType(currencyType);
        condition.setUserId(userId);
        List<WalletTransaction> list = this.readAll(condition);
        for (WalletTransaction transaction : list) {
            this.checkTransactionStatus(transaction);
        }
    }

    @Override
    @Transactional
    public void checkTransactionStatus(WalletTransaction transaction) throws Exception {
        Boolean isUpdate = false;
        long confirmations = 0;
        TransactionInfo info = null;
        WalletTransaction updateModel = new WalletTransaction();
        if (transaction.getConfirmations() > 3) {
            confirmations = transaction.getConfirmations();
            isUpdate = true;
        } else {
            info = WalletUtil.getTransactionJSON(WalletUtil.getBitCoinClient(transaction.getOrderType()), transaction.getTxtId());
            updateModel.setUserId(transaction.getUserId());
            updateModel.setTxtId(transaction.getTxtId());
            updateModel.setConfirmations(info.getConfirmations());
            if (info.getConfirmations() > 3) {
                isUpdate = true;
                confirmations = info.getConfirmations();
            }
        }
        if (isUpdate) {
            updateModel.setTransactionStatus(WalletUtil.checkTransactionStatus(confirmations).getCode());
            this.updateById(transaction.getId(), updateModel);
            if (transaction.getOrderType() == 1) {
                userService.updatePayAmtByUserId(transaction.getUserId(), transaction.getAmount());
            } else if (transaction.getOrderType() == 2) {
                userService.updateTradeAmtByUserId(transaction.getUserId(), transaction.getAmount());
            } else if (transaction.getOrderType() == 3) {
                userService.updateEquityAmtByUserId(transaction.getUserId(), transaction.getAmount());
            }
        }
    }

    @Override
    public Integer addWalletOrderTransaction(String userId, String address, WalletOrderType walletOrderType, WalletOrderStatus walletOrderStatus, String txtId, String orderNo, Double amount) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setRemark(walletOrderType.getMsg());
        transaction.setOrderType(walletOrderType.getCode());
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setPrepayId(orderNo);
        transaction.setTxtId(txtId);
        transaction.setStatus(walletOrderStatus.getCode());
        this.create(transaction);
        return 1;
    }


    @Override
    @Transactional
    public Boolean checkCoinOut(SysUser loginUser, String orderId,Integer status) throws Exception {
        WalletTransaction  order = this.readById(orderId);

        if (order==null){
            return false;
        }
        if (ToolUtil.isNotEmpty(order.getUserId())){
            return false;
        }
        User con = new User();
        con.setUid(Integer.valueOf(order.getUserId()));
        User user=userService.readOne(con);
        CurrencyType currencyType = WalletOrderType.getCoinTypeFromWalletOrderTypeCode(order.getOrderType());
        if (user==null){
            logger.error("提币审核找不到提币用户，提币失败");
        }
        if (WalletOrderStatus.DENIED.getCode()==status){
            logger.error("审核不通过，原路退回");
            if (CurrencyType.TRADE.getCode()==currencyType.getCode()){
                userService.updateTradeAmtByUserId(user.getId(),-order.getAmount());
                userDetailService.updateForzenTradeAmtByUserId(user.getId(),order.getAmount());
            }else if (CurrencyType.PAY.getCode()==currencyType.getCode()){
                userService.updatePayAmtByUserId(user.getId(),-order.getAmount());
                userDetailService.updateForzenPayAmtByUserId(user.getId(),order.getAmount());
            }else  if (CurrencyType.EQUITY.getCode()==currencyType.getCode()){
                userService.updateEquityAmtByUserId(user.getId(),-order.getAmount());
                userDetailService.updateForzenEquityAmtByUserId(user.getId(),order.getAmount());
            }

        }
        WalletTransaction updateModel = new WalletTransaction();

        BitcoinClient client = WalletUtil.getBitCoinClient(currencyType);
        String txtId = WalletUtil.sendToAddress(client, order.getAddress(), new BigDecimal(order.getAmount().toString()), "用户" + order.getUserId() + "提币", "用户" + order.getAddress() + "收币");
        if (ToolUtil.isNotEmpty(txtId)) {
            updateModel.setStatus(WalletOrderStatus.CONFIRMING.getCode());
            updateModel.setRemark("提币审核通过，审核人：" + loginUser.getUserName());
            this.addWalletOrderTransaction(Constant.SYSTEM_USER_ID,order.getAddress(), WalletOrderType.fromCode(order.getOrderType()), WalletOrderStatus.CONFIRMING, txtId, OrderNoUtil.get(), order.getAmount());
        }
        updateModel.setId(orderId);
        updateModel.setRemark(WalletOrderStatus.CONFIRMING.getMsg());
        this.updateById(order.getId(), updateModel);
        return true;
    }
}
