package com.mall.constant;

/**
 * 图片空间枚举类
 * @date 2016年11月15日
 */
public enum BucketType {

	/** 正式 **/
	MALL(0, "英树美妆"),
	/** test **/
	MALL_TEST(0, "英树美妆");

	private final Integer code;

	private final String msg;

	private BucketType(Integer code, String msg) {
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
