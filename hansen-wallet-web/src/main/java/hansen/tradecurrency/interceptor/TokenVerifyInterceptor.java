package hansen.tradecurrency.interceptor;


import hansen.utils.Key;
import hansen.utils.ResourceUtil;
import hansen.utils.EncryptUtil;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class TokenVerifyInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.addHeader("Access-Control-Max-Age", "1728000");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With,Cache-Control, Expires, Content-Type");

        String sign = request.getHeader("sign");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");

        if (ResourceUtil.getIsEncrypt()) {
            if (!EncryptUtil.isEmpty(sign)) {
                if (Key.check(sign)) {
                    return true;
                }
                System.out.println("签名校验失败");
                return false;
            }
        }
        if (EncryptUtil.isNotEmpty(ResourceUtil.getIsDebug()) && ResourceUtil.getIsDebug().equals("true")) {
            return true;
        }
        System.out.println("isDebug false");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object object, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object object, Exception exception) throws Exception {
    }
}
