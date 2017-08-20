package com.common.constant;

/**
 * redis相关key
 *
 * @date 2017年08月07日
 */
public enum RedisKey {

    /**
     * test
     */
    USER_TEST("user_test_", 1 * 60 * 60),
    /**
     * 系统商品待发货
     */
    SYSTEM_EXPRESS_GOODS("system_express_goods_", 20),
    /**
     * 管理端Token，保存1天
     */
    BOSS_TOKEN_API("boss_token_key", 24 * 60 * 60),
    /**
     * Token，保存1天
     */
    TOKEN_API("token_api_", 24 * 60 * 60),
    /**
     * 系统参数更新标致(保存72小时)
     */
    SYS_PARAM_UPDTOKEN("sys_param_updtoken", 72 * 60 * 60),
    /**
     * 短信验证码，保存10分钟
     */
    SMS_CODE("sms_code_", 10 * 60),;

    private final String key;

    private final int seconds;

    private RedisKey(String key, int seconds) {
        this.key = key;
        this.seconds = seconds;
    }

    public String getKey() {
        return key;
    }

    public int getSeconds() {
        return seconds;
    }
}
