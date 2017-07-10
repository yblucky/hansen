package com.mall.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mall.common.Token;
import com.mall.redis.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @date 2016年12月8日
 */
public class TokenUtil {

    private static final Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 生成token
     *
     * @return
     */
    public static String generateToken(String id, String nickName) {
        Token token = new Token();
        token.setId(id);
        token.setNickName(nickName);
        token.setTime(new Date().getTime());
        return Base64.encode(JSON.toJSON(token).toString().getBytes());
    }

    /**
     * 获取Token对象
     *
     * @return
     */
    @SuppressWarnings("static-access")
    public static Object getTokenObject(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        JSONObject jsonObject = new JSONObject();
        try {
            return jsonObject.parseObject(new String(Base64.decode(token)), Token.class);
        } catch (Exception e) {
            logger.error("Illegal token[" + token + "]," + e.getMessage());
            return null;
        }
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    public static Token getSessionUser(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (null == token) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return (Token) getTokenObject(token);
    }


    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    public static String getToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (null == token) {
            token = request.getParameter("token");
        }
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        return token;
    }

    public static void main(String[] args) {

        System.out.println(
                TokenUtil.generateToken("F2CEDF2210C243C4A93F286D216569EA","杰")
        );

    }
}
