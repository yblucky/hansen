package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.constant.OrderStatus;
import com.constant.UserStatusType;
import com.model.*;
import com.service.*;
import com.utils.toolutils.RedisLock;
import com.utils.toolutils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.paradoxs.bitcoin.client.BitcoinClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/app")
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CardGradeService cardGradeService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private UserSignService userSignService;
    @Autowired
    private UserDepartmentService userDepartmentService;


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

    /**
     * 测试业绩升级
     */
    @ResponseBody
    @RequestMapping(value = "/up", method = RequestMethod.GET)
    public JsonResult testGrade(HttpServletRequest request, String userId) throws Exception {
        Grade grade = gradeService.getUserGrade(userId);
        if (grade != null) {
            userService.updateUserGradeByUserId(userId, grade.getGrade());
        }
        if (grade == null) {
            return new JsonResult(ResultCode.ERROR);
        }
        return new JsonResult(grade);
    }




    /**
     * 极差奖
     */
    @ResponseBody
    @RequestMapping(value = "/differ", method = RequestMethod.GET)
    public JsonResult differ(HttpServletRequest request, String userId,String orderNo) throws Exception {
        TradeOrder con = new TradeOrder();
        con.setOrderNo(orderNo);
        TradeOrder order =  tradeOrderService.readOne(con);
        if (ToolUtil.isEmpty(orderNo)){
            return new JsonResult(ResultCode.ERROR.getCode(),"订单为空");
        }
        if (order==null){
            return new JsonResult(ResultCode.ERROR.getCode(),"找不到订单");
        }
        if (order!=null){
            userService.differnceBonus(userId,order);
        }
        return new JsonResult(ResultCode.SUCCESS);
    }

    /**
     * 直推奖
     */
    @ResponseBody
    @RequestMapping(value = "/push", method = RequestMethod.GET)
    public JsonResult push(HttpServletRequest request, String userId, String orderNo) throws Exception {
        TradeOrder con = new TradeOrder();
        con.setOrderNo(orderNo);
        TradeOrder order =  tradeOrderService.readOne(con);
        if (ToolUtil.isEmpty(orderNo)){
            return new JsonResult(ResultCode.ERROR.getCode(),"订单为空");
        }
        if (order==null){
            return new JsonResult(ResultCode.ERROR.getCode(),"找不到订单");
        }
        if (order!=null){
            userService.pushBonus(userId,order);
        }
        return new JsonResult(ResultCode.SUCCESS);
    }


    /**
     * 是否能挂载节点人
     */
    @ResponseBody
    @RequestMapping(value = "/concat", method = RequestMethod.GET)
    public JsonResult concat(HttpServletRequest request, String userId1, String userId2) throws Exception {
        if (ToolUtil.isEmpty(userId1) || ToolUtil.isEmpty(userId2)) {
            return new JsonResult("邀请人，节点人id都不能为空");
        }
        Boolean flag = userService.isVrticalLine(userId1, userId2);
        return new JsonResult(flag);
    }

    /**
     * 是否能挂载节点人
     */
    @ResponseBody
    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public JsonResult clear(HttpServletRequest request) throws Exception {
        Boolean flag = userService.regularlyClearUnActiveUser();
        return new JsonResult(flag);
    }


    /**
     * 更新部门业绩，最后一层要加上用户自己的部门业绩
     */
    @ResponseBody
    @RequestMapping(value = "/performance", method = RequestMethod.GET)
    public JsonResult concat() throws Exception {
        List<UserDepartment> list = userDepartmentService.getAllUserDepartment();
        if (list == null) {
            return new JsonResult("空");
        }
        try {
            for (UserDepartment userDepartment : list) {
                User user = userService.readById(userDepartment.getId());
                if (user == null) {
                    continue;
                }
                if (user.getCardGrade() == null) {
                    continue;
                }
                if (UserStatusType.ACTIVATESUCCESSED.getCode().intValue() != user.getStatus()) {
                    continue;
                }
                CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
                if (cardGrade == null || cardGrade.getInsuranceAmt() == null) {
                    continue;
                }
                Double perfomance = cardGrade.getInsuranceAmt();
                userDepartmentService.updatePerformance(user.getId(), perfomance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonResult("error");
        }
        return new JsonResult("ok");
    }

    @ResponseBody
    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public JsonResult coinOut(HttpServletRequest request, String id, String host, Integer port) {
        try {
            BitcoinClient client = new BitcoinClient(host, "user", "password", port);
            return new JsonResult(WalletUtil.getBalance(client));
        } catch (Exception e) {
        }
        return null;
    }

}
