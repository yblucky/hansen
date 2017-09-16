package com.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum UserStatusType {

    /** 分享注册 */
    OUT_SHARE_REGISTER_SUCCESSED(1, "分享注册"),

    /** 注册成功 */
    INNER_REGISTER_SUCCESSED(2, "内部注册成功"),

    /** 激活成功*/
    ACTIVATESUCCESSED(3, "激活成功"),

    /** 待激活（保单正在处理中） */
    WAITACTIVATE(4, "待激活（正在保单中）"),

    /** 待激活（重新使用消费码的） */
    OUT(5, "待激活"),

    /** 出局（可以重新激活） */
    ORDER_OUT(6, "出局"),

    /** 已删除 */
    DEL(7, "已删除"),

    /** 禁用 */
    DISABLE(8, "禁用");


    private final Integer code;

    private final String msg;

    private UserStatusType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
    public static String getName(Integer code){
        if (code == null) {
            return "";
        }
        for(UserStatusType enums : UserStatusType.values()){
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
