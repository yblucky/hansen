package com.constant;

/**
 * 标准状态枚举类 <br/>
 *
 * @date 2016年11月15日
 */
public enum CodeType {

    /**
     * 1.激活码
     */
    ACTIVATECODE(1, "激活码"),
    /**
     * 注册码
     */
    REGISTERCODE(2, "注册码"),

    /**
     * 注册使用注册码
     */
    REGISTER_USE_REGISTERCODE(10, "注册使用注册码"),
    /**
     * 转账使用注册码
     */
    TRANSFER_USE_REGISTERCODE(11, "转账使用注册码"),
    /**
     * 系统赠送注册码
     */
    SYSTEM_SEND_USE_REGISTERCODE(12, "系统赠送注册码"),
    /**
     * /**
     * 激活使用激活码
     */
    REGISTER_USE_ACTIVECODE(20, "激活使用激活码"),
    /**
     * 转账使用激活码
     */
    TRANSFER_USE_ACTIVECODE(21, "转账使用激活码"),
    /**
     * 系统赠送激活码
     */
    SYSTEM_SEND_USE_ACTIVECODE(22, "系统赠送激活码");

    private final Integer code;

    private final String msg;

    private CodeType(Integer code, String msg) {
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
