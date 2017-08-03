package com.hansen.common.utils;

import com.alibaba.fastjson.JSONArray;
import com.hansen.common.constant.ENumCode;
import com.hansen.model.WalletTransaction;
import net.sf.json.JSONObject;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class WalletUtil {
    public static BitcoinClient bitcoinClient = null;

    public static void main(String[] args) {
        long a = new Date().getTime();
        System.out.println(new Date().getTime());
     /*   for (int i = 0; i < 1000; i++) {

            // System.out.println(WalletUtil.client.getBalance("123"));
            System.out.println(WalletUtil.sendToAddress("ro8b9yAGuatvt6BubqHSCmbVwsc6Xsk3jx", new BigDecimal(0.02), "a", "b"));
        }
        System.out.println((new Date().getTime() - a) / 1000);*/
        // WalletUtil.sendToAddress("ro8b9yAGuatvt6BubqHSCmbVwsc6Xsk3jx", new
        // BigDecimal(0.02), "a", "b");
        // WalletUtil.getAccountAddress("000001");
//        System.out.println(WalletUtil.getAccountAddress("000001"));

    }

    // 生成钱包地址
    public static String getAccountAddress(String account) {
        return bitcoinClient.getAccountAddress(account);
    }

    // 查询钱包余额
    public static BigDecimal getBalance() {
        return bitcoinClient.getBalance();
    }

    // 查询钱包余额
    public static BigDecimal getBalance(String account) {
        return bitcoinClient.getBalance(account);
    }

    // 发送币给指定地址
    public static String sendToAddress(String bitcoinAddress, BigDecimal amount, String comment, String commentTo) {
        return bitcoinClient.sendToAddress(bitcoinAddress, amount, comment, commentTo);
    }

    // 根据交易编号查询交易信息
    public static TransactionInfo getTransactionJSON(String txId) {
        return bitcoinClient.getTransaction(txId);
    }

    // 查询单个账户的交易记录
    public static List<TransactionInfo> listTransactions(String account, int count) {
        return bitcoinClient.listTransactions(account, count);
    }

    // 查询单个账户的交易记录
    public static ENumCode checkTransactionStatus(int confirmations) {
        if (confirmations == 0) {
            return ENumCode.UNCHECKED;
        } else if (confirmations > 0 && confirmations < 3) {
            return ENumCode.CHECKING;
        } else if (confirmations > 3) {
            return ENumCode.UNCHECKED;
        }
        return ENumCode.ERROR;
    }

    public static WalletTransaction parseTransactionInfo(JSONObject transactionInfo) {
        WalletTransaction transaction = new WalletTransaction();
        if (transactionInfo.containsKey("amount")) {
            transaction.setAmount(transactionInfo.getDouble("amount"));
        }
        if (transactionInfo.containsKey("fee")) {
            transaction.setFee(transactionInfo.getDouble("fee"));
        }

        if (transactionInfo.containsKey("confirmations")) {
            transaction.setConfirmations(transactionInfo.getInt("confirmations"));
        }

        if (transactionInfo.containsKey("blocktime")) {
            transaction.setTransactionLongTime((Long.valueOf(transactionInfo.getString("blocktime")) * 1000));
        }

        if (transactionInfo.containsKey("timereceived")) {
            transaction.setTransactionLongTime((Long.valueOf(transactionInfo.getString("timereceived")) * 1000));
        }

        if (transactionInfo.containsKey("comment")) {
            transaction.setMessage(transactionInfo.getString("comment"));
        }

        if (transactionInfo.containsKey("details")) {
            JSONArray jsonArray = JSONArray.parseArray(transactionInfo.getString("details"));
            if (jsonArray.size() > 0) {
                transaction.setDetails((Map<String, Object>) JSONArray.parseArray(transactionInfo.getString("details")).get(0));
            }
        }
        return transaction;
    }

    public static BitcoinClient getBitcoinClient(String rpcallowip, String rpcuser, String rpcpassword, int rpcport) {
        if (bitcoinClient == null) {
            bitcoinClient = new BitcoinClient(rpcallowip, rpcuser, rpcpassword);
            System.out.println(bitcoinClient);
        }
        return bitcoinClient;
    }



}
