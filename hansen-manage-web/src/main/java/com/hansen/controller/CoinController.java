package com.hansen.controller;


import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.*;
import com.hansen.service.UserDetailService;
import com.hansen.service.UserService;
import com.hansen.service.WalletOrderService;
import com.hansen.service.WalletTransactionService;
import com.hansen.vo.CoinVerfyVo;
import com.hansen.vo.UserVo;
import com.model.User;
import com.model.UserDetail;
import com.model.WalletOrder;
import com.utils.WalletUtil;
import com.utils.toolutils.ToolUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.paradoxs.bitcoin.client.BitcoinClient;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created on 2017-08-21;
 */
@Controller
@RequestMapping("/coin")
public class CoinController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(CoinController.class);

    @Autowired
    private WalletOrderService walletOrderService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private UserService userService;
    @Autowired
    private WalletTransactionService transactionService;

    /**
     * @param request
     * @param currencyType 参数参照枚举  2  5  8   WalletOrderType    WalletOrderStatus
     * @param page
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/list")
    public JsonResult list(HttpServletRequest request, Integer currencyType, Page page, Integer status) {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.MANGE_ERROR.getCode(), "登录用户不存在");
        }
        WalletOrder condition = new WalletOrder();
        if (currencyType != null) {
            condition.setOrderType(currencyType);
        }
        if (status != null) {
            condition.setStatus(status);
        }
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10);
        }
        Integer count = walletOrderService.readCount(condition);
        List<WalletOrder> orders = null;
        if (count != null && count > 0) {
            orders = walletOrderService.readList(condition, page.getPageNo(), page.getPageSize(), count);
        } else {
            count = 0;
            orders = Collections.emptyList();
        }
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, orders);
        return success(pageResult);
    }

    /**
     * 2:交易币提币
     * 5:支付币提币
     * 8:股权币提币
     *
     * @param request
     * @param vo
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/coinverfy")
    public JsonResult coinVerfy(HttpServletRequest request, CoinVerfyVo vo) {
        JsonResult result = null;
        try {
            Token token = TokenUtil.getSessionUser(request);

            if (ToolUtil.isEmpty(vo.getOrderId())) {
                return fail("待审订单编号不能为空");
            }
            if (vo.getStatus() == null) {
                return fail("待审订单状态不能为空");
            }
            User user = userService.readById(token.getId());
            if (user == null) {
                return fail("登录用户不存在");
            }
            if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
                return fail("登录账号未激活");
            }
            WalletOrder order = walletOrderService.readById(token.getId());
            if (WalletOrderStatus.PENDING.getCode() != order.getStatus()) {
                return fail("订单不是待审核状态");
            }
            WalletOrder updateModel = new WalletOrder();
            if (vo.getStatus() == WalletOrderStatus.DENIED.getCode()) {
                updateModel.setId(vo.getOrderId());
                updateModel.setStatus(WalletOrderStatus.DENIED.getCode());
                updateModel.setRemark("审核不通过，审核人：" + user.getUid() + " " + user.getNickName());
                walletOrderService.updateById(order.getId(), updateModel);
            }
            User coinUser = userService.readById(order.getSendUserId());
            UserDetail coinUserDetail = userDetailService.readById(order.getSendUserId());
            Boolean isDeleteFlag = false;
            if (coinUser == null) {
                return fail("提币用户不存在");
            }
            if (UserStatusType.ACTIVATESUCCESSED.getCode() != coinUser.getStatus()) {
                return fail("提币账号未激活");
            }
            if (coinUserDetail == null) {
                return fail("提币用户不存在");
            }
            CurrencyType currencyType = WalletOrderType.getCoinTypeFromWalletOrderTypeCode(order.getOrderType());
            String address = "";
            if (currencyType.getCode() == CurrencyType.TRADE.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutTradeAddress())) {
                    return fail("用户未添加交易币提币地址");
                }
                address = coinUserDetail.getOutTradeAddress();
            } else if (currencyType.getCode() == CurrencyType.PAY.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutPayAddress())) {
                    return fail("用户未添加支付币提币地址");
                }
                address = coinUserDetail.getOutPayAddress();
            } else if (currencyType.getCode() == CurrencyType.EQUITY.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutEquityAddress())) {
                    return fail("用户未添加股权币提币地址");
                }
                address = coinUserDetail.getOutEquityAddress();
            }
            if (ToolUtil.isEmpty(address)) {
                return fail("用户未添加提币地址");
            }

            BitcoinClient client = WalletUtil.getBitCoinClient(currencyType);
            String txtId = WalletUtil.sendFrom(client, coinUser.getUid().toString(), address, new BigDecimal(order.getAmount().toString()), "用户" + user.getUid() + "提币", "用户" + address + "收币");
            if (ToolUtil.isNotEmpty(txtId)) {
                vo.setStatus(WalletOrderStatus.CONFIRMING.getCode());
                updateModel.setRemark("提币审核通过，审核人：" + user.getUid() + " " + user.getNickName());
                transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID, address, WalletOrderType.fromCode(order.getOrderType()), WalletOrderStatus.CONFIRMING, txtId, order.getOrderNo(), order.getAmount());
            }
            updateModel.setId(vo.getOrderId());
            updateModel.setStatus(vo.getStatus());
            walletOrderService.updateById(order.getId(), updateModel);
        } catch (Exception e) {
            e.printStackTrace();
            return fail("提现审核异常");
        }
        return success("审核成功");
    }

    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, User model) {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (user.getStatus() == StatusType.DEL.getCode()) {
            return fail(ResultCode.MANGE_ERROR.getCode(), "用户已删除");
        }
        model.setStatus(model.getStatus());
        model.setContactUserId(model.getContactUserId());
        model.setNickName(model.getNickName());
        model.setPhone(model.getPhone());
        userService.updateById(user.getId(), user);
        return success(ResultCode.SUCCESS);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(HttpServletRequest request, String userId) {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (null == user) {
            return fail("用户不存在");
        }
        userService.deleteById(userId);
        return success();
    }

    @ResponseBody
    @RequestMapping("/detail")
    public JsonResult detail(HttpServletRequest request, String userId) {
        UserVo userVo = null;
        try {
            Token token = TokenUtil.getSessionUser(request);
            User user = userService.readById(token.getId());
            if (user == null) {
                return fail("用户不存在");
            }
            userVo = new UserVo();
            BeanUtils.copyProperties(userVo, user);

            UserDetail userDetail = userDetailService.readById(userId);
            if (userDetail != null) {
                BeanUtils.copyProperties(userVo, userDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail("用户不存在");
        }
        return success(userVo);
    }
}
