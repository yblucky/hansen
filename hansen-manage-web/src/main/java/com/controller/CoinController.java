package com.controller;


import com.Token;
import com.base.TokenUtil;
import com.base.page.*;
import com.constant.*;
import com.model.*;
import com.service.*;
import com.sysservice.ManageUserService;
import com.vo.SysUserVo;
import com.vo.UserVo;
import com.vo.CoinVerfyVo;
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
    @Resource
    private WalletTransactionService walletTransactionService;

    /**
     * @param request
     * @param currencyType 参数参照枚举  2  5  8   WalletOrderType    WalletOrderStatus  转账记录
     * @param page
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public RespBody list(HttpServletRequest request, Integer currencyType, Paging page, Integer status,String orderNo,String phone,Integer uid) throws Exception  {
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
        if (ToolUtil.isNotEmpty(orderNo)){
            condition.setOrderNo(orderNo);
        }
        if (ToolUtil.isNotEmpty(uid)){
            User u =userService.readUserByUid(uid);
            if (u==null){
                respBody.add(RespCodeEnum.ERROR.getCode(), "查不到记录");
            }else {
                condition.setReceviceUserId(u.getId());
            }
        }
        if (ToolUtil.isNotEmpty(phone)){
            User uc=new User();
            uc.setPhone(userVo.getMobile());
            User u =userService.readOne(uc);
            if (u==null){
                respBody.add(RespCodeEnum.ERROR.getCode(), "查不到记录");
            }else {
                condition.setReceviceUserId(u.getId());
            }
        }
        Integer count = walletOrderService.readCount(condition);
        List<WalletOrder> orders = null;
        if (count != null && count > 0) {
            orders = walletOrderService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
            for (WalletOrder order:orders){
               User receUser =  userService.readById(order.getReceviceUserId());
                if (receUser!=null){
                    order.setReceviceUserId(receUser.getUid()+"");
                }
                User sendUser =  userService.readById(order.getSendUserId());
                if (sendUser!=null){
                    order.setSendUserId(sendUser.getUid()+"");
                }
            }
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
     * @param request
     * @param currencyType 参数参照枚举  2  5  8   WalletOrderType    WalletOrderStatus  提币审核列表
     * @param page
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/coinOutList",method = RequestMethod.GET)
    public RespBody coinOutList(HttpServletRequest request, Integer currencyType, Paging page, Integer status,String uid,String orderNo) throws Exception  {
        // 创建返回对象
        RespBody respBody = new RespBody();
//        String token = request.getHeader("token");
//        SysUserVo userVo = manageUserService.SysUserVo(token);
//        SysUser user = manageUserService.readById(userVo.getId());
        SysUser user = manageUserService.readById("3709027e482f489dbf5a79ab17649bd6");
        if (user == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
            return respBody;
        }

        WalletTransaction condition = new WalletTransaction();
        if (currencyType != null) {
            condition.setOrderType(currencyType);
        }
        if (status != null) {
            condition.setStatus(status);
        }
        if (ToolUtil.isNotEmpty(uid)){
            condition.setUserId(uid);
        }
        if (ToolUtil.isNotEmpty(orderNo)){
            condition.setPrepayId(orderNo);
        }
        Integer count = walletTransactionService.readCount(condition);
        List<WalletTransaction> orders = null;
        if (count != null && count > 0) {
            orders = walletTransactionService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
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
     * 5:支付币提币 废弃
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

            WalletTransaction  model = new WalletTransaction();
            model.setPrepayId(vo.getOrderId());
            WalletTransaction order = walletTransactionService.readOne(model);
            if (order == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "订单不存在");
                return respBody;
            }
            if (WalletOrderStatus.PENDING.getCode() != order.getStatus()) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "订单不是待审核状态");
                return respBody;
            }
            User coinUser = userService.readUserByUid(Integer.valueOf(order.getUserId()));
            UserDetail coinUserDetail = userDetailService.readById(coinUser.getId());
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
            Integer coinTyep=2;
            if (WalletOrderType.TRADE_COIN_DRWA.getCode()==order.getOrderType()){
                coinTyep=CurrencyType.TRADE.getCode();
            }
            if (WalletOrderType.PAY_COIN_DRWA.getCode()==order.getOrderType()){
                coinTyep=CurrencyType.PAY.getCode();
            }
            if (WalletOrderType.EQUITY_COIN_DRWA.getCode()==order.getOrderType()){
                coinTyep=CurrencyType.EQUITY.getCode();
            }
            CurrencyType currencyType = WalletOrderType.getCoinTypeFromWalletOrderTypeCode(coinTyep);
            String address = "";
            if (currencyType.getCode() == CurrencyType.TRADE.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutTradeAddress())) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加交易币提币地址");
                    return respBody;
                }
                address = coinUserDetail.getOutTradeAddress();
            } else if (currencyType.getCode() == CurrencyType.PAY.getCode()) {
                if (ToolUtil.isEmpty(coinUserDetail.getOutPayAddress())) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加购物币提币地址");
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
            WalletTransaction updateModel = new WalletTransaction();
            if (vo.getStatus() == WalletOrderStatus.DENIED.getCode()) {
                updateModel.setId(vo.getOrderId());
                updateModel.setStatus(WalletOrderStatus.DENIED.getCode());
                updateModel.setRemark("审核不通过，审核人：" + sysUser.getUserName());
                updateModel.setMessage("审核不通过");
                walletTransactionService.updateById(order.getId(), updateModel);
                respBody.add(RespCodeEnum.ERROR.getCode(), "审核不通过成功");
                return respBody;
            }

            BitcoinClient client = WalletUtil.getBitCoinClient(currencyType);
//            String txtId = WalletUtil.sendToAddress(client, address, new BigDecimal(order.getAmount().toString()), "用户" + coinUser.getUid() + "提币", "用户" + address + "收币");
            String txtId=ToolUtil.getUUID();
            if (ToolUtil.isNotEmpty(txtId)) {

                vo.setStatus(WalletOrderStatus.CONFIRMING.getCode());
                updateModel.setRemark("审核通过，确认中，审核人：" + sysUser.getUserName());
                updateModel.setMessage(WalletOrderStatus.CONFIRMING.getMsg());
                updateModel.setId(order.getId());
                updateModel.setStatus(vo.getStatus());
                walletTransactionService.updateById(order.getId(), updateModel);
                respBody.add(RespCodeEnum.SUCCESS.getCode(),WalletOrderStatus.CONFIRMING.getMsg());
//                transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID, address, WalletOrderType.fromCode(order.getOrderType()), WalletOrderStatus.CONFIRMING, txtId, order.getPrepayId(), order.getAmount());
            }
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
    @RequestMapping(value = "/coincheckorder",method = RequestMethod.POST)
    public RespBody coinCheckorder(HttpServletRequest request,@RequestBody CoinVerfyVo vo) {
        // 创建返回对象
        RespBody respBody = new RespBody();

        try {

            String token = request.getHeader("token");
//            SysUserVo userVo = manageUserService.SysUserVo(token);
//            SysUser sysUser = manageUserService.readById(userVo.getId());
            SysUser sysUser = manageUserService.readById("3709027e482f489dbf5a79ab17649bd6");
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
            WalletTransaction updateModel = new WalletTransaction();
            if (vo.getStatus() == WalletOrderStatus.DENIED.getCode()) {
                updateModel.setId(vo.getOrderId());
                updateModel.setStatus(WalletOrderStatus.DENIED.getCode());
                updateModel.setRemark("审核不通过，审核人：" + sysUser.getUserName());
                walletTransactionService.updateById(order.getId(), updateModel);
                respBody.add(RespCodeEnum.ERROR.getCode(), "审核不通过成功");
                return respBody;
            }
            User coinUser = userService.readById(order.getSendUserId());
            UserDetail coinUserDetail = userDetailService.readById(order.getSendUserId());
            if (coinUser == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "提币用户不存在");
                return respBody;
            }
//            if (UserStatusType.ACTIVATESUCCESSED.getCode() != coinUser.getStatus()) {
//                respBody.add(RespCodeEnum.ERROR.getCode(), "提币账号未激活");
//                return respBody;
//            }
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
                    respBody.add(RespCodeEnum.ERROR.getCode(), "用户未添加购物币提币地址");
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
            Double  blance =  client.getBalance().doubleValue();
            if (blance<order.getConfirmAmt()){
                respBody.add(RespCodeEnum.ERROR.getCode(), "钱包币数量不足,请充值");
                return respBody;
            }
            walletTransactionService.checkCoinOut(sysUser,order.getId(),vo.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
            respBody.add(RespCodeEnum.ERROR.getCode(), "提现审核异常");
            return respBody;
        }
        respBody.add(RespCodeEnum.ERROR.getCode(), "审核成功");
        return respBody;
    }

    private void checkCoinOut(@RequestBody CoinVerfyVo vo, SysUser sysUser, WalletOrder order, WalletTransaction updateModel, User coinUser, CurrencyType currencyType, String address) throws Exception {
        BitcoinClient client = WalletUtil.getBitCoinClient(currencyType);
        String txtId = WalletUtil.sendToAddress(client, address, new BigDecimal(order.getAmount().toString()), "用户" + coinUser.getUid() + "提币", "用户" + address + "收币");
        if (ToolUtil.isNotEmpty(txtId)) {
            vo.setStatus(WalletOrderStatus.CONFIRMING.getCode());
            updateModel.setRemark("提币审核通过，审核人：" + sysUser.getUserName());
            transactionService.addWalletOrderTransaction(Constant.SYSTEM_USER_ID, address, WalletOrderType.fromCode(order.getOrderType()), WalletOrderStatus.CONFIRMING, txtId, order.getOrderNo(), order.getAmount());
        }
        updateModel.setId(vo.getOrderId());
        updateModel.setStatus(vo.getStatus());
        walletTransactionService.updateById(order.getId(), updateModel);
    }

}
