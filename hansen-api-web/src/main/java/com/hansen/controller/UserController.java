package com.hansen.controller;

import com.base.page.JsonResult;
import com.common.base.TokenUtil;
import com.common.constant.RedisKey;
import com.hansen.service.UserService;
import com.model.User;
import com.redis.Strings;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public JsonResult loginByWeixin(HttpServletRequest request, String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return new JsonResult(-1, "id不能為空");
        }
        User user = userService.readById(id);
        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        user.setRemark(token);
        return new JsonResult(user);
    }
}
