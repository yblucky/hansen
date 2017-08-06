package com.hansen.interceptor;

import com.base.exception.CoreException;
import com.base.page.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 *
 * @date 2016年12月8日
 */
@ControllerAdvice
public class ApiExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler
    public final JsonResult handleException(Exception ex, WebRequest request) {
        int code = 500;
        if (ex instanceof CoreException) {
            CoreException oex = (CoreException) ex;
            code = oex.getCode();
        }
        JsonResult body = new JsonResult();
        body.setCode(code);

        HttpServletRequest req = (HttpServletRequest) request.resolveReference(WebRequest.REFERENCE_REQUEST);
        String requestUrl = null == req ? "" : req.getRequestURI() + "|" + req.getParameterMap();
        if (ex instanceof CoreException) {
            body.setMsg(ex.getMessage());
            logger.error(((CoreException) ex).getCode() + "|" + ex.getMessage() + "|" + requestUrl);
        } else {
            body.setMsg("服务器异常，请稍候再试！");
            logger.error(requestUrl, ex);
        }
        return body;
    }

}
