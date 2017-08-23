package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.SignType;
import com.constant.UserStatusType;
import com.service.UserService;
import com.service.UserSignService;
import com.model.User;
import com.model.UserSign;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/sign")
public class SignController {
    @Resource
    private UserService userService;
    @Autowired
    private UserSignService userSignService;


    @ResponseBody
    @RequestMapping(value = "/rewardsign", method = RequestMethod.POST)
    public JsonResult rewardSign(HttpServletRequest request) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }

        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录账号未激活");
        }

        UserSign conditon = new UserSign();
        conditon.setUserId(user.getId());
        conditon.setStatus(SignType.WAITING_SIGN.getCode());
        Integer count = userSignService.readCount(conditon);
        UserSign sign = null;
        Map<String, Double> map = new HashMap<>();
        if (count != null && count > 0) {
            sign = userSignService.readOne(conditon);
            try {
                Boolean flag = userSignService.sign(sign.getId());
                if (flag) {
                    user = userService.readById(user.getId());
                }
                map.put("payAmt", user.getPayAmt());
                map.put("tradeAmt", user.getTradeAmt());
                map.put("equityAmt", user.getEquityAmt());
            } catch (Exception e) {
                e.printStackTrace();
                return new JsonResult(ResultCode.ERROR.getCode(), "签到失败", map);
            }
        }
        return new JsonResult(ResultCode.SUCCESS.getCode(), "签到成功", map);
    }


    @ResponseBody
    @RequestMapping(value = "/signlist", method = RequestMethod.POST)
    public JsonResult inList(HttpServletRequest request, Page page) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
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
        List<UserSign> userSignList = new ArrayList<>();
        PageResult<UserSign> pageResult = new PageResult<>();
        BeanUtils.copyProperties(pageResult, page);
        UserSign condition = new UserSign();
        condition.setUserId(user.getId());
        Integer count = userSignService.readCount(condition);
        if (count != null && count > 0) {
            userSignList = userSignService.readList(condition, page.getPageNo(), page.getPageSize(), count);
            pageResult.setRows(userSignList);
        }
        return new JsonResult(pageResult);
    }
}
