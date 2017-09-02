package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.constant.OrderStatus;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;
import com.service.CardGradeService;
import com.service.TradeOrderService;
import com.service.UserService;
import com.utils.toolutils.RedisLock;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 订单处理
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private UserService userService;
    @Autowired
    private CardGradeService cardGradeService;

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public JsonResult handleOrder(String userId, Integer cardGrade) throws Exception {
        if (ToolUtil.isEmpty(userId)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "userId不能为空");
        }
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "卡等级不能为空");
        }
        User user = userService.readById(userId);
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "找不到用户");
        }
        CardGrade cardGradeModel = cardGradeService.getUserCardGrade(cardGrade);
        if (cardGradeModel == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "找不到卡等级");
        }
        TradeOrder order = tradeOrderService.createInsuranceTradeOrder(user, cardGradeModel);
        return new JsonResult(order);
    }


    @ResponseBody
    @RequestMapping(value = "/handle", method = RequestMethod.GET)
    public JsonResult handleOrder(HttpServletRequest request, HttpServletResponse response, String orderNo) throws Exception {

        Token token = TokenUtil.getSessionUser(request);
        if (ToolUtil.isEmpty(orderNo)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "保单号不能为空");
        }
        TradeOrder con = new TradeOrder();
        con.setOrderNo(orderNo);
        TradeOrder order = tradeOrderService.readOne(con);
        if (order == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "查无此单");
        }
        if (OrderStatus.PENDING.getCode() != order.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "保单不是待处理状态");
        }
        Boolean flag = RedisLock.redisLock(orderNo, orderNo, 15);
        Boolean handleFlag = false;
        if (flag) {
            handleFlag = tradeOrderService.handleInsuranceTradeOrder(orderNo);
        }
        if (handleFlag) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "保单处理成功");
        } else {
            return new JsonResult(ResultCode.ERROR.getCode(), "保单处理成功");
        }
    }
}
