package com.constant;

/**
 * 标准状态枚举类 <br/>
 * @date 2016年11月15日
 */
public enum StatusType {

	/** 1 */
	TRUE(1, "正常"),
	/** 已删除 */
	DEL(2, "已删除");

	private final Integer code;

	private final String msg;

	private StatusType(Integer code, String msg) {
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
