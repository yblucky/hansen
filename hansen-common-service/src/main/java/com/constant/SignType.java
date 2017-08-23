package com.constant;

/**
 * 标准状态枚举类 <br/>
 * @date 2018年08月19日
 */
public enum SignType {

	/**
	 * 1.等待领取红包
	 */
	WAITING_SIGN(1, "等待领取红包"),
	/**
	 * 已领取红包
	 */
	SIGNED(2,  "已领取红包"),
	/**
	 * 红包过期
	 */
	EXPIRED(3,  "红包过期");

	private final Integer code;

	private final String msg;

	private SignType(Integer code, String msg) {
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
