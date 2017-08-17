package com.common.constant;

/**
 * 订单类型枚举类 <br/>
 *
 * @date 2017年08月07日
 */
public enum WalletOrderStatus {
    /**
     * 待结算
     */
    PENDING(1, "待审核"),
    /**
     * 审核通过，确认中
     */
    CONFIRMING(2, "审核通过，确认中"),
    /**
     * 审核不通过
     */
    DENIED(3, "审核不通过"),
    /**
     *   交易成功
     */
    SUCCESS(4, "交易成功"),
    /**
     *  已删除
     */
    DEL(5, " 已删除");

    private final Integer code;

    private final String msg;

    private WalletOrderStatus(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static WalletOrderStatus fromCode(Integer code) {
        try {
            for(WalletOrderStatus orderType : WalletOrderStatus.values()){
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
