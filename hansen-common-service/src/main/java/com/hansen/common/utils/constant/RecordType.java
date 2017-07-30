package com.hansen.common.utils.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 *
 */
public enum RecordType {
    // TODO: 2017/7/14 待完善
    /** 每周收益 */
    RELASE(1, "每周收益"),

    /** 直推奖 */
    PUSH(2, "直推奖"),

    /** 管理奖 */
    MANAGE(3, "管理奖"),

    /** 级差奖 */
    DIFFERENT(4, "级差奖"),

    /** 平级奖 */
    EQUALITY(5, "平级奖");


    private final Integer code;

    private final String msg;

    private RecordType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    public static RecordType fromCode(Integer code) {
        try {
            for(RecordType recordType : RecordType.values()){
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
