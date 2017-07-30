package com.hansen.common.utils.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum GradeType {

    /** 一星 */
    GRADE1(1, "一星"),

    /** 二星 */
    GRADE2(2, "二星"),

    /** 三星 */
    GRADE3(3, "三星"),

    /** 四星 */
    GRADE4(4, "四星"),

    /** 五星 */
    GRADE5(5, "五星");


    private final Integer code;

    private final String msg;

    private GradeType(Integer code, String msg) {
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
