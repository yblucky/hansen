package com.hansen.tradecurrency.trade.controller;

import com.base.page.JsonResult;
import com.common.constant.ENumCode;
import com.common.utils.WalletUtil;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.service.UserService;
import com.hansen.service.WalletTransactionService;
import com.hansen.tradecurrency.vo.TransactionInfoVo;
import com.model.WalletTransaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

import static com.common.utils.WalletUtil.getBitCoinClient;

@Controller
@RequestMapping("/operation")
public class OperationController {
    @Resource
    private UserService userService;
    @Resource
    private WalletTransactionService transactionService;


    @ResponseBody
    @RequestMapping(value = "/sendToAddress", method = RequestMethod.POST)
    public JsonResult sendToAddress(@RequestBody TransactionInfoVo vo) {
        JsonResult result = null;
        WalletTransaction transaction = null;
        try {
            BitcoinClient bitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
            BigDecimal money = new BigDecimal(vo.getAmount());
            String res = WalletUtil.sendToAddress(bitcoinClient, vo.getAddress(), money, vo.getComment(), vo.getCommentTo());
            transaction = new WalletTransaction();
            transaction.setAddress(vo.getAddress());
            transaction.setAmount(vo.getAmount());
            transaction.setCategory(vo.getCategory());
            transaction.setTransactionStatus(ENumCode.UNCHECKED.toString());
            transaction.setFee(0d);
            transaction.setTxtId(res);
            transaction.setConfirmations(0);
            transaction.setTransactionLongTime(new Date().getTime());
            transaction.setMessage("");
            transactionService.create(transaction);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult(transaction);
    }

    @ResponseBody
    @RequestMapping(value = "/getBalance", method = RequestMethod.GET)
    public JsonResult getBalance(String account) {

        JsonResult result = null;
        try {
            BitcoinClient bitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
            BigDecimal blance = null;
            if (ToolUtil.isEmpty(account)) {
                blance = WalletUtil.getBalance(bitcoinClient);
            } else {
                blance = WalletUtil.getBalance(bitcoinClient, account);
            }
            result = new JsonResult(blance);
        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getTransaction", method = RequestMethod.GET)
    public JsonResult getTransactionJSON(String txtId) {
        JsonResult result = null;
        try {
            BitcoinClient bitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
            TransactionInfo transaction = new TransactionInfo();

            TransactionInfo transactionInfo = WalletUtil.getTransactionJSON(bitcoinClient, txtId);


            transaction.setAmount(transactionInfo.getAmount());


            transaction.setFee(transactionInfo.getFee());


            transaction.setConfirmations(transactionInfo.getConfirmations());


            transaction.setTime(transactionInfo.getTime());
            transaction.setMessage(transactionInfo.getMessage());



			/*	JSONArray jsonArray = JSONArray.parseArray(transactionInfo.g);
                if (jsonArray.size() > 0) {
					transaction.setDetails((Map<String, Object>) JSONArray.parseArray(transactionInfo.getString("details")).get(0));
				}*/


            result = new JsonResult(ENumCode.SUCCESS);
            long confirmations = transaction.getConfirmations();

//			result.addData("status", WalletUtil.checkTransactionStatus((int) confirmations));


        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/listtransactions", method = RequestMethod.GET)
    public JsonResult listtransactions(@RequestParam(value = "account", defaultValue = "", required = false) String account, @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = "1", required = false) int pageSize) {
        JsonResult result = null;
        try {
            BitcoinClient bitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
            TransactionInfo transaction = new TransactionInfo();
            List<TransactionInfo> transactionInfoList = WalletUtil.listTransactions(bitcoinClient, account, pageSize);
            result = new JsonResult(ENumCode.SUCCESS);
            List<Map<String, Object>> list = new ArrayList<>();
            for (TransactionInfo transactionInfo : transactionInfoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("category", transactionInfo.getCategory());
                map.put("amount", transactionInfo.getAmount());
                map.put("confirmations", transactionInfo.getConfirmations());
                map.put("txId", transactionInfo.getTxId());
                map.put("time", transactionInfo.getTime());
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/listtransactionsByTime", method = RequestMethod.GET)
    public JsonResult listtransactions(@RequestParam(value = "account", required = false) String account, @RequestParam(value = "start", required = false) Long start, @RequestParam(value = "end", required = false) Long end, @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = "1", required = false) int pageSize) {
        JsonResult result = new JsonResult(ENumCode.SUCCESS);
        Integer userId = null;
        if (start == null || end == null) {
            return new JsonResult("time is empty");
        }
        if (account != null) {
            userId = new Integer(account);
        }
        try {
            List<WalletTransaction> transactionList = transactionService.listByTransactionTime(start, end, userId);
            result = new JsonResult(ENumCode.SUCCESS);
            List<Map<String, Object>> list = new ArrayList<>();
            for (WalletTransaction transaction : transactionList) {
                Map<String, Object> map = new HashMap<>();
                map.put("category", transaction.getCategory());
                map.put("amount", transaction.getAmount());
                map.put("confirmations", transaction.getConfirmations());
                map.put("txId", transaction.getTxtId());
                map.put("time", transaction.getTransactionLongTime());
                map.put("status", transaction.getTransactionStatus());
                map.put("address", transaction.getAddress());
                map.put("userId", transaction.getUserId());
                list.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println("**************");
        System.out.println("**************");
        System.out.println("===========");
        System.out.println("===========");
        System.out.println("===========");
        System.out.println(new Date().getTime());
        System.out.println(new Date().getTime() / 100000);
        System.out.println(Integer.valueOf(String.valueOf(new Date().getTime() / 30000)));
    }

}
