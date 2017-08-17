package com.common.constant;

/**
 * 标准状态枚举类 <br/>
 * @date 2016年11月15日
 */
public enum UserType {

	/**
	 * 1.市场人员内部注册
	 */
	INNER(1, "市场人员内部注册"),
	/**
	 * 用户自主注册
	 */
	OUT(2,  "用户自主注册");

	private final Integer code;

	private final String msg;

	private UserType(Integer code, String msg) {
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
