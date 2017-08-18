package com.hansen.controller;

import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.common.Token;
import com.common.base.TokenUtil;
import com.common.constant.CurrencyType;
import com.common.constant.ResultCode;
import com.common.constant.UserStatusType;
import com.common.constant.WalletOrderType;
import com.common.utils.WalletUtil;
import com.common.utils.codeutils.Md5Util;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.service.TradeOrderService;
import com.hansen.service.UserService;
import com.hansen.service.WalletOrderService;
import com.hansen.service.WalletTransactionService;
import com.hansen.vo.CoinInOutVo;
import com.hansen.vo.CoinTransferVo;
import com.model.TradeOrder;
import com.model.User;
import com.model.WalletOrder;
import com.model.WalletTransaction;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/wallet")
public class WalletController {
    @Resource
    private UserService userService;
    @Resource
    private WalletTransactionService transactionService;
    @Autowired
    private WalletOrderService walletOrderService;


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




    @ResponseBody
    @RequestMapping(value = "/inoutlist", method = RequestMethod.POST)
    public JsonResult inList(HttpServletRequest request ,Integer currencyType,Page page) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        if (ToolUtil.isEmpty(currencyType)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "查询类型不能为空");
        }
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录账号未激活");
        }
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10);
        }
        WalletTransaction condition = new WalletTransaction();
        condition.setUserId(user.getId());
        condition.setCurrencyType(currencyType);
        Integer count = transactionService.readCount(condition);
        List<WalletTransaction> transactionList=new ArrayList<>();
        for (WalletTransaction transaction:transactionList){
            transaction.setMessage(WalletUtil.checkTransactionStatus(transaction.getConfirmations()).getMessage());
        }
        PageResult<WalletTransaction> pageResult =new PageResult<>();
        if (count != null && count > 0) {
            transactionList= transactionService.readList(condition, page.getPageNo(), page.getPageSize(),count );
            pageResult.setRows(transactionList);
        }
        BeanUtils.copyProperties(pageResult,page);
        return new JsonResult(pageResult);
    }
}
