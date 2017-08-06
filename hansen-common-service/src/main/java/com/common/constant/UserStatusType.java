package com.common.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum UserStatusType {

    /** 注册 */
    REGISTER(1, "注册"),

    /** 注册成功 */
    REGISTERSUCCESSED(2, "注册成功"),

    /** 激活成功 */
    ACTIVATESUCCESSED(3, "激活成功"),

    /** 待激活（正在保单中） */
    WAITACTIVATE(4, "待激活（正在保单中）"),

    /** 待激活（出局后的） */
    OUT(5, "待激活（出局后的）"),

    /** 已删除 */
    DEL(6, "已删除");


    private final Integer code;

    private final String msg;

    private UserStatusType(Integer code, String msg) {
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
