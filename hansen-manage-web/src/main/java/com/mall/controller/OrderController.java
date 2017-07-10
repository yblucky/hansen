package com.mall.controller;

import com.mall.core.page.JsonResult;
import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.model.MallOrder;
import com.mall.service.MallOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单controller
 */
@Controller
@RequestMapping("/order")
public class OrderController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private MallOrderService mallOrderService;


    /**
     * 分页获取订单列表
     *
     * @param request
     * @param page
     * @return
     * @throws Exception
     */

    @Override
    protected JsonResult index(HttpServletRequest request, Object model, Page page) throws Exception {
        return null;
    }

    @Override
    protected JsonResult add(HttpServletRequest request, Object model) throws Exception {
        return null;
    }

    @Override
    protected JsonResult edit(HttpServletRequest request, Object model) throws Exception {
        return null;
    }

    @Override
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    @ResponseBody
    protected JsonResult list(HttpServletRequest request, Object model, Page page) throws Exception {
        //查询订单列表
        PageResult<MallOrder> orderList = mallOrderService.getPage(new MallOrder(), page);
        //返回订单列表
        return new JsonResult(orderList);
    }
}
