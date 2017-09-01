package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.CurrencyType;
import com.constant.TransactionStatusType;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.mapper.WalletTransactionMapper;
import com.model.User;
import com.model.UserDetail;
import com.service.UserDetailService;
import com.service.UserService;
import com.service.WalletTransactionService;
import com.model.Parameter;
import com.model.WalletTransaction;
import com.service.WalletUtil;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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

    @Transactional
    @Override
    public Boolean createTransaction(String uid, Integer currencyType, BitcoinClient client) {
        User condition = new User();
        condition.setUid(Integer.parseInt(uid));
        User user=userService.readOne(condition);
        if (user==null){
            logger.error("读取钱包记录，根据uid获取用户信息失败");
            return false;
        }
        UserDetail  userDetail=userDetailService.readById(user.getId());
        if (userDetail==null){
            logger.error("读取钱包记录，根据uid获取用户详情信息失败");
            return false;
        }

        List<TransactionInfo> infolist = WalletUtil.listTransactions(client, uid, Parameter.WALLET_PAGE_SIZE);
        for (TransactionInfo info : infolist) {
            WalletTransaction conditon = new WalletTransaction();
            conditon.setTxtId(info.getTxId());
            int count = this.readCount(conditon);
            if (count > 0) {
                continue;
            }
            WalletTransaction transaction = new WalletTransaction();
            Integer walletOrderType=0;
            if (currencyType== CurrencyType.TRADE.getCode().intValue()){
                walletOrderType=WalletOrderType.TRADE_COIN_RECHARGE.getCode();
                transaction.setAddress(userDetail.getInTradeAddress());
            }else if (currencyType== CurrencyType.PAY.getCode().intValue()){
                walletOrderType=WalletOrderType.PAY_COIN_RECHARGE.getCode();
                transaction.setAddress(userDetail.getInPayAddress());
            }else if (currencyType== CurrencyType.EQUITY.getCode().intValue()){
                walletOrderType=WalletOrderType.EQUITY_COIN_RECHARGE.getCode();
                transaction.setAddress(userDetail.getInEquityAddress());
            }
            transaction.setRemark(WalletOrderType.fromCode(walletOrderType).getMsg());
            transaction.setOrderType(walletOrderType);
            transaction.setUserId(uid);
            transaction.setAmount(info.getAmount().doubleValue());
            transaction.setCategory(info.getCategory());
            transaction.setConfirmations(Long.valueOf(info.getConfirmations() + ""));
            transaction.setCreateTime(new Date());
            transaction.setFee(0d);
            transaction.setPrepayId("");
            transaction.setMessage("");
            transaction.setTransactionLongTime(new Date().getTime());
            transaction.setTxtId(info.getTxId());
            transaction.setStatus(TransactionStatusType.CHECKING.getCode());
//            if (info.getCategory().equals("immature") || info.getCategory().equals("generate")) {
//                transaction.setAddress("");
//            } else {
//                transaction.setAddress(info.getOtherAccount());
//            }
            transaction.setTransactionStatus(WalletUtil.checkTransactionStatus(0).toString());
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
            updateModel.setTransactionStatus(WalletUtil.checkTransactionStatus(confirmations).getMessage());
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
    public Integer addWalletOrderTransaction(String userId, String address, WalletOrderType walletOrderType, String txtId, String orderNo, Double amount) {
        WalletTransaction transaction = new WalletTransaction();
        transaction.setOrderType(walletOrderType.getCode());
        transaction.setUserId(userId);
        transaction.setAmount(amount);
        transaction.setPrepayId(orderNo);
        transaction.setTxtId(txtId);
        this.create(transaction);
        return 1;
    }

    @Override
    public List<WalletTransaction> readCoinOutterListByUid(String uid,List<Integer> list) throws Exception {
        return walletTransactionMapper.readCoinOutterListByUid(uid,list);
    }

    @Override
    public Integer readCoinOutterCountByUid(String uid,List<Integer> list) {
        return walletTransactionMapper.readCoinOutterCountByUid(uid,list);
    }
}
