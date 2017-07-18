package com.api.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum RecordType {
    // TODO: 2017/7/14 待完善
    /**
     * 每周收益
     */
    RELASE(1, "每周收益"),

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
     * 每周收益之股权币
     */
    RELASEEQUITY(11, "每周收益之股权币"),

    /**
     * 直推奖之股权币
     */
    PUSHEQUITY(21, "直推奖之股权币"),

    /**
     * 管理奖之股权币
     */
    MANAGEEQUITY(31, "管理奖之股权币"),

    /**
     * 级差奖之股权币
     */
    DIFFERENTEQUITY(41, "级差奖之股权币"),

    /**
     * 平级奖之股权币
     */
    EQUALITYEQUITY(51, "平级奖之股权币"),

    /**
     * 每周收益之支付币
     */
    RELASEPAY(12, "每周收益之支付币"),

    /**
     * 直推奖之支付币
     */
    PUSHPAY(22, "直推奖之支付币"),

    /**
     * 管理奖之支付币
     */
    MANAGEPAY(32, "管理奖之支付币"),

    /**
     * 级差奖之支付币
     */
    DIFFERENTPAY(42, "级差奖之支付币"),

    /**
     * 平级奖之支付币
     */
    EQUALITYPAY(52, "平级奖之支付币"),

    /**
     * 每周收益之交易币
     */
    RELASETRADE(13, "每周收益之交易币"),

    /**
     * 直推奖之交易币
     */
    PUSHTRADE(23, "直推奖之交易币"),

    /**
     * 管理奖之交易币
     */
    MANAGETRADE(33, "管理奖之交易币"),

    /**
     * 级差奖之交易币
     */
    DIFFERENTTRADE(43, "级差奖之交易币"),

    /**
     * 平级奖之交易币
     */
    EQUALITYTRADE(53, "平级奖之交易币");


    private final Integer code;

    private final String msg;

    private RecordType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static RecordType fromCode(Integer code) {
        try {
            for(RecordType recordType :RecordType.values()){
                if(recordType.getCode().intValue() == code.intValue()){
                    return recordType;
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
