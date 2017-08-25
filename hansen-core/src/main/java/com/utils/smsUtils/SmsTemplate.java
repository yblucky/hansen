package com.utils.smsUtils;

/**
 * 短信模板类
 */
public enum SmsTemplate {

    /**
     * 通用
     */
    COMMON("a0e9d2a9f79c410e96972994b7ae0ef8", "【速通宝】您的验证码是{1}。如非本人操作，请忽略本短信");

    private final String code;

    private final String param;

    private SmsTemplate(String code, String param) {
        this.code = code;
        this.param = param;
    }

    public String getCode() {
        return code;
    }

    public String getParam() {
        return param;
    }

}
