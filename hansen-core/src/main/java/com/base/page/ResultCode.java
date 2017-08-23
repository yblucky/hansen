package com.base.page;

/**
 * 返回码定义
 *
 * @date 2017年08月07日
 */
public enum ResultCode {
    /**
     * 失败
     */
    ERROR(9999, "失败"),

    /**
     * 成功
     **/
    SUCCESS(200, "成功"),

    /**
     * 无令牌
     */
    NO_TOKEN(0, "登录失效"),

    /**
     * 登录失效
     */
    NO_LOGIN(-1, "登录失效，请重新登录"),

    MANGE_SUCCESS(0000, "成功"),

    MANGE_NOLOGIN(5000, "沒有登录"),

    MANGE_ERROR(9999, "错误");

    private final Integer code;

    private final String msg;

    private ResultCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
