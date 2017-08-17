package com.common.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum UpGradeType {

    /**保单升级 */
    INSURANCE(1, "保单升级"),
    /** 原点升级 */
    ORIGINUPGRADE(2, "原点升级"),
    /** 覆盖升级 */
    COVERAGEUPGRADE(3, "覆盖升级");


    private final Integer code;

    private final String msg;

    public static UpGradeType fromCode(Integer code) {
        try {
            for(UpGradeType upGradeType : UpGradeType.values()){
                if(upGradeType.getCode().intValue() == code.intValue()){
                    return upGradeType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }


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
