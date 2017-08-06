package com.common.utils.toolutils;

import javax.servlet.http.HttpServletRequest;

/**
 * @date 2017年08月07日
 */
public class BrowserUtil {

    public static final Boolean isWechat(HttpServletRequest request) {
        Boolean flag = false;
        String ua = request.getHeader("user-agent").toLowerCase();
        if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
            flag = true;
        }
        return flag;
    }
}
