package com.hansen.controller;

import com.base.page.JsonResult;
import com.common.base.TokenUtil;
import com.common.constant.RedisKey;
import com.common.utils.codeutils.Md5Util;
import com.hansen.service.BaseAdminService;
import com.hansen.vo.LoginVo;
import com.model.BaseAdmin;
import com.redis.Strings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zzwei on 2017/5/19 0019.
 */
@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private BaseAdminService baseAdminService;

    @RequestMapping("/loginIn")
    @ResponseBody
    public JsonResult login(HttpServletRequest request, HttpServletResponse response, @RequestBody LoginVo vo) {
        if (StringUtils.isEmpty(vo.getUserName())) {
            return new JsonResult(-1, "用户名不能为空");
        }
        if (StringUtils.isEmpty(vo.getPassword())) {
            return new JsonResult(-1, "密码不能为空");
        }
        BaseAdmin model = new BaseAdmin();
        model.setUserName(vo.getUserName());
        BaseAdmin admin = baseAdminService.readOne(model);
        if (admin == null) {
            return new JsonResult(-1, "用户不存在");
        }
        model.setPassword(Md5Util.MD5Encode(vo.getPassword(), admin.getSalt()));
        admin = baseAdminService.readOne(model);
        if (admin == null) {
            return new JsonResult(-1, "密码错误");
        }

        String name = StringUtils.isEmpty(admin.getNickName()) ? "" : admin.getNickName();
        String token = TokenUtil.generateToken(admin.getId(), admin.getNickName());
        //token 放入缓存,有效时间十分钟
        Strings.setEx(RedisKey.BOSS_TOKEN_API.getKey()+admin.getId(), 10 * 60, token);
        response.setHeader("token", token);
        Map map = new HashMap();
        map.put("token", token);
        map.put("nickName", admin.getNickName());
        map.put("headImgUrl", admin.getHeadImgUrl());
        map.put("phone", admin.getPhone());
        map.put("userName", admin.getUserName());
        map.put("id", admin.getId());
        return new JsonResult(map);
    }

    public static void main(String[] args) {
        System.out.println(Md5Util.MD5Encode("1234", "111"));
    }

}
