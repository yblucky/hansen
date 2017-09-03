package com.constant;

/**
 * 订单类型枚举类 <br/>
 *
 * @date 2017年08月07日
 */
public enum WalletOrderType {
    /**
     *交易币内部转账
     */
    TRADE_COIN_INNER_TRANSFER(1, "交易币内部转账"),
    /**
     * 交易币提币
     */
    TRADE_COIN_DRWA(2, "交易币提币"),

    /**
     * 交易币充币
     */
    TRADE_COIN_RECHARGE (3, "交易币充币"),
    /**
     *购物币内部转账
     */
    PAY_COIN_INNER_TRANSFER(4, "购物币内部转账"),
    /**
     * 购物币提币
     */
    PAY_COIN_DRWA(5, "购物币提币"),
    /**
     * 购物币充币
     */
    PAY_COIN_RECHARGE (6, "购物币充币"),

    /**
     * 股权币充币
     */
    EQUITY_COIN_RECHARGE (7, "股权币充币"),
    /**
     *股权币内部转账
     */
    EQUITY_COIN_INNER_TRANSFER(8, "股权币内部转账"),
    /**
     * 股权币提币
     */
    EQUITY_COIN_DRWA(9, "股权币提币"),
    /**
     * 交易币管理员充值
     */
    TRADE_COIN_BACK_CHARGE(10, "交易币管理员充值"),
    /**
     * 购物币管理员充值
     */
    PAY_COIN_BACK_CHARGE(11, "购物币管理员充值"),
    /**
     * 股权币提币
     */
    EQUITY_COIN_BACK_CHARGE(12, "股权币管理员充值");

    private final Integer code;

    private final String msg;

    private WalletOrderType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static WalletOrderType fromCode(Integer code) {
        try {
            for(WalletOrderType orderType : WalletOrderType.values()){
                if(orderType.getCode().intValue() == code.intValue()){
                    return orderType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static  CurrencyType getCoinTypeFromWalletOrderTypeCode(Integer code) {
           if (code==2){
               return CurrencyType.TRADE;
           }else if (code==5){
               return CurrencyType.PAY;
           }else if (code==8){
               return CurrencyType.EQUITY;
           }
            return null;
    }
}
