package com.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum GradeType {
    /**
     * 普通会员
     */
    GRADE0(0, "普通会员"),
    /**
     * 专员
     */
    GRADE1(1, "专员"),

    /**
     * 主任
     */
    GRADE2(2, "主任"),

    /**
     * 经理
     */
    GRADE3(3, "经理"),
    /**
     * 区代
     */
    GRADE4(5, "区代"),

    /**
     * 县代
     */
    GRADE5(5, "县代"),

    /**
     * 市代
     */
    GRADE6(6, "市代"),

    /**
     * 省代
     */
    GRADE7(7, "省代"),

    /**
     * 董事
     */
    GRADE8(8, "董事");


    private final Integer code;

    private final String msg;

    private GradeType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static String getName(Integer code){
        if (code == null) {
            return "";
        }
        for(GradeType enums : GradeType.values()){
            if (enums.getCode().equals(code)) {
                return enums.getMsg();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

}
