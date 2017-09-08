package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.*;
import com.constant.Constant;
import com.constant.OrderType;
import com.model.Parameter;
import com.model.TradeOrder;
import com.model.User;
import com.service.ParameterService;
import com.service.TradeOrderService;
import com.service.UserService;
import com.service.UserSignService;
import com.utils.DateUtils.DateUtils;
import com.utils.numberutils.CurrencyUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zzwei on 2017/9/5.
 */
@Controller
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private UserService userService;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private ParameterService parameterService;
    @Autowired
    private UserSignService userSignService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult inList(HttpServletRequest request, Page page, String tradeOrderType) throws Exception {
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
            sourceList.add(OrderType.SAME.getCode());
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
                } else if (OrderType.SAME.getCode() == order.getSource()) {
                    User u = userService.readById(order.getSendUserId());
                    vo.setRemark("来自会员" + u.getUid() + "平级奖励，获得" + order.getConfirmAmt() + "奖励");
                    vo.setRewardType(OrderType.SAME.getMsg());
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
        pageResult.setTotalSize(count);

        pageResult.getExtend().put("totalReward", totalReward);
        return new JsonResult(pageResult);
    }

    @ResponseBody
    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public RespBody statistic(HttpServletRequest request) throws Exception {

//        List<Integer> sourceList = new ArrayList<>();
//        List<Integer> orderTypeList = new ArrayList<>();
//        sourceList.add(OrderType.PUSH.getCode());
//        sourceList.add(OrderType.MANAGE.getCode());
//        sourceList.add(OrderType.DIFFERENT.getCode());
//        sourceList.add(OrderType.EQUALITY.getCode());
//        sourceList.add(OrderType.RELASE.getCode());
//        List<TradeOrderVo> voList = new ArrayList<>();
//        Integer count = tradeOrderService.readRewardCountByOrderType(null, sourceList);
//
        // 创建返回对象
        RespBody respBody = new RespBody();

        List<Integer> declarationOrderTypeList = new ArrayList<>();
        declarationOrderTypeList.add(OrderType.INSURANCE.getCode());
        declarationOrderTypeList.add(OrderType.INSURANCE_COVER.getCode());
        declarationOrderTypeList.add(OrderType.INSURANCE_ORIGIN.getCode());
        Double payScale = parameterService.getScale(Constant.RMB_CONVERT_PAY_SCALE);
        Double tradeScale = parameterService.getScale(Constant.RMB_CONVERT_TRADE_SCALE);
        Double rmbConvertEquityScale = parameterService.getScale(Parameter.RMBCONVERTEQUITYSCALE);
        Double rewardConvertEquityScale = parameterService.getScale(Parameter.REWARDCONVERTEQUITYSCALE);
        Double rewardConvertTradeScale = parameterService.getScale(Parameter.REWARDCONVERTTRADESCALE);
        Double rewardConvertPayScale = parameterService.getScale(Parameter.REWARDCONVERTPAYSCALE);
        String currentDate = DateUtils.currentDate();
        String startTime = currentDate + " 00:00:00";
        String endTime = currentDate + " 23:59:59";
        //今日报单总数
        Integer currentOrderCount = tradeOrderService.countTotalOrderAmtByTime(declarationOrderTypeList, startTime, endTime);
        //今日保单总金额
        Double currentOrderAmt = tradeOrderService.sumTotalOrderAmtByTime(declarationOrderTypeList, startTime, endTime);
        Double currentOrderPayAmt = tradeOrderService.sumTotalPayAmtByTime(declarationOrderTypeList, startTime, endTime);
        Double currentOrderTradeAmt = tradeOrderService.sumTotalTradeAmtByTime(declarationOrderTypeList, startTime, endTime);
        //换算成对应币的个数
        //今日报单购物币总个数
        Double currentOrderPayAmtCoin = CurrencyUtil.multiply(currentOrderPayAmt, payScale, 4);
        //今日报单交易币总个数
        Double currentOrderTradeAmtCoin = CurrencyUtil.multiply(currentOrderTradeAmt, tradeScale, 4);

        //历史
        //累计报单总数
        Integer orderCount = tradeOrderService.countTotalOrderAmtByTime(declarationOrderTypeList, null, null);
        //累计报单总金额
        Double orderAmt = tradeOrderService.sumTotalOrderAmtByTime(declarationOrderTypeList, null, null);
        Double orderPayAmt = tradeOrderService.sumTotalPayAmtByTime(declarationOrderTypeList, null, null);
        Double orderTradeAmt = tradeOrderService.sumTotalTradeAmtByTime(declarationOrderTypeList, null, null);

        //换算成对应币的个数
        //累计报单购物币总个数
        Double orderPayAmtCoin = CurrencyUtil.multiply(orderPayAmt, payScale, 4);
        //累计报单交易币总个数
        Double orderTradeAmtCoin = CurrencyUtil.multiply(orderTradeAmt, tradeScale, 4);

        //今日签到总支出金额
        Double currentSignAmt = userSignService.sumUserSignByTime(startTime, endTime);

        //换算成对应币的个数，今日任务奖励各项币支出
        Double currentSignPayAmtCoin = CurrencyUtil.multiply(orderPayAmt, payScale * rewardConvertPayScale, 4);
        Double currentSignTradeAmtCoin = CurrencyUtil.multiply(orderTradeAmt, tradeScale * rewardConvertTradeScale, 4);
        Double currentSignEquityAmtCoin = CurrencyUtil.multiply(orderTradeAmt, rmbConvertEquityScale * rewardConvertEquityScale, 4);

        //总拨出
        Double maxOut = userService.sumUserMaxProfitByTime(null, null);
        //历史签到总支出金额
        Double hasOut = userSignService.sumUserSignByTime(null, null);
        Map<String, Object> map = new HashMap<>();


        //今日收入
        map.put("currentOrderCount", currentOrderCount);
        map.put("currentOrderAmt", currentOrderAmt);
        map.put("currentOrderPayAmtCoin", currentOrderPayAmtCoin);
        map.put("currentOrderTradeAmtCoin", currentOrderTradeAmtCoin);

        //今日拨出
        map.put("currentSignPayAmtCoin", currentSignPayAmtCoin);
        map.put("currentSignTradeAmtCoin", currentSignTradeAmtCoin);
        map.put("currentSignEquityAmtCoin", currentSignEquityAmtCoin);
        map.put("currentSignEquityAmtCoin", currentSignEquityAmtCoin);
        //总收入
        map.put("orderAmt", orderAmt);
        map.put("orderCount", orderCount);
        map.put("orderPayAmtCoin", orderPayAmtCoin);
        map.put("orderTradeAmtCoin", orderTradeAmtCoin);
        //拨付比
        map.put("maxOut", maxOut);
        map.put("hasOut", hasOut);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", map);
        return respBody;
    }
}
