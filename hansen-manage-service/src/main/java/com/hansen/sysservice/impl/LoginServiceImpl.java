package com.hansen.sysservice.impl;


import com.common.service.RedisService;
import com.common.utils.ConfUtils;
import com.common.utils.codeutils.CryptUtils;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.enums.RespCodeEnum;
import com.hansen.enums.StateEnum;
import com.hansen.resp.RespBody;
import com.hansen.sysmapper.SysUserMapper;
import com.hansen.sysservice.LoginService;
import com.hansen.vo.LoginMvo;
import com.hansen.vo.SysUserVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户登录业务层实现类
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private RedisService redisService;
    @Resource
    private ConfUtils confUtils;

    /**
     * 用户登录验证
     *
     * @param loginVo 登录信息
     * @return token
     * @throws Exception
     */
    @Override
    public RespBody LoginIn(LoginMvo loginVo) throws Exception {
        RespBody respBody = new RespBody();
        // 产生token
        String token = ToolUtil.getUUID();
        //验证码是否正确
        String picCode = redisService.getString(loginVo.getImgKey());
        if (picCode != null && picCode.equals(loginVo.getPicCode())) {
            //验证码正确,查询用户信息
            SysUserVo userVo = sysUserMapper.findByloginName(loginVo.getLoginName());
            // 是否查找到用户信息
            if (userVo == null) {
                // 不存在
                respBody.add(RespCodeEnum.ERROR.getCode(), "登录用户不存在");
            } else {
                // 存在用户，判断是否有效
                if (userVo.getState().equals(StateEnum.DISABLE.getCode())) {
                    // 无效用户
                    respBody.add(RespCodeEnum.ERROR.getCode(), "登录用户已被禁用，请联系管理员！");
                } else {
                    // 用户有效，对输入密码进行加密
                    String loginPw = CryptUtils.hmacSHA1Encrypt(loginVo.getPassword(), userVo.getSalt());
                    // 验证密码是否正确
                    if (loginPw.equals(userVo.getPassword())) {
                        //将对象转换成序列化对象
                        // 登录成功,将用户信息存储到redis中
                        if (!redisService.putObj(token, userVo, confUtils.getSessionTimeout()).equalsIgnoreCase("ok")) {
                            // 缓存用户信息失败
                            respBody.add(RespCodeEnum.ERROR.getCode(), "缓存用户信息失败！");
                        } else {
                            respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户登录成功", token);
                        }
                    } else {
                        // 登录失败
                        respBody.add(RespCodeEnum.ERROR.getCode(), "登录密码错误！");
                    }
                }
            }
        } else {
            //验证码输入有误或失效
            respBody.add(RespCodeEnum.ERROR.getCode(), "验证码输入有误或已失效");
        }
        return respBody;
    }

}
