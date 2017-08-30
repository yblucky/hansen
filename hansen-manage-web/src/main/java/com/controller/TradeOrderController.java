package com.controller;

import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.constant.CardLevelType;
import com.constant.OrderStatus;
import com.model.SysUser;
import com.model.Task;
import com.model.TradeOrder;
import com.model.User;
import com.service.TaskService;
import com.service.TradeOrderService;
import com.service.UserService;
import com.sysservice.ManageUserService;
import com.utils.classutils.MyBeanUtils;
import com.utils.toolutils.ToolUtil;
import com.vo.SysUserVo;
import com.vo.TradeOrderVo;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/tradeOrder")
public class TradeOrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TradeOrderController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TradeOrderService tradeOrderService;
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
    public RespBody getTask(HttpServletRequest request, HttpServletResponse response, Paging page) throws Exception {
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
        List<TradeOrder> list = null;
        List<TradeOrderVo> rsList = null;
        TradeOrder condition = new TradeOrder();
        condition.setSource(1);//保单类型
        Integer count = tradeOrderService.readCount(condition);
        if (count != null && count > 0) {
            list = tradeOrderService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
            rsList = new ArrayList<>();
            TradeOrderVo vo = null;
            User userPo = null;
            for(TradeOrder order :list){
                vo = MyBeanUtils.copyProperties(order,TradeOrderVo.class);
                userPo = userService.readById(order.getSendUserId());
                if(userPo!=null){
                    vo.setNickName(userPo.getNickName());
                }
                vo.setCardGradeName(CardLevelType.getName(order.getCardGrade()));
                vo.setStatusName(OrderStatus.getName(order.getStatus()));
                rsList.add(vo);
            }
        } else {
            rsList = Collections.emptyList();
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(),"成功",page,rsList);
        return respBody;
    }


}
