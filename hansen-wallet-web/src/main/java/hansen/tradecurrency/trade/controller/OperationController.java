package hansen.tradecurrency.trade.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hansen.base.page.JsonResult;
import com.hansen.common.utils.constant.ENumCode;
import com.hansen.model.User;
import com.hansen.service.UserService;
import hansen.bitcoin.client.TransactionInfo;
import hansen.tradecurrency.trade.model.Transaction;
import hansen.tradecurrency.trade.service.TransactionService;
import hansen.tradecurrency.trade.vo.TransactionInfoVo;
import hansen.utils.ToolUtil;
import hansen.utils.WalletUtil;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequestMapping("/rtb")
public class OperationController {
    @Resource
    private UserService userService;
    @Resource
    private TransactionService transactionService;

    @RequestMapping(value = "/getAddressById", method = RequestMethod.GET)
    @ResponseBody
    public JsonResult getAccountAddress(String account) {
        JsonResult result = null;
        try {
            if (ToolUtil.isEmpty(account)) {
                return new JsonResult("账号不能为空");
            }
            User u = userService.readById(account);
            if (u != null) {
                return new JsonResult("账号已存在");
            }
            String address = WalletUtil.getAccountAddress(account);
            if (ToolUtil.isNotEmpty(address)) {
                User user = new User();
                userService.create(user);
            }
            return new JsonResult(address);
        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/sendToAddress", method = RequestMethod.POST)
    public JsonResult sendToAddress(@RequestBody TransactionInfoVo vo) {
        JsonResult result = null;
        try {
            BigDecimal money = new BigDecimal(vo.getAmount());
            String res = WalletUtil.sendToAddress(vo.getAddress(), money, vo.getComment(), vo.getCommentTo());
//            Transaction transaction = new Transaction();
//            transaction.setAddress(vo.getAddress());
//            transaction.setAmount(new BigDecimal(vo.getAmount()));
//            transaction.setCategory(vo.getCategory());
//            transaction.setTransactionStatus(ENumCode.UNCHECKED.toString());
//            transaction.setFee(new BigDecimal("0"));
//            transaction.setTxtId(res);
//            transaction.setConfirmations(0);
//            transaction.setTransactionLongTime(new Date().getTime());
//            transaction.setMessage("");
//            transactionService.insert(transaction);
//            result = new JsonResult(ENumCode.SUCCESS);
//            result.addData("txtId", res);
        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        System.out.println(JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
        System.out.println(JSON.toJSON(result));
        return result;
    }

    @ResponseBody
    @RequestMapping(value = "/getBalance", method = RequestMethod.GET)
    public JsonResult getBalance(String account) {
        JsonResult result = null;
        try {
            BigDecimal blance = null;
            if (ToolUtil.isEmpty(account)) {
                blance = WalletUtil.getBalance();
            } else {
                blance = WalletUtil.getBalance(account);
            }
            return new JsonResult(blance);
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
            TransactionInfo transaction = new TransactionInfo();

            JSONObject transactionInfo = WalletUtil.getTransactionJSON(txtId);

            if (transactionInfo.containsKey("amount")) {
                transaction.setAmount(new BigDecimal(transactionInfo.getString("amount")));
            }
            if (transactionInfo.containsKey("fee")) {
                transaction.setFee(new BigDecimal(transactionInfo.getString("fee")));
            }

            if (transactionInfo.containsKey("confirmations")) {
                transaction.setConfirmations(transactionInfo.getLong("confirmations"));
            }

            if (transactionInfo.containsKey("blocktime")) {
                transaction.setBlockTime((Long.valueOf(transactionInfo.getString("blocktime")) * 1000));
            }

            if (transactionInfo.containsKey("timereceived")) {
                transaction.setBlockTime((Long.valueOf(transactionInfo.getString("timereceived")) * 1000));
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

            result = new JsonResult(ENumCode.SUCCESS);
            long confirmations = transaction.getConfirmations();
            Map map = new HashMap();

            map.put("status", WalletUtil.checkTransactionStatus((int) confirmations));

            map.put("txId", transaction);
            return new JsonResult(map);
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
            TransactionInfo transaction = new TransactionInfo();
            List<TransactionInfo> transactionInfoList = WalletUtil.listTransactions(account, pageSize, (pageNo - 1) * pageSize + 1);
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
            return new JsonResult(list);

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
            List<Transaction> transactionList = transactionService.listByTransactionTime(start, end, userId);
            result = new JsonResult(ENumCode.SUCCESS);
            List<Map<String, Object>> list = new ArrayList<>();
            for (Transaction transaction : transactionList) {
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
            return new JsonResult(list);

        } catch (Exception e) {
            e.printStackTrace();
            result = new JsonResult(ENumCode.ERROR);
        }
        return result;
    }

    public static void main(String[] args) throws Exception {
        System.out.println(ToolUtil.getCurrentDateTime());
        System.out.println("**************");
        System.out.println(ToolUtil.getCurrentDateTime());
        System.out.println("**************");
        System.out.println("===========");
        System.out.println("===========");
        System.out.println("===========");
        System.out.println(new Date().getTime());
        System.out.println(new Date().getTime() / 100000);
        System.out.println(Integer.valueOf(String.valueOf(new Date().getTime() / 30000)));
    }

}
