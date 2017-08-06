package com.common.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum CardLevelType {

    /** 普卡 */
    ORDINARYCARD(1, "普卡"),

    /** 铜卡 */
    COPPERCARD(2, "铜卡"),

    /** 银卡 */
    SILVERCARD(3, "银卡"),

    /** 金卡 */
    GOLDCARD(4, "金卡"),

    /** 钻石卡 */
    DRILLCRAD(5, "钻石卡");


    private final Integer code;

    private final String msg;

    private CardLevelType(Integer code, String msg) {
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
