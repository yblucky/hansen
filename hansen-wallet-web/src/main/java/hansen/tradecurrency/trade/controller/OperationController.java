package hansen.tradecurrency.trade.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.hansen.service.UserService;
import com.hansen.service.WalletTransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

import net.sf.json.JSONObject;
import ru.paradoxs.bitcoin.client.TransactionInfo;
import ru.paradoxs.utils.ToolUtil;
import ru.paradoxs.utils.WalletUtil;

@Controller
@RequestMapping("/rtb")
public class OperationController {
	@Resource
	private UserService userService;
	@Resource
	private WalletTransactionService transactionService;

	@RequestMapping(value = "/getAddressById", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getAccountAddress(String account) {
		JsonResult result = new JsonResult(ENumCode.SUCCESS);
		try {
			if (ToolUtil.isEmpty(account)) {
				result.addData("message", "账号不能为空");
				return result;
			}
			RtbUser u = userService.selectByUserId(Integer.valueOf(account));
			if (u != null) {
				result.addData("message", "账号已存在");
				return result;
			}
			String address = WalletUtil.getAccountAddress(account);
			if (ToolUtil.isNotEmpty(address)) {
				RtbUser user = new RtbUser();
				user.setAddress(address);
				user.setBalance(new BigDecimal("0.0"));
				user.setUserId(Integer.valueOf(account));
				userService.insert(user);
			}
			result.addData("address", address);
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
			Transaction transaction = new Transaction();
			transaction.setAddress(vo.getAddress());
			transaction.setAmount(new BigDecimal(vo.getAmount()));
			transaction.setCategory(vo.getCategory());
			transaction.setTransactionStatus(ENumCode.UNCHECKED.toString());
			transaction.setFee(new BigDecimal("0"));
			transaction.setTxtId(res);
			transaction.setConfirmations(0);
			transaction.setTransactionLongTime(new Date().getTime());
			transaction.setMessage("");
			transactionService.insert(transaction);
			result = new JsonResult(ENumCode.SUCCESS);
			result.addData("txtId", res);
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
			result = new JsonResult(ENumCode.SUCCESS);
			result.addData("blance", blance);
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

			result.addData("status", WalletUtil.checkTransactionStatus((int) confirmations));

			result.addData("txId", transaction);
		} catch (Exception e) {
			e.printStackTrace();
			result = new JsonResult(ENumCode.ERROR);
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/listtransactions", method = RequestMethod.GET)
	public JsonResult listtransactions( @RequestParam(value = "account", defaultValue = "", required = false) String account, @RequestParam(value = "pageNo", defaultValue = "1", required = false) int pageNo, @RequestParam(value = "pageSize", defaultValue = "1", required = false) int pageSize) {
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
			result.addData("list", list);

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
			result.addData("message", "time is empty");
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
			result.addData("list", list);

		} catch (Exception e) {
			e.printStackTrace();
			result = new JsonResult(ENumCode.ERROR);
		}
		return result;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(ToolUtil.getCurrentDateTime());
		System.out.println(ToolUtil.convertByteToString(ru.paradoxs.utils.Key.getEncode()));
		System.out.println("**************");
		System.out.println(ToolUtil.getCurrentDateTime());
		System.out.println("**************");
		System.out.println("===========");
		System.out.println(ru.paradoxs.utils.Key.check(ToolUtil.convertByteToString(ru.paradoxs.utils.Key.getEncode())));;
		System.out.println("===========");
		System.out.println(ru.paradoxs.utils.Key.check("8,-14,-99,100,-8,-128,110,-105,-91,104,99,-112,-105,112,34,26,75,-30,63,56,88,72,-87,86,116,-51,-80,-118,-21,-25,93,-16,-73,-42,-91,-93,-37,-3,25,-83,-68,94,64,-9,125,26,-92,-5,20,-121,71,22,-34,96,45,72,126,118,106,-50,-85,-17,45,-96,-67,124,118,96,-75,-101,69,-63,-4,-83,88,-46,75,83,96,-60,-67,-9,110,81,41,-58,24,22,-121,-80,-29,84,61,69,56,98,-95,-77,-5,25,-48,111,-120,47,-121,76,-100,106,-79,-99,-68,70,100,-24,6,-128,-38,-77,-64,119,-18,13,42,-120,-82,117,-75,-28,50,-86,-28,-41,20,-48,-119,31,123,-41,-24,-74,-10,-114,102,-108,45,20,-59,0,-67,-115,-4,-73,-38,24,13,-90,-14,124,84,19,7,29,51,28,112,100,-29,-43,53,-20,-16,75,-97,4,-43,-66,-33,46,-40,-85,-97,46,60,-78,121,40,-96,12,-68,-112,-14,-114,-126,71,99,21,-11,-49,-15,-72,-79,-127,34,18,-125,-105,-120,-15,-53,61,96,-43,-126,30,12,13,-79,107,-116,32,69,115,-19,-123,50,-88,-89,-124,18,-15,-48,-100,83,19,61,8,5,113,14,4,71,-13,55,-103,85,97,88,97,101,0,45,-104,36,-38,6,-75"));;
		System.out.println("===========");
		System.out.println(new Date().getTime());
		System.out.println(new Date().getTime() / 100000);
		System.out.println(Integer.valueOf(String.valueOf(new Date().getTime() / 30000)));
	}

}
