package com.mall.service;

import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.core.service.CommonService;
import com.mall.model.MallOrder;
import com.sun.tools.internal.ws.processor.model.Model;

import java.rmi.server.ExportException;

/**
 * @date 2016年11月27日
 */
public interface MallOrderService extends CommonService<MallOrder> {

    /**
     * 分页获取订单列表
     * @param model
     * @param page
     * @return
     * @throws Exception
     */
    public PageResult<MallOrder> getPage(MallOrder model, Page page) throws Exception;

}
