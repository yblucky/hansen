package com.hansen.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created on 2017-08-21;
 */
public class BaseController {

    public static JsonResult success(int pageNo, int pageSize, int total, List list) {
        PageResult pageResult = new PageResult(pageNo, pageSize, total, list);
        JsonResult jsonResult = new JsonResult(list);
        return jsonResult;
    }

    public static JsonResult success(ResultCode resultCode) {
        JsonResult jsonResult = new JsonResult(resultCode.getCode(), resultCode.getMsg());
        return jsonResult;
    }

    public static JsonResult success() {
        JsonResult jsonResult = new JsonResult(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMsg());
        return jsonResult;
    }


    public static JsonResult success(Object obj) {
        JsonResult jsonResult = new JsonResult(obj);
        return jsonResult;
    }

    public static JsonResult success(ResultCode resultCode,Object obj) {
        JsonResult jsonResult = new JsonResult(resultCode.getCode(),resultCode.getMsg(),obj);
        return jsonResult;
    }

    public static JsonResult fail(String message) {
        JsonResult jsonResult = new JsonResult(ResultCode.MANGE_ERROR.getCode(), message);
        return jsonResult;
    }

    public static JsonResult fail(ResultCode resultCode) {
        JsonResult jsonResult = new JsonResult(resultCode.getCode(), resultCode.getMsg());
        return jsonResult;
    }

    public static JsonResult fail(int code, String message) {
        JsonResult jsonResult = new JsonResult(code, message);
        return jsonResult;
    }

    public static JsonResult fail(int code, String message, Object result) {
        JsonResult jsonResult = new JsonResult(code, message, result);
        return jsonResult;
    }

    public static PageResult getPageResult(Page page, int count, List list) {
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return pageResult;
    }

    public static Token getToken(HttpServletRequest request) {
        String tokenBody = request.getHeader("token");
        Token token = (Token) TokenUtil.getTokenObject(tokenBody);
        return token;
    }

}
