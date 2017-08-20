package com.hansen.sysservice;

import com.hansen.resp.RespBody;
import com.hansen.vo.LoginMvo;

/**
 * 用户登录业务层接口
 */
public interface LoginService {

    /**
     * 用户登录
     *
     * @param loginVo 登录信息
     * @return
     * @throws Exception
     */
    public RespBody LoginIn(LoginMvo loginVo) throws Exception;


}
