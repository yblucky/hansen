package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.CurrencyType;
import com.constant.UserStatusType;
import com.constant.WalletOrderType;
import com.model.User;
import com.model.WalletOrder;
import com.model.WalletTransaction;
import com.service.UserService;
import com.service.WalletOrderService;
import com.service.WalletTransactionService;
import com.service.WalletUtil;
import com.utils.codeutils.Md5Util;
import com.utils.toolutils.ToolUtil;
import com.vo.CoinInOutVo;
import com.vo.CoinTransferVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.TransactionInfo;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    @Resource
    private UserService userService;
    @Resource
    private WalletTransactionService transactionService;
    @Autowired
    private WalletOrderService walletOrderService;
    @Autowired
    private WalletTransactionService walletTransactionService;


    @ResponseBody
    @RequestMapping(value = "/cointransfer", method = RequestMethod.POST)
    public JsonResult coinTransfer(HttpServletRequest request, @RequestBody CoinTransferVo vo) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        if (ToolUtil.isEmpty(vo.getToUid())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请输入用户uid");
        }
        if (vo.getAmount() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请输入转账金额");
        }
        if (vo.getWalletOrderType() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "转账币种不能为空");
        }
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录账号未激活");
        }
        if (!user.getPayWord().equals(Md5Util.MD5Encode(vo.getPayPassWord(), user.getSalt()))) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付密码不正确");
        }
        if (CurrencyType.TRADE.getCode() == vo.getWalletOrderType()) {
            if (user.getTradeAmt() < vo.getAmount()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "交易币数量不足");
            }
        } else if (CurrencyType.PAY.getCode() == vo.getWalletOrderType()) {
            if (user.getPayAmt() < vo.getAmount()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "支付币数量不足");
            }
        } else if (CurrencyType.EQUITY.getCode() == vo.getWalletOrderType()) {
            if (user.getEquityAmt() < vo.getAmount()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "股权币数量不足");
            }
        }
        User conditon = new User();
        conditon.setUid(vo.getToUid());
        User toUser = userService.readOne(conditon);
        if (toUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "收款用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != toUser.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "收款账号未激活");
        }

        try {
            walletOrderService.coinTransfer(user.getId(), toUser.getId(), WalletOrderType.fromCode(vo.getWalletOrderType()), vo.getAmount());
            return new JsonResult(ResultCode.SUCCESS.getCode(), "转账成功");
        } catch (Exception e) {
            return new JsonResult(ResultCode.ERROR.getCode(), "转账失败");
        }
    }


    @ResponseBody
    @RequestMapping(value = "/coinout", method = RequestMethod.POST)
    public JsonResult coinOut(HttpServletRequest request, @RequestBody CoinInOutVo vo) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        if (ToolUtil.isEmpty(vo.getAddress())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请输入地址");
        }
        if (vo.getAmount() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请输入转账金额");
        }
        if (vo.getWalletOrderType() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "币种不能为空");
        }
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录账号未激活");
        }
        if (!user.getPayWord().equals(Md5Util.MD5Encode(vo.getPayPassWord(), user.getSalt()))) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付密码不正确");
        }
        if (WalletOrderType.TRADE_COIN_DRWA.getCode() == vo.getWalletOrderType()) {
            if (user.getTradeAmt() < vo.getAmount()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "交易币数量不足");
            }
        } else if (WalletOrderType.PAY_COIN_DRWA.getCode() == vo.getWalletOrderType()) {
            if (user.getPayAmt() < vo.getAmount()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "支付币数量不足");
            }
        } else if (WalletOrderType.EQUITY_COIN_DRWA.getCode() == vo.getWalletOrderType()) {
            if (user.getEquityAmt() < vo.getAmount()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "股权币数量不足");
            }
        }

        try {
            walletOrderService.coinOut(user.getId(), "", vo.getAddress(), WalletOrderType.fromCode(vo.getWalletOrderType()), vo.getAmount());
            return new JsonResult(ResultCode.SUCCESS.getCode(), "提币申请成功，等待审核");
        } catch (Exception e) {
            return new JsonResult(ResultCode.ERROR.getCode(), "提币申请失败");
        }
    }

    /**
     * 充币提币记录
     *
     * @param request
     * @param page
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/coinoutterlist", method = RequestMethod.GET)
    public JsonResult outterList(HttpServletRequest request, Page page, String orderType) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(ResultCode.NO_LOGIN);
        }
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "找不到用户");
        }
        try {
            walletTransactionService.listTransactionsByTag(user.getUid().toString(), CurrencyType.EQUITY.getCode());
//            walletTransactionService.listTransactionsByTag(user.getUid().toString(), CurrencyType.PAY.getCode());
        } catch (Exception e) {
            System.out.println("查询虚拟币充值记录失败。。。。。。。。。。。。。。。。。。。。。。。");
            e.printStackTrace();
        }
        PageResult<WalletTransaction> pageResult = new PageResult<>();
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10);
        }
        if (ToolUtil.isEmpty(orderType)) {
            pageResult.setRows(Collections.emptyList());
            return new JsonResult(pageResult);
        }
        String[] orderTypes = orderType.split(",");
        List<Integer> orderTypeList = new ArrayList<>();
        if (orderTypes.length > 0) {
            for (int i = 0; i < orderTypes.length; i++) {
                orderTypeList.add(Integer.valueOf(orderTypes[i]));
            }
        }
        if (ToolUtil.isEmpty(orderType)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "查询类型不能为空");
        }

        WalletTransaction condition = new WalletTransaction();
        condition.setUserId(user.getId());
        condition.setOrderType(orderTypeList.get(0));
        Integer count = transactionService.readCount(condition);
        List<WalletTransaction> transactionList = new ArrayList<>();
        if (count != null && count > 0) {
            transactionList = transactionService.readList(condition, page.getPageNo(), page.getPageSize(), count);
            for (WalletTransaction transaction : transactionList) {
                transaction.setMessage(WalletUtil.checkTransactionStatus(transaction.getConfirmations()).getMessage());
            }
            pageResult.setRows(transactionList);
        }
        BeanUtils.copyProperties(pageResult, page);
        return new JsonResult(pageResult);
    }


    /**
     * 内部币种转账交易
     *
     * @param request
     * @param page
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/coininnerlist", method = RequestMethod.GET)
    public JsonResult innerList(HttpServletRequest request, Page page, String orderType) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        PageResult<WalletOrder> pageResult = new PageResult<>();
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10);
        }
        if (ToolUtil.isEmpty(orderType)) {
            pageResult.setRows(Collections.emptyList());
            return new JsonResult(pageResult);
        }
        String[] orderTypes = orderType.split(",");
        List<Integer> orderTypeList = new ArrayList<>();
        if (orderTypes.length > 0) {
            for (int i = 0; i < orderTypes.length; i++) {
                orderTypeList.add(Integer.valueOf(orderTypes[i]));
            }
        }
        if (ToolUtil.isEmpty(orderType)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "查询类型不能为空");
        }
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录账号未激活");
        }

        WalletOrder condition = new WalletOrder();
        condition.setOrderType(orderTypeList.get(0));
        Integer count = walletOrderService.readCount(condition);
        try {
            if (count != null && count > 0) {
                List<WalletOrder> orderList = new ArrayList<>();
                pageResult = walletOrderService.readTransferList(user.getId(), orderTypeList, page);
                BeanUtils.copyProperties(pageResult, page);
            } else {
                pageResult.setRows(Collections.emptyList());
            }
            return new JsonResult(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult(ResultCode.ERROR.getCode(), "获取转币记录失败");
    }

    @ResponseBody
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public JsonResult coinOut(HttpServletRequest request, String id, String host, Integer port) {
        try {
            BitcoinClient client = new BitcoinClient(host, "user", "password", port);
            return new JsonResult(WalletUtil.getBalance(client));
        } catch (Exception e) {
        }
        return null;
    }

    @ResponseBody
    @RequestMapping(value = "/createAddress", method = RequestMethod.GET)
    public JsonResult getAddress(HttpServletRequest request, String uid) {
        try {
            Map<String, String> map = new HashMap<>();
            String inpayAddress = WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.PAY.getCode()), uid);
            String inequityAddress = WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.EQUITY.getCode()), uid);
            String intradeAddress = WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.TRADE.getCode()), uid);
            map.put("inpayAddress", inpayAddress);
            map.put("inequityAddress", inequityAddress);
            map.put("intradeAddress", intradeAddress);
            return new JsonResult(map);
        } catch (Exception e) {
        }
        return new JsonResult(ResultCode.ERROR);
    }

    @ResponseBody
    @RequestMapping(value = "/rechargelist", method = RequestMethod.GET)
    public JsonResult rechargeList(HttpServletRequest request, String uid) {
        try {
            Map<String, Object> map = new HashMap<>();
            List<TransactionInfo> paylist = WalletUtil.listTransactions(WalletUtil.getBitCoinClient(CurrencyType.PAY), uid, 20);
            List<TransactionInfo> equitylist = WalletUtil.listTransactions(WalletUtil.getBitCoinClient(CurrencyType.EQUITY), uid, 20);
            List<TransactionInfo> trradelist = WalletUtil.listTransactions(WalletUtil.getBitCoinClient(CurrencyType.TRADE), uid, 20);
            map.put("paylist", paylist);
            map.put("equitylist", equitylist);
            map.put("trradelist", trradelist);
            return new JsonResult(map);
        } catch (Exception e) {

        }
        return new JsonResult(ResultCode.ERROR);
    }
}
