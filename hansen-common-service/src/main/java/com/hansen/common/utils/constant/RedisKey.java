package com.hansen.common.utils.constant;

/**
 * redis相关key
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum RedisKey {

	/** test */
	USER_TEST("user_test_",1*60*60),
	/**
	 * 系统商品待发货
	 */
	SYSTEM_EXPRESS_GOODS("system_express_goods_",20),
	/** 管理端Token，保存1天 */
	BOSS_TOKEN_API("boss_token_key", 24 * 60 * 60),
	/** Token，保存1天 */
	TOKEN_API("token_api_", 24 * 60 * 60),
	/**系统参数更新标致(保存72小时)*/
	SYS_PARAM_UPDTOKEN("sys_param_updtoken", 72*60*60);

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
