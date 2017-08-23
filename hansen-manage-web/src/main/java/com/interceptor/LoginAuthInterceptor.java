package com.interceptor;

import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.service.RedisService;
import com.sysservice.SysUrlRecordService;
import com.vo.SysUserVo;
import com.utils.ConfUtils;
import com.utils.toolutils.LogUtils;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


/**
 * 请求拦截器，校验用户是否登录
 *
 * @author qsy
 * @version v1.0
 * @date 2016年11月28日
 */
public class LoginAuthInterceptor extends HandlerInterceptorAdapter {
    @Resource
    private SysUrlRecordService sysUrlRecordService;
    @Autowired
    private RedisService redisService;
    @Resource
    private ConfUtils confUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contextPath = request.getContextPath();
        String requestUri = request.getRequestURI();
        //获取请求URL
        String url = requestUri.substring(contextPath.length());
        if (url.lastIndexOf("?") > 0) {
            url = url.substring(0, url.lastIndexOf("?"));
        }
        //查找不被拦截URL
        List<String> urls = sysUrlRecordService.findUrl();
        if (urls.contains(url)) {
            return true;
        }

        //获取请求token
        String token = request.getHeader("token");
        if (null == token) {
            token = request.getParameter("token");
        }
        LogUtils.info("请求Token:" + token);

        //token为空
        if (token == null || token.trim().length() == 0 || token.equals("null")) {
            RespBody respBody = new RespBody();
            respBody.add(RespCodeEnum.NOLOGIN.getCode(), "非法访问，系统自动退出");
            errorOut(response, respBody);
            return false;
        }

        //redis是否存在
        Object obj = redisService.getObj(token);
        if (obj == null || !(obj instanceof SysUserVo)) {
            RespBody respBody = new RespBody();
            respBody.add(RespCodeEnum.NOLOGIN.getCode(), "过长时间没有操作，页面过期，请重新登录");
            errorOut(response, respBody);
            return false;
        }
        //更新时间
        redisService.expire(token, confUtils.getSessionTimeout());
        return true;
    }

    private void errorOut(HttpServletResponse response, RespBody res) {
        response.setStatus(608);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print(ToolUtil.toJson(res));
        } catch (IOException e) {
            LogUtils.error("响应异常", e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
                out = null;
            }
        }
    }
}
