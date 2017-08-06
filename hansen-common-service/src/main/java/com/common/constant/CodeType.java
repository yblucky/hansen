package com.common.constant;

/**
 * 标准状态枚举类 <br/>
 * @date 2016年11月15日
 */
public enum CodeType {

	/** 1 */
	ACTIVATECODE(1, "激活码"),
	/** 已删除 */
	REGISTERCODE(2, "注册码");

	private final Integer code;

	private final String msg;

	private CodeType(Integer code, String msg) {
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
