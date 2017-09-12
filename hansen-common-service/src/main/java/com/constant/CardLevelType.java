package com.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum CardLevelType {

    /**
     * 未选
     */
    UNCHOOSE(0, "未选"),

    /**
     * 普卡
     */
    ORDINARYCARD(1, "普卡"),

    /**
     * 铜卡
     */
    COPPERCARD(2, "铜卡"),

    /**
     * 银卡
     */
    SILVERCARD(3, "银卡"),

    /**
     * 金卡
     */
    GOLDCARD(4, "金卡"),

    /**
     * 钻石卡
     */
    DRILLCRAD(5, "钻石卡");


    private final Integer code;

    private final String msg;

    private CardLevelType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static String getName(Integer code) {
        if (code == null) {
            return "";
        }
        for (CardLevelType enums : CardLevelType.values()) {
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }
        return "";
    }

    public static CardLevelType fromCode(Integer code) {
        try {
            for (CardLevelType cardLevelType : CardLevelType.values()) {
                if (cardLevelType.getCode().intValue() == code.intValue()) {
                    return cardLevelType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
