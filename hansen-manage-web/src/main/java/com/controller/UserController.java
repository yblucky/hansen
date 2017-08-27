package com.controller;


import com.base.page.*;
import com.Token;
import com.base.TokenUtil;
import com.constant.StatusType;
import com.model.SysUser;
import com.service.UserDetailService;
import com.service.UserService;
import com.sysservice.ManageUserService;
import com.utils.classutils.MyBeanUtils;
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
import java.util.ArrayList;
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
    public RespBody list(HttpServletRequest request, UserVo vo, Paging page) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        User condition = new User();
        condition.setUid(vo.getUid());
        condition.setPhone(vo.getPhone());
        condition.setId(vo.getId());
        Integer count = userService.readCount(condition);
        List<User> users = userService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
        List<UserVo> list = new ArrayList<UserVo>();
        UserVo userVo = null;
        for(User po : users){
            userVo= MyBeanUtils.copyProperties(po,UserVo.class);
            userVo.setGrade(po.getGrade());
            userVo.setCardGrade(po.getCardGrade());
            userVo.setStatus(po.getStatus());
            list.add(userVo);
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(),"成功",page,list);
        return respBody;
    }

    @ResponseBody
    @RequestMapping("/update")
    public RespBody update(HttpServletRequest request, User model) {
        // 创建返回对象
        RespBody respBody = new RespBody();
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (null == user) {
            logger.error(String.format("Illegal user id[%s]", token.getId()));
            throw new IllegalArgumentException();
        }
        if (user.getStatus() == StatusType.DEL.getCode()) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户已删除");
            return respBody;
        }
        model.setStatus(model.getStatus());
        model.setContactUserId(model.getContactUserId());
        model.setNickName(model.getNickName());
        model.setPhone(model.getPhone());
        userService.updateById(user.getId(), user);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户已更新");
        return respBody;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public RespBody delete(HttpServletRequest request, String userId) {
        // 创建返回对象
        RespBody respBody = new RespBody();
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (null == user) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
            return respBody;
        }
        userService.deleteById(userId);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户已删除");
        return respBody;
    }

    @ResponseBody
    @RequestMapping("/detail")
    public RespBody detail(HttpServletRequest request, String userId) {
        UserVo userVo = null;
        // 创建返回对象
        RespBody respBody = new RespBody();
        try {
            Token token = TokenUtil.getSessionUser(request);
            User user = userService.readById(token.getId());
            if (user == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
                return respBody;
            }
            userVo = new UserVo();
            BeanUtils.copyProperties(userVo, user);

            UserDetail userDetail = userDetailService.readById(userId);
            if (userDetail != null) {
                BeanUtils.copyProperties(userVo, userDetail);
            }
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功获取");
        } catch (Exception e) {
            e.printStackTrace();
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
        }
        return respBody;
    }


}
