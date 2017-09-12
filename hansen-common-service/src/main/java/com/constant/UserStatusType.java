package com.constant;

/**
 * @author jay.zheng
 * @date 2017/7/12
 */
public enum UserStatusType {

    /** 分享注册 */
    OUT_SHARE_REGISTER_SUCCESSED(1, "分享注册"),

    /** 注册成功 */
    INNER_REGISTER_SUCCESSED(2, "市场内部注册成功"),

    /** 激活成功  市场内部注册账号，用户登录时默认激活 */
    ACTIVATESUCCESSED(3, "激活成功"),

    /** 待激活（正在保单中） */
    WAITACTIVATE(4, "待激活（正在保单中）"),

    /** 待激活（重新使用激活码的） */
    OUT(5, "待激活"),

    /** 出局（可以重新激活） */
    ORDER_OUT(6, "出局"),

    /** 已删除 */
    DEL(7, "已删除");


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
