package com.mall.constant;

/**
 * 流水类型枚举类
 * @date 2016年11月15日
 */
public enum RecordType {

    /**
     * 购买
     */
    RECHARGE(1, "购买"),
    /**
     * 退款
     */
    REFUNDS(2, "退款");

    private final Integer code;

    private final String msg;

    private RecordType(Integer code, String msg) {
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
