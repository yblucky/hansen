package com.hansen.service;


import com.base.page.RespBody;
import com.hansen.vo.LoginVo;

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
    public RespBody LoginIn(LoginVo loginVo) throws Exception;


}
