package com.controller;


import com.Token;
import com.base.TokenUtil;
import com.base.page.*;
import com.constant.*;
import com.model.SysUser;
import com.service.*;
import com.sysservice.ManageUserService;
import com.vo.SysUserVo;
import com.vo.UserVo;
import com.vo.CoinVerfyVo;
import com.model.User;
import com.model.UserDetail;
import com.model.WalletOrder;
import com.utils.toolutils.ToolUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.paradoxs.bitcoin.client.BitcoinClient;

import javax.annotation.Resource;
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
    @Resource
    private ManageUserService manageUserService;//用户业务层

    /**
     * @param request
     * @param currencyType 参数参照枚举  2  5  8   WalletOrderType    WalletOrderStatus
     * @param page
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public RespBody list(HttpServletRequest request, Integer currencyType, Paging page, Integer status) throws Exception  {
        // 创建返回对象
        RespBody respBody = new RespBody();
        String token = request.getHeader("token");
        SysUserVo userVo = manageUserService.SysUserVo(token);
        SysUser user = manageUserService.readById(userVo.getId());
        if (user == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
            return respBody;
        }

        WalletOrder condition = new WalletOrder();
        if (currencyType != null) {
            condition.setOrderType(currencyType);
        }
        if (status != null) {
            condition.setStatus(status);
        }
        Integer count = walletOrderService.readCount(condition);
        List<WalletOrder> orders = null;
        if (count != null && count > 0) {
            orders = walletOrderService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
        } else {
            count = 0;
            orders = Collections.emptyList();
        }
        //返回
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(),"成功",page,orders);
        return respBody;
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
    @RequestMapping(value = "/coinverfy",method = RequestMethod.POST)
    public RespBody coinVerfy(HttpServletRequest request,@RequestBody CoinVerfyVo vo) {
        // 创建返回对象
        RespBody respBody = new RespBody();

        try {

            String token = request.getHeader("token");
            SysUserVo userVo = manageUserService.SysUserVo(token);
            SysUser sysUser = manageUserService.readById(userVo.getId());
            if (sysUser == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
                return respBody;
            }
            if (!"10".equals(sysUser.getState())) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "管理员账号被禁用");
                return respBody;
            }
            if (ToolUtil.isEmpty(vo.getOrderId())) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "待审订单编号不能为空");
                return respBody;
            }
            if (vo.getStatus() == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "待审订单状态不能为空");
                return respBody;
            }

            WalletOrder model = new WalletOrder();
            model.setOrderNo(vo.getOrderId());
            WalletOrder order = walletOrderService.readOne(model);
            if (order == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "订单不存在");
                return respBody;
            }
            if (WalletOrderStatus.PENDING.getCode() != order.getStatus()) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "订单不是待审核状态");
                return respBody;
            }
            WalletOrder updateModel = new WalletOrder();
            if (vo.getStatus() == WalletOrderStatus.DENIED.getCode()) {
                updateModel.setId(vo.getOrderId());
                updateModel.setStatus(WalletOrderStatus.DENIED.getCode());
                updateModel.setRemark("审核不通过，审核人：" + sysUser.getUserName());
                walletOrderService.updateById(order.getId(), updateModel);
            }
            User coinUser = userService.readById(order.getSendUserId());
            UserDetail coinUserDetail = userDetailService.readById(order.getSendUserId());
            Boolean isDeleteFlag = false;
            if (coinUser == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "提币用户不存在");
                return respBody;
            }
            if (UserStatusType.ACTIVATESUCCESSED.getCode() != coinUser.getStatus()) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "提币账号未激活");
                return respBody;
            }
            if (coinUserDetail == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "提币用户信息不存在");
                return respBody;
            }
            CurrencyType currencyType = WalletOrderType.getCoinTypeFromWalletOrderTypeCode(order.getOrderType());
            String address = "";
            if (currencyType.getCode() == CurrencyType.TRADE.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutTradeAddress())) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加交易币提币地址");
                    return respBody;
                }
                address = coinUserDetail.getOutTradeAddress();
            } else if (currencyType.getCode() == CurrencyType.PAY.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutPayAddress())) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加支付币提币地址");
                    return respBody;
                }
                address = coinUserDetail.getOutPayAddress();
            } else if (currencyType.getCode() == CurrencyType.EQUITY.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutEquityAddress())) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加股权币提币地址");
                    return respBody;
                }
                address = coinUserDetail.getOutEquityAddress();
            }
            if (ToolUtil.isEmpty(address)) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加提币地址");
                return respBody;
            }

            BitcoinClient client = WalletUtil.getBitCoinClient(currencyType);
            String txtId = WalletUtil.sendFrom(client, coinUser.getUid().toString(), address, new BigDecimal(order.getAmount().toString()), "用户" + coinUser.getUid() + "提币", "用户" + address + "收币");
            if (ToolUtil.isNotEmpty(txtId)) {
                vo.setStatus(WalletOrderStatus.CONFIRMING.getCode());
                updateModel.setRemark("提币审核通过，审核人：" + sysUser.getUserName());
                transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID, address, WalletOrderType.fromCode(order.getOrderType()), WalletOrderStatus.CONFIRMING, txtId, order.getOrderNo(), order.getAmount());
            }
            updateModel.setId(vo.getOrderId());
            updateModel.setStatus(vo.getStatus());
            walletOrderService.updateById(order.getId(), updateModel);
        } catch (Exception e) {
            e.printStackTrace();
            respBody.add(RespCodeEnum.ERROR.getCode(), "提现审核异常");
            return respBody;
        }
        respBody.add(RespCodeEnum.ERROR.getCode(), "审核成功");
        return respBody;
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
