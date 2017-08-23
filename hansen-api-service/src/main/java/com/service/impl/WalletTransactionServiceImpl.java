package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.WalletOrderType;
import com.mapper.WalletTransactionMapper;
import com.service.UserService;
import com.service.WalletTransactionService;
import com.model.Parameter;
import com.model.WalletTransaction;
import com.service.WalletUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import java.util.Date;
import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class WalletTransactionServiceImpl extends CommonServiceImpl<WalletTransaction> implements WalletTransactionService {

    @Autowired
    private WalletTransactionMapper walletTransactionMapper;
    @Autowired
    private UserService userService;
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
            transaction.setCurrencyType(currencyType);
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
            transaction.setTransactionStatus(WalletUtil.checkTransactionStatus(info.getConfirmations()).toString());
            this.create(transaction);
        }
        return true;
    }

    @Override
    public void checkTransactionListStatus(String userId, Integer currencyType) throws Exception {
        WalletTransaction condition = new WalletTransaction();
        condition.setCurrencyType(currencyType);
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
            info = WalletUtil.getTransactionJSON(WalletUtil.getBitCoinClient(transaction.getCurrencyType()), transaction.getTxtId());
            updateModel.setUserId(transaction.getUserId());
            updateModel.setTxtId(transaction.getTxtId());
            updateModel.setConfirmations(info.getConfirmations());
            if (info.getConfirmations() > 3) {
                isUpdate = true;
                confirmations = info.getConfirmations();
            }
        }
        if (isUpdate) {
            updateModel.setTransactionStatus(WalletUtil.checkTransactionStatus(confirmations).getMessage());
            this.updateById(transaction.getId(), updateModel);
            if (transaction.getCurrencyType() == 1) {
                userService.updatePayAmtByUserId(transaction.getUserId(), transaction.getAmount());
            } else if (transaction.getCurrencyType() == 2) {
                userService.updateTradeAmtByUserId(transaction.getUserId(), transaction.getAmount());
            } else if (transaction.getCurrencyType() == 3) {
                userService.updateEquityAmtByUserId(transaction.getUserId(), transaction.getAmount());
            }
        }
    }

    @Override
    public Integer addWalletOrderTransaction(String userId, String address, WalletOrderType walletOrderType, String txtId, String orderNo, Double amount) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setCurrencyType(walletOrderType.getCode());
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setPrepayId(orderNo);
        transaction.setTxtId(txtId);
        this.create(transaction);
        return 1;
    }
}
