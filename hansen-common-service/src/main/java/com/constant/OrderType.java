package com.constant;

/**
 * 订单类型枚举类 <br/>
 *
 * @date 2017年08月07日
 */
public enum OrderType {
    INSURANCE(1, "保单"),
    /**
     * 直推奖
     */
    PUSH(2, "直推奖"),

    /**
     * 管理奖
     */
    MANAGE(3, "管理奖"),

    /**
     * 级差奖
     */
    DIFFERENT(4, "级差奖"),

    /**
     * 平级奖
     */
    EQUALITY(5, "平级奖"),
    /**
     * 平级奖
     **/
    RELASE(6, "每周收益"),
    /**
     * 原点升级保单
     **/
    INSURANCE_ORIGIN(1, "原点升级保单"),
    /**
     * 覆盖升级保单
     **/
    INSURANCE_COVER(1, "覆盖升级保单");

    private final Integer code;

    private final String msg;

    private OrderType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static OrderType fromCode(Integer code) {
        try {
            for(OrderType orderType : OrderType.values()){
                if(orderType.getCode().intValue() == code.intValue()){
                    return orderType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }


}
