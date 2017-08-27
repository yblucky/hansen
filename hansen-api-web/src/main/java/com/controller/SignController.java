package com.controller;

import com.Token;
import com.alibaba.fastjson.JSON;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.SignType;
import com.constant.UserStatusType;
import com.model.Parameter;
import com.service.ParamUtil;
import com.service.TradeOrderService;
import com.service.UserService;
import com.service.UserSignService;
import com.model.User;
import com.model.UserSign;
import com.utils.classutils.MyBeanUtils;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.ToolUtil;
import com.vo.UserSignVo;
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
    @Autowired
    private TradeOrderService tradeOrderService;


    @ResponseBody
    @RequestMapping(value = "/rewardsign", method = RequestMethod.GET)
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
        Map<String, Object> map = new HashMap<>();
        if (count != null && count > 0) {
            sign = userSignService.readOne(conditon);
            if (sign==null){
                map.put("isCanSign",false);
            }
            try {
                Boolean flag = userSignService.sign(sign.getId());
                if (flag) {
                    user = userService.readById(user.getId());
                    map.put("isCanSign",true);
                    map.put("amt", sign.getAmt());
                }else {
                    map.put("isCanSign",false);
                    map.put("amt",0);
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
    @RequestMapping(value = "/signlist", method = RequestMethod.GET)
    public JsonResult inList(HttpServletRequest request, Page page) throws Exception {
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
        List<UserSignVo> list = new ArrayList<>();
        PageResult<UserSignVo> pageResult = new PageResult<>();
        BeanUtils.copyProperties(pageResult, page);
        UserSign condition = new UserSign();
        condition.setUserId(user.getId());
        Integer count = userSignService.readCount(condition);
        if (count != null && count > 0) {
            userSignList = userSignService.readList(condition, page.getPageNo(), page.getPageSize(), count);
            UserSignVo vo = null;
            double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE),1d);
            double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE),1d);
            double equtyScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTEQUITYSCALE),1d);

            double rewardPayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE),1d);
            double rewardTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE),1d);
            double rewardEqutyScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE),1d);
            for (UserSign sign:userSignList){
                vo = MyBeanUtils.copyProperties(sign,UserSignVo.class);
                //三种币的收入
                Double payAmtRmb = CurrencyUtil.multiply(sign.getAmt(),rewardPayScale,2);
                Double tradeAmtRmb = CurrencyUtil.multiply(sign.getAmt(),rewardTradeScale,2);
                Double equityAmtRmb = CurrencyUtil.multiply(sign.getAmt(),rewardEqutyScale,2);

                Double payAmt=CurrencyUtil.multiply(payAmtRmb,payScale,2);
                Double tradeAmt=CurrencyUtil.multiply(tradeAmtRmb,tradeScale,2);
                Double equityAmt=CurrencyUtil.multiply(equityAmtRmb,equtyScale,2);

                vo.setPayAmt(payAmt);
                vo.setTradeAmt(tradeAmt);
                vo.setEquityAmt(equityAmt);
                vo.setPayAmtRmb(payAmtRmb);
                vo.setTradeAmtRmb(tradeAmtRmb);
                vo.setEquityAmtRmb(equityAmtRmb);
                list.add(vo);
            }
            pageResult.setRows(list);
        }
        return new JsonResult(pageResult);
    }
}
