package com.service;

import com.alibaba.fastjson.JSONArray;
import com.constant.CurrencyType;
import com.constant.TransactionStatusType;
import com.model.Parameter;
import com.model.WalletTransaction;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Component;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
public class WalletUtil {

    public static BitcoinClient getBitCoinClient(CurrencyType currencyType) throws Exception {
        BitcoinClient bitcoinClient = null;
        if (currencyType.getCode() == 2) {
            bitcoinClient = getBitCoinClient(ParamUtil.getIstance().get(Parameter.PAY_RPCALLOWIP), "user", ParamUtil.getIstance().get(Parameter.PAY_RPCPASSWORD), Integer.valueOf(ParamUtil.getIstance().get(Parameter.PAY_RPCPORT)));
        } else if (currencyType.getCode() == 1) {
            bitcoinClient = getBitCoinClient(ParamUtil.getIstance().get(Parameter.TRADE_RPCALLOWIP), "user", ParamUtil.getIstance().get(Parameter.TRADE_RPCPASSWORD), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TRADE_RPCPORT)));
        } else if (currencyType.getCode() == 3) {
            bitcoinClient = getBitCoinClient(ParamUtil.getIstance().get(Parameter.EQUITY_RPCALLOWIP), "user", ParamUtil.getIstance().get(Parameter.EQUITY_RPCPASSWORD), Integer.valueOf(ParamUtil.getIstance().get(Parameter.EQUITY_RPCPORT)));
        }

        System.out.println(bitcoinClient);
        return bitcoinClient;
    }

    public static BitcoinClient getBitCoinClient(Integer currencyType) throws Exception {
        BitcoinClient bitcoinClient = null;
        if (currencyType == 1) {
            bitcoinClient = getBitCoinClient(ParamUtil.getIstance().get(Parameter.TRADE_RPCALLOWIP), "user", ParamUtil.getIstance().get(Parameter.PAY_RPCPASSWORD), Integer.valueOf(ParamUtil.getIstance().get(Parameter.TRADE_RPCPORT)));
        } else if (currencyType == 2) {
            bitcoinClient = getBitCoinClient(ParamUtil.getIstance().get(Parameter.PAY_RPCALLOWIP), "user", ParamUtil.getIstance().get(Parameter.TRADE_RPCPASSWORD), Integer.valueOf(ParamUtil.getIstance().get(Parameter.PAY_RPCPORT)));
        } else if (currencyType == 3) {
            bitcoinClient = getBitCoinClient(ParamUtil.getIstance().get(Parameter.EQUITY_RPCALLOWIP), "user", ParamUtil.getIstance().get(Parameter.EQUITY_RPCPASSWORD), Integer.valueOf(ParamUtil.getIstance().get(Parameter.EQUITY_RPCPORT)));
        }

        System.out.println(bitcoinClient);
        return bitcoinClient;
    }


    public static BitcoinClient getBitCoinClient(String rpcallowip, String rpcuser, String rpcpassword, int rpcport) {
        BitcoinClient bitcoinClient = null;
        if (bitcoinClient == null) {
            bitcoinClient = new BitcoinClient(rpcallowip, rpcuser, rpcpassword, rpcport);
            System.out.println(bitcoinClient);
        }
        return bitcoinClient;
    }


    // 生成钱包地址
    public static String getAccountAddress(BitcoinClient bitcoinClient, String account) {
        return bitcoinClient.getAccountAddress(account);
    }

    // 查询钱包余额
    public static BigDecimal getBalance(BitcoinClient bitcoinClient) {
        return bitcoinClient.getBalance();
    }

    // 查询钱包余额
    public static BigDecimal getBalance(BitcoinClient bitcoinClient, String account) {
        return bitcoinClient.getBalance(account);
    }

    // 发送币给指定地址
    public static String sendToAddress(BitcoinClient bitcoinClient, String bitcoinAddress, BigDecimal amount, String comment, String commentTo) {
        return bitcoinClient.sendToAddress(bitcoinAddress, amount, comment, commentTo);
    }

    // 从指定账户发送币给指定地址
    public static String sendFrom(BitcoinClient bitcoinClient, String fromAcount, String bitcoinAddress, BigDecimal amount, String comment, String commentTo) {
        return bitcoinClient.sendFrom(fromAcount, bitcoinAddress, amount, 3, comment, commentTo);
    }

    // 根据交易编号查询交易信息
    public static TransactionInfo getTransactionJSON(BitcoinClient bitcoinClient, String txId) {
        return bitcoinClient.getTransaction(txId);
    }

    // 查询单个账户的交易记录
    public static List<TransactionInfo> listTransactions(BitcoinClient bitcoinClient, String account, int count) {
        return bitcoinClient.listTransactions(account, count);
    }

    // 查询单个账户的交易记录
    public static TransactionStatusType checkTransactionStatus(long confirmations) {
        if (confirmations == 0) {
            return TransactionStatusType.UNCHECKED;
        } else if (confirmations > 0 && confirmations < 3) {
            return TransactionStatusType.CHECKING;
        } else if (confirmations > 3) {
            return TransactionStatusType.UNCHECKED;
        }
        return TransactionStatusType.ERROR;
    }

    public static WalletTransaction parseTransactionInfo(BitcoinClient bitcoinClient, JSONObject transactionInfo) {
        WalletTransaction transaction = new WalletTransaction();
        if (transactionInfo.containsKey("amount")) {
            transaction.setAmount(transactionInfo.getDouble("amount"));
        }
        if (transactionInfo.containsKey("fee")) {
            transaction.setFee(transactionInfo.getDouble("fee"));
        }

        if (transactionInfo.containsKey("confirmations")) {
            transaction.setConfirmations(transactionInfo.getLong("confirmations"));
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

        BitcoinClient bitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
//        System.out.println(JSON.toJSONString(bitcoinClient.getTransaction("bfb2b451184259940c8b23dffb5af4e2d6e9a2db44a702e9efc1c0ecbe7a2451")));
        System.out.println(bitcoinClient.getBalance());
        System.out.println(bitcoinClient.getBalance("000001"));
        System.out.println(bitcoinClient.getBalance("01"));
        System.out.println(bitcoinClient.getBalance("02"));
//        bitcoinClient.sendToAddress("rhrgi626nKKSFFqD63KUhHQ3tggbEMYaMt", new BigDecimal("2"), "", "");
        System.out.println(bitcoinClient.sendFrom("02", "rirwjvfXXAMi1ZUHEEq6QwVcMTKSTovdmb", new BigDecimal("2.1"), 3, "", ""));;
//        System.out.println(WalletUtil.getAccountAddress(bitcoinClient, "000001"));
//        System.out.println(JSON.toJSONString(WalletUtil.listTransactions(bitcoinClient,"",100)));
//        System.out.println(JSON.toJSONString(WalletUtil.sendFrom(bitcoinClient, "", "rW53u6WDfZFoVWyHrgcGTU3AwtCJAipk9y", new BigDecimal("2"), "test01", "test001")));
//                System.out.println(JSON.toJSONString(WalletUtil.sendToAddress(bitcoinClient,"rfciwDvRrHS6DFjM3nxriU9XmAMbaXMRaP",new BigDecimal("2"),"test01","test001")));
    }


}
