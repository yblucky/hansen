package com.constant;

/**
 * 订单枚举类
 *
 * @date 2016年11月15日
 */
public enum OrderStatus {

    /**
     * 待结算
     */
    PENDING(1, "保单处理中"),

    /**
     * 已结算
     */
    HANDING(2, "任务进行中"),

    /**
     * 任务完成，奖励已分配
     */
    HANDLED(3, "任务完成，奖励已分配"),

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

    public static String getName(Integer code){
        if (code == null) {
            return "";
        }
        for(OrderStatus enums : OrderStatus.values()){
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
