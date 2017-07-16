package com.api.controller;

import com.api.common.Token;
import com.api.core.page.JsonResult;
import com.api.core.page.Page;
import com.api.core.page.PageResult;
import com.api.model.User;
import com.api.service.UserService;
import com.api.util.TokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController  extends  BaseController{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    /**
     * 会员列表
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(HttpServletRequest request, User model, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        int count = userService.readCount(model);
        List<User> goodsList = userService.readList(model, page.getPageNo(), page.getPageSize(), count);
        PageResult<User> result = new PageResult<User>(page.getPageNo(), page.getPageSize(), count, goodsList);
        return new JsonResult(result);
    }

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
    protected JsonResult list(HttpServletRequest request, Object model, Page page) throws Exception {
        return null;
    }
}
