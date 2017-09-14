package com.controller;

import com.base.page.PageResult;
import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.constant.CardLevelType;
import com.constant.OrderStatus;
import com.constant.OrderType;
import com.model.SysUser;
import com.model.TradeOrder;
import com.model.User;
import com.service.TradeOrderService;
import com.service.UserService;
import com.sysservice.ManageUserService;
import com.utils.classutils.MyBeanUtils;
import com.utils.toolutils.ToolUtil;
import com.vo.SysUserVo;
import com.vo.TradeOrderVo;
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
     * 获取保单列表：首次保单  覆盖升级  补差升级
     *
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/orderlist", method = RequestMethod.GET)
    public RespBody orderList(HttpServletRequest request, HttpServletResponse response, Paging page, Integer uid, String phone, String tradeOrderType) throws Exception {
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
        Boolean isHasData = true;
        String sendUserId = null;
        User con = new User();

        User conUser = null;
        if (uid != null) {
            con.setUid(uid);
            conUser = userService.readOne(con);
            if (conUser == null) {
                isHasData = false;
            } else {
                sendUserId = conUser.getId();
            }
        }
        if (ToolUtil.isNotEmpty(phone)) {
            con.setPhone(phone);
            conUser = userService.readOne(con);
            if (conUser == null) {
                isHasData = false;
            } else {
                sendUserId = conUser.getId();
            }
        }
        if (page.getPageNumber() == 0) {
            page.setPageNumber(0);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        List<TradeOrder> list = null;
        List<TradeOrderVo> rsList = null;
        List<Integer> sourceList = new ArrayList<>();
        sourceList.add(OrderType.INSURANCE.getCode());
        sourceList.add(OrderType.INSURANCE_ORIGIN.getCode());
        sourceList.add(OrderType.INSURANCE_COVER.getCode());
        Integer count = tradeOrderService.readInsuranceCountByOrderType(sendUserId, sourceList);
        if (count != null && count > 0) {
            list = tradeOrderService.readInsuranceListByOrderType(sendUserId, sourceList, page.getStartRow(), page.getPageSize());
            rsList = new ArrayList<>();
            TradeOrderVo vo = null;
            User userPo = null;
            for (TradeOrder order : list) {
                vo = MyBeanUtils.copyProperties(order, TradeOrderVo.class);
                userPo = userService.readById(order.getSendUserId());
                if (OrderType.INSURANCE.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("首次报单");
                    vo.setRewardType(OrderType.INSURANCE.getMsg());
                } else if (OrderType.INSURANCE_ORIGIN.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("补差升级");
                    vo.setRewardType(OrderType.INSURANCE_ORIGIN.getMsg());
                } else if (OrderType.INSURANCE_COVER.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("覆盖升级");
                    vo.setRewardType(OrderType.INSURANCE_COVER.getMsg());
                }
                if (userPo != null) {
                    vo.setNickName(userPo.getNickName());
                    vo.setNickName(userPo.getNickName());
                    vo.setUid(userPo.getUid());
                    vo.setPhone(userPo.getPhone());
                }
                vo.setCardGradeName(CardLevelType.getName(order.getCardGrade()));
                vo.setStatusName(OrderStatus.getName(order.getStatus()));
                rsList.add(vo);
            }
        } else {
            rsList = Collections.emptyList();
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, rsList);
        return respBody;
    }


    /**
     * 奖励列表
     *
     * @param request
     * @param page
     * @param uid
     * @param phone
     * @param tradeOrderType
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/rewardList", method = RequestMethod.GET)
    public RespBody rewardList(HttpServletRequest request, Paging page, Integer uid, String phone, String tradeOrderType) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        String receviceUserId = null;
        User con = new User();

        User conUser = null;
        if (uid != null) {
            con.setUid(uid);
            conUser = userService.readOne(con);
            if (conUser != null) {
                receviceUserId = conUser.getId();
            }
        }
        if (ToolUtil.isNotEmpty(phone)) {
            con.setPhone(phone);
            conUser = userService.readOne(con);
            if (conUser != null) {
                receviceUserId = conUser.getId();
            }
        }
        if (page.getPageNumber() == 0) {
            page.setPageNumber(0);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        List<Integer> sourceList = new ArrayList<>();
        if (ToolUtil.isNotEmpty(tradeOrderType)) {
            String[] orderTypes = tradeOrderType.split(",");
            List<Integer> orderTypeList = new ArrayList<>();
            if (orderTypes.length > 0) {
                for (int i = 0; i < orderTypes.length; i++) {
                    sourceList.add(Integer.valueOf(orderTypes[i]));
                }
            }
        } else {
            sourceList.add(OrderType.PUSH.getCode());
            sourceList.add(OrderType.MANAGE.getCode());
            sourceList.add(OrderType.DIFFERENT.getCode());
            sourceList.add(OrderType.SAME.getCode());
            sourceList.add(OrderType.RELASE.getCode());
        }
        List<TradeOrderVo> voList = new ArrayList<>();
        PageResult<TradeOrderVo> pageResult = new PageResult<>();
        pageResult.setRows(voList);
        BeanUtils.copyProperties(page, pageResult);
        Integer count = tradeOrderService.readRewardCountByOrderType(receviceUserId, sourceList);
        if (count != null && count > 0) {
            List<TradeOrder> list = tradeOrderService.readRewardListByOrderType(receviceUserId, sourceList, page.getStartRow(), page.getPageSize());
            for (TradeOrder order : list) {
                TradeOrderVo vo = new TradeOrderVo();
                BeanUtils.copyProperties(order, vo);
                if (OrderType.PUSH.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    if (u != null) {
                        order.setRemark("会员" + u.getUid() + "注册，获得" + order.getConfirmAmt() + "奖励");
                    }
                    vo.setRewardType(OrderType.PUSH.getMsg());
                } else if (OrderType.PUSH.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "注册，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.PUSH.getMsg());
                } else if (OrderType.MANAGE.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "管理奖励，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.MANAGE.getMsg());
                } else if (OrderType.DIFFERENT.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "级差奖励，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.DIFFERENT.getMsg());
                } else if (OrderType.SAME.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "平级奖励，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.SAME.getMsg());
                } else if (OrderType.RELASE.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "周期释放，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.RELASE.getMsg());
                }
                User receUser = userService.readById(order.getReceviceUserId());
                if (receUser != null) {
                    vo.setNickName(receUser.getNickName());
                    vo.setUid(receUser.getUid());
                    vo.setPhone(receUser.getPhone());
                }
                vo.setCardGradeName(CardLevelType.getName(order.getCardGrade()));
                vo.setStatusName(OrderStatus.getName(order.getStatus()));
                voList.add(vo);
            }
        } else {
            count = 0;
        }
        pageResult.setTotalSize(count);
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, voList);
        return respBody;
    }

}
