package com.controller;


import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.Token;
import com.base.TokenUtil;
import com.base.page.ResultCode;
import com.constant.StatusType;
import com.model.SysUser;
import com.service.UserDetailService;
import com.service.UserService;
import com.sysservice.ManageUserService;
import com.vo.UserVo;
import com.model.User;
import com.model.UserDetail;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created on 2017-08-21;
 */
@Controller
@RequestMapping("/muser")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ManageUserService manageUserService  ;

    @Autowired
    private UserDetailService userDetailService;

    @ResponseBody
    @RequestMapping(value = "/list")
    public JsonResult list(HttpServletRequest request, UserVo vo, Page page) {
        User condition = new User();
        condition.setUid(vo.getUid());
        condition.setPhone(vo.getPhone());
        condition.setId(vo.getId());
        Integer count = userService.readCount(condition);
        List<User> users = userService.readList(condition, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, users);

        SysUser condition1 = new SysUser();
        condition1.setId(vo.getPhone());
        condition1.setId("3709027e482f489dbf5a79ab17649bd6");
        Integer count1 = manageUserService.readCount(condition1);
        List<SysUser> s = manageUserService.readList(condition1, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult1= new PageResult(page.getPageNo(), page.getPageSize(), count1, users);
        return success(ResultCode.MANGE_SUCCESS,pageResult1);
    }

    @ResponseBody
    @RequestMapping("/update")
    public JsonResult update(HttpServletRequest request, User model) {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (user.getStatus() == StatusType.DEL.getCode()) {
            return fail(ResultCode.ERROR.getCode(), "用户已删除");
        }
        model.setStatus(model.getStatus());
        model.setContactUserId(model.getContactUserId());
        model.setNickName(model.getNickName());
        model.setPhone(model.getPhone());
        userService.updateById(user.getId(), user);
        return success(ResultCode.MANGE_SUCCESS);
    }

    @ResponseBody
    @RequestMapping("/delete")
    public JsonResult delete(HttpServletRequest request, String userId) {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (null == user) {
            return fail("用户不存在");
        }
        userService.deleteById(userId);
        return success(ResultCode.MANGE_SUCCESS);
    }

    @ResponseBody
    @RequestMapping("/detail")
    public JsonResult detail(HttpServletRequest request, String userId) {
        UserVo userVo = null;
        try {
            Token token = TokenUtil.getSessionUser(request);
            User user = userService.readById(token.getId());
            if (user == null) {
                return fail("用户不存在");
            }
            userVo = new UserVo();
            BeanUtils.copyProperties(userVo, user);

            UserDetail userDetail = userDetailService.readById(userId);
            if (userDetail != null) {
                BeanUtils.copyProperties(userVo, userDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return fail("用户不存在");
        }
        return success(ResultCode.MANGE_SUCCESS,userVo);
    }


}
