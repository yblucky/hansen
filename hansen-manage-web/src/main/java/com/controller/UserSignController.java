package com.controller;

import com.base.page.PageResult;
import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.model.*;
import com.service.ParamUtil;
import com.service.TradeOrderService;
import com.service.UserService;
import com.service.UserSignService;
import com.sysservice.ManageUserService;
import com.utils.classutils.MyBeanUtils;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.ToolUtil;
import com.vo.SysUserVo;
import com.vo.UserSignVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/userSign")
public class UserSignController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserSignController.class);
    @Autowired
    private UserSignService userSignService;
    @Autowired
    private UserService userService;
    @Resource
    private ManageUserService manageUserService;//用户业务层


    /**
     * 获取任务列表
     *
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespBody getTask(HttpServletRequest request, HttpServletResponse response, Paging page,String mobile,Integer uid,Integer status) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        String token = request.getHeader("token");
        //读取用户信息
        SysUserVo userVo = manageUserService.SysUserVo(token);
        SysUser user = manageUserService.readById(userVo.getId());
        if (user == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
            return respBody;
        }
        UserSign condition = new UserSign();
        if (uid != null) {
            condition.setUid(uid);
        }
        if (status != null) {
            condition.setStatus(status);
        }
        if (ToolUtil.isNotEmpty(mobile)) {
            User condion = new User();
            condion.setPhone(mobile);
            User u=userService.readOne(condion);
            if (u==null){
                respBody.add(RespCodeEnum.ERROR.getCode(), "没有记录");
            }else{
                condition.setUid(u.getUid());
            }
        }
        List<UserSign> userSignList = new ArrayList<>();
        List<UserSignVo> list = new ArrayList<>();

        Integer count = userSignService.readCount(condition);
        if (count != null && count > 0) {
            userSignList = userSignService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
            UserSignVo vo = null;
            double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE),1d);
            double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE),1d);
            double equtyScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTEQUITYSCALE),1d);

            double rewardPayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE),1d);
            double rewardTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE),1d);
            double rewardEqutyScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE),1d);
            for (UserSign sign:userSignList){
                vo = MyBeanUtils.copyProperties(sign,UserSignVo.class);
                User userPo =userService.readById(sign.getUserId());
                if (vo.getUid()==null){
                    vo.setUid(userPo.getUid());
                }
                vo.setNickName(userPo.getNickName());
                //三种币的收入
                Double payAmtRmb = CurrencyUtil.multiply(sign.getAmt(),rewardPayScale,2);
                Double tradeAmtRmb = CurrencyUtil.multiply(sign.getAmt(),rewardTradeScale,2);
                Double equityAmtRmb = CurrencyUtil.multiply(sign.getAmt(),rewardEqutyScale,2);

                Double payAmt=CurrencyUtil.multiply(payAmtRmb,payScale,2);
                Double tradeAmt=CurrencyUtil.multiply(tradeAmtRmb,tradeScale,2);
                Double equityAmt=CurrencyUtil.multiply(equityAmtRmb,equtyScale,2);

                vo.setStatus(sign.getStatus());
                vo.setPayAmt(payAmt);
                vo.setTradeAmt(tradeAmt);
                vo.setEquityAmt(equityAmt);
                vo.setPayAmtRmb(payAmtRmb);
                vo.setTradeAmtRmb(tradeAmtRmb);
                vo.setEquityAmtRmb(equityAmtRmb);
                list.add(vo);
            }
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(),"成功",page,list);
        return respBody;
    }


}
