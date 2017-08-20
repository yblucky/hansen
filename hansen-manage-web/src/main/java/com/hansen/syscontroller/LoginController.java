package com.hansen.syscontroller;

import com.common.utils.toolutils.LogUtils;
import com.hansen.annotation.SystemControllerLog;
import com.hansen.enums.RespCodeEnum;
import com.hansen.resp.RespBody;
import com.hansen.sysservice.KaptchaService;
import com.hansen.sysservice.LoginService;
;
import com.hansen.vo.LoginMvo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 用户登陆控制器
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController {
    @Resource(name = "webLogin")
    private LoginService loginService;
    @Resource
    private KaptchaService kaptchaService;

    /**
     * 用户登录
     *
     * @param loginVo 接收Vo
     * @return 响应对象
     */
    @RequestMapping("/loginIn")
    @SystemControllerLog(description = "用户登录")
    public RespBody longIn(@RequestBody LoginMvo loginVo) {
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
    private boolean hasErrors(LoginMvo loginVo, RespBody respBody) {
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
