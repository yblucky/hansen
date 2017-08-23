
package com.constant;
/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum GradeRecordType {

    /** 会员卡等级变更 */
    CARDUPDATE(1, "会员卡等级变更"),

    /** 会员星级变更 */
    GRADEUPDATE(2, "会员星级变更");


    private final Integer code;

    private final String msg;

    private GradeRecordType(Integer code, String msg) {
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
