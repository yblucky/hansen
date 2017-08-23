package com.hansen.syscontroller;

import com.alibaba.fastjson.JSON;
import com.annotation.SystemControllerLog;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.hansen.service.KaptchaService;
import com.hansen.service.LoginService;
import com.hansen.service.UserService;
import com.hansen.vo.LoginVo;
import com.model.User;
import com.utils.toolutils.LogUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

;

/**
 * 用户登陆控制器
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
    @Resource
    private LoginService loginService;
    @Resource
    private KaptchaService kaptchaService;
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     *
     * @param loginVo 接收Vo
     * @return 响应对象
     */
    @RequestMapping("/loginIn")
    @SystemControllerLog(description = "用户登录")
    public RespBody longIn(@RequestBody LoginVo loginVo) {
        User mode = new User();
        mode.setUid(200016);
        User user = userService.readById("200016");
        System.out.println(JSON.toJSONString(user));
        // 创建返回对象
        RespBody respBody = new RespBody();
        try {
            //验证FormBean
            if (hasErrors(loginVo, respBody)) {
                // 验证通过，调用业务层，实现登录验证处理
                respBody = loginService.LoginIn(loginVo);
            }
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户登录失败");
            LogUtils.error("用户登录失败！", ex);
        }
        return respBody;
    }

    /**
     * 加载图片验证码
     *
     * @return
     */
    @RequestMapping("/loadImgCode")
    public RespBody loadImgCode() {
        // 创建返回对象
        RespBody respBody = new RespBody();
        try {
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "获取图片验证码成功", kaptchaService.createCodeImg());
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "获取图片验证码失败");
            LogUtils.error("用户登录失败！", ex);
        }
        return respBody;
    }


    /**
     * 验证formBean
     *
     * @param loginVo
     * @param respBody
     * @return
     */
    private boolean hasErrors(LoginVo loginVo, RespBody respBody) {
        if (StringUtils.isEmpty(loginVo.getLoginName())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户名不能为空！");
            return false;
        }
        if (StringUtils.isEmpty(loginVo.getPassword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "密码不能为空！");
            return false;
        }
        if (StringUtils.isEmpty(loginVo.getPicCode())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "验证码不能为空！");
            return false;
        }
        return true;
    }

}
