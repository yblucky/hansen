package com.controller;

import com.base.page.Page;
import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.constant.CodeType;
import com.constant.Constant;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.model.TransferCode;
import com.model.User;
import com.model.WalletOrder;
import com.service.TransferCodeService;
import com.service.UserService;
import com.service.WalletOrderService;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import com.vo.BackReChargeVo;
import com.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/charge")
public class ChargeController {
    private Logger logger = LoggerFactory.getLogger(ChargeController.class);
    @Resource
    private UserService userService;
    @Autowired
    private WalletOrderService walletOrderService;
    @Autowired
    private TransferCodeService transferCodeService;


    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespBody list(HttpServletRequest request, UserVo vo, Paging page) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        User user = null;
        List<WalletOrder> list = new ArrayList<WalletOrder>();
        String userId = "";
        if (ToolUtil.isNotEmpty(vo.getUid()) && vo.getUid() != 0) {
            User condition = new User();
            condition.setUid(vo.getUid());
            user = userService.readOne(condition);
        }
        if (page.getPageNumber() == 0) {
            page.setPageNumber(1);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        Page page1 = new Page();
        page1.setPageNo(page.getPageNumber());
        page1.setPageSize(page.getPageSize());
        if (user != null) {
            userId = user.getId();
        }
        List<Integer> orderTypeList = new ArrayList<>();
        orderTypeList.add(WalletOrderType.TRADE_COIN_BACK_CHARGE.getCode());
        orderTypeList.add(WalletOrderType.PAY_COIN_BACK_CHARGE.getCode());
        orderTypeList.add(WalletOrderType.EQUITY_COIN_BACK_CHARGE.getCode());

        Integer c = walletOrderService.readOrderCount(userId, orderTypeList);
        if (c != null && c > 0) {
            list = walletOrderService.readOrderList(userId, orderTypeList, page1);
            for (WalletOrder order : list) {
                User u = userService.readById(order.getReceviceUserId());
                order.setRemark(u.getNickName());
                order.setReceviceUserId(u.getUid() + "");
            }
        } else {
            c = 0;
        }
        page.setTotalCount(c);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, list);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, list);
        return respBody;
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBody add(HttpServletRequest request, @RequestBody BackReChargeVo vo) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        if (vo.getUid() == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "uid不能为空");
        }
        User condition = new User();
        condition.setUid(vo.getUid());
        User chargeTargerUser = userService.readOne(condition);
        if (chargeTargerUser == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "充值用户不存在");
        }
        walletOrderService.chargeService(vo, chargeTargerUser);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功");
        return respBody;
    }

    private void chargeService(BackReChargeVo vo, User chargeTargerUser) {
        if (vo.getActiveCodeNo() != null && vo.getActiveCodeNo() > 0) {
            TransferCode transferCode = new TransferCode();
            transferCode.setSendUserId(Constant.SYSTEM_USER_ID);
            transferCode.setReceviceUserId(chargeTargerUser.getId());
            transferCode.setTransferNo(vo.getActiveCodeNo().intValue());
            transferCode.setType(CodeType.ACTIVATECODE.getCode());
            transferCode.setReceviceUserNick(chargeTargerUser.getNickName());
            transferCode.setSendUserNick(Constant.SYSTEM_USER_NICKNAME);
            transferCode.setRemark("系统赠送激活码");
            transferCodeService.create(transferCode);
            userService.updateUserActiveCode(chargeTargerUser.getId(), vo.getActiveCodeNo().intValue());
        }
        if (vo.getRegisterCodeNo() != null && vo.getRegisterCodeNo() > 0) {
            TransferCode transferCode = new TransferCode();
            transferCode.setSendUserId(Constant.SYSTEM_USER_ID);
            transferCode.setReceviceUserId(chargeTargerUser.getId());
            transferCode.setTransferNo(vo.getRegisterCodeNo().intValue());
            transferCode.setType(CodeType.REGISTERCODE.getCode());
            transferCode.setReceviceUserNick(chargeTargerUser.getNickName());
            transferCode.setSendUserNick(Constant.SYSTEM_USER_NICKNAME);
            transferCode.setRemark("系统赠送注册码");
            transferCodeService.create(transferCode);
            userService.updateUserRegisterCode(chargeTargerUser.getId(), vo.getRegisterCodeNo().intValue());
        }

        WalletOrder addModel = new WalletOrder();
        addModel.setReceviceUserId(chargeTargerUser.getId());
        addModel.setSendUserId(Constant.SYSTEM_USER_ID);
        addModel.setStatus(WalletOrderStatus.SUCCESS.getCode());
        if (vo.getPayAmt() != null && vo.getPayAmt() > 0) {
            addModel.setId(ToolUtil.getUUID());
            addModel.setOrderNo(OrderNoUtil.get());
            addModel.setAmount(-vo.getPayAmt());
            addModel.setConfirmAmt(-vo.getPayAmt());
            addModel.setOrderType(WalletOrderType.PAY_COIN_BACK_CHARGE.getCode());
            addModel.setRemark("管理员后台充值购物币");
            walletOrderService.create(addModel);
            userService.updatePayAmtByUserId(chargeTargerUser.getId(), vo.getPayAmt());
        }
        if (vo.getTradeAmt() != null && vo.getTradeAmt() > 0) {
            addModel.setId(ToolUtil.getUUID());
            addModel.setOrderNo(OrderNoUtil.get());
            addModel.setAmount(-vo.getTradeAmt());
            addModel.setConfirmAmt(-vo.getTradeAmt());
            addModel.setOrderType(WalletOrderType.TRADE_COIN_BACK_CHARGE.getCode());
            addModel.setRemark("管理员后台充值交易币");
            walletOrderService.create(addModel);
            userService.updateTradeAmtByUserId(chargeTargerUser.getId(), vo.getTradeAmt());
        }
        if (vo.getEquityAmt() != null && vo.getEquityAmt() > 0) {
            addModel.setId(ToolUtil.getUUID());
            addModel.setOrderNo(OrderNoUtil.get());
            addModel.setAmount(-vo.getEquityAmt());
            addModel.setConfirmAmt(-vo.getEquityAmt());
            addModel.setOrderType(WalletOrderType.EQUITY_COIN_BACK_CHARGE.getCode());
            addModel.setRemark("管理员后台充值股权币");
            walletOrderService.create(addModel);
            userService.updateEquityAmtByUserId(chargeTargerUser.getId(), vo.getEquityAmt());
        }
    }
}
