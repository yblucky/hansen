package com.constant;

/**
 * 性别枚举类
 * @date 2016年11月15日
 */
public enum SexType {

	/** 男 */
	male(1, "男"),

	/** 女 */
	female(2, "女"),

	/** 保密 */
	other(3, "保密");

	private final Integer code;

	private final String msg;

	private SexType(Integer code, String msg) {
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
