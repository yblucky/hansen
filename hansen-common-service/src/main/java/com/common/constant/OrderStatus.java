package com.common.constant;

/**
 * 订单枚举类
 *
 * @date 2016年11月15日
 */
public enum OrderStatus {

    /**
     * 待结算
     */
    PENDING(1, "待结算"),

    /**
     * 已结算
     */
    HANDLED(2, "已结算"),

    /**
     * 已取消结算
     */
    CANCEL(3, "已取消结算"),

    /**
     * 已删除
     */
    DEL(4, "已删除");

    private final Integer code;

    private final String msg;

    private OrderStatus(Integer code, String msg) {
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
