package com.common.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum UpGradeType {

    /** 原点升级 */
    ORIGINUPGRADE(1, "原点升级"),

    /** 覆盖升级 */
    COVERAGEUPGRADE(2, "覆盖升级");


    private final Integer code;

    private final String msg;

    private UpGradeType(Integer code, String msg) {
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
