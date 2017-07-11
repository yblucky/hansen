package com.manage.controller;

import com.manage.constant.OrderType;
import com.manage.core.page.JsonResult;
import com.manage.model.MallOrder;
import com.manage.service.*;
import com.manage.util.WechatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/app")
public class AppController {

    private static final Logger logger = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private BaseUserService userService;
    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallSortService goodsSortService;
    @Autowired
    private MallStoreService mallService;
    @Autowired
    private MallOrderService goodsWinService;
    @Autowired
    private MallStoreService mallStoreService;

    // private static String
    // JP_URL="http://192.168.2.200:9010/api/mall/store/goods/draw";
    private static String JP_URL = "https://www.6pyun.com/api/mall/store/goods/draw";
    private static String INNER_TEST_URL = "https://www.6pyun.com/api/mall/store/goods/draw";
    private static String LOCAL_TEST_URL = "http://127.0.0.1:8090/api/mall/store/goods/draw";
    private static String TEST_200_URL = "http://192.168.2.200:9010/api/mall/store/goods/draw";

    @ResponseBody
    @RequestMapping(value = "/callscan", method = RequestMethod.GET)
    public JsonResult callScan(HttpServletRequest request, String orderNo, String auth, MallOrder model) throws Exception {
        String uri = request.getRequestURI();
        if (org.apache.commons.lang3.StringUtils.isEmpty(orderNo)) {
            return new JsonResult("订单号不能为空");
        }
        if (null == model) {
            return new JsonResult("订单不存在");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(model.getOrderNo())) {
            return new JsonResult("此订单不是扫码支付订单，接口调用错误");
        }

        if (model.getStatus() != OrderType.PENDING.getCode()) {
            return new JsonResult("订单支付微信支付宝已经回调成功");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(model.getUserId())) {
            return new JsonResult("用户id不能为空");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(auth) || !auth.equals("qwertyui")) {
            return new JsonResult("error");
        }

        if (model.getStatus() != OrderType.PENDING.getCode()) {
            return new JsonResult("订单支付宝微信已经回调成功");
        }
        Boolean isSuccess = false;
        isSuccess = WechatUtil.isAppPaySucess(orderNo);
        if (!isSuccess) {

            return new JsonResult("微信扫码订单未支付");
        }
        isSuccess = WechatUtil.isAppScanAliPaySucess(orderNo);
        if (!isSuccess) {
            return new JsonResult("支付宝扫码订单未支付");
        }

        isSuccess = WechatUtil.isAppScanAliPaySucess(orderNo);

        return new JsonResult("callscan 处理成功");
    }


    public static void main(String[] args) {
//        System.out.println(WechatUtil.isAppScanAliPaySucess("20170428115454480065"));
        System.out.println(WechatUtil.isAppScanAliPaySucess("20170428115454480065"));
    }
}
