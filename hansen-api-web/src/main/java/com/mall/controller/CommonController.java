package com.mall.controller;

import com.mall.core.page.JsonResult;
import com.mall.service.BaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/common")
public class CommonController {
    @Autowired
    private BaseAgreementService baseAgreementService;

    /**
     * 关于协议
     */
    @ResponseBody
    @RequestMapping(value = "/about", method = RequestMethod.GET)
    public JsonResult about(HttpServletRequest request) throws Exception {
        return new JsonResult();
    }

}
