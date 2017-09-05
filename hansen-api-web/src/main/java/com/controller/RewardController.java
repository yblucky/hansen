package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.OrderType;
import com.model.TradeOrder;
import com.model.User;
import com.service.TradeOrderService;
import com.service.UserService;
import com.utils.DateUtils.DateTimeUtil;
import com.utils.DateUtils.DateUtils;
import com.utils.toolutils.ToolUtil;
import com.vo.TradeOrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzwei on 2017/9/5.
 */
@Controller
@RequestMapping("/reward")
public class RewardController {
    @Autowired
    private UserService userService;
    @Autowired
    private TradeOrderService tradeOrderService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(HttpServletRequest request, Page page, String tradeOrderType) throws Exception {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
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
            sourceList.add(OrderType.EQUALITY.getCode());
            sourceList.add(OrderType.RELASE.getCode());
        }
        List<TradeOrderVo> voList = new ArrayList<>();
        PageResult<TradeOrderVo> pageResult = new PageResult<>();
        pageResult.setRows(voList);
        BeanUtils.copyProperties(page, pageResult);
        Integer count = tradeOrderService.readRewardCountByOrderType(user.getId(), sourceList);
        if (count != null && count > 0) {
            List<TradeOrder> list = tradeOrderService.readRewardListByOrderType(user.getId(), sourceList, page.getStartRow(), page.getPageSize());
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
                } else if (OrderType.EQUALITY.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "平级奖励，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.EQUALITY.getMsg());
                } else if (OrderType.RELASE.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "周期释放，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.RELASE.getMsg());
                }
                voList.add(vo);
            }
        } else {
            count = 0;
        }
        Double totalReward = tradeOrderService.sumReadRewardByOrderType(user.getId(), sourceList);
        pageResult.getExtend().put("totalReward", totalReward);
        pageResult.setTotalSize(count);
        return new JsonResult(pageResult);
    }
}
