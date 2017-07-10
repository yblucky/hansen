package com.mall.controller;

import com.mall.core.page.JsonResult;
import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.model.MallRecord;
import com.mall.service.MallRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 财务controller
 */
@Controller
@RequestMapping("/finance")
public class FinanceController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(FinanceController.class);

    @Autowired
    private MallRecordService mallRecordService;

    /**
     * 分页查询财务流水记录
     *
     * @param request
     * @param model
     * @param page
     * @return
     * @throws Exception
     */
    @Override
    protected JsonResult index(HttpServletRequest request, Object model, Page page) throws Exception {
        return  null;
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
        //分页查询财务流水记录
        PageResult<MallRecord> recordList = mallRecordService.getPage(new MallRecord(), page);

        //返回财务流水记录
        return new JsonResult(recordList);
    }
}
