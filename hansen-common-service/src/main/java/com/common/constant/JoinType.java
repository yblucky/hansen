package com.common.constant;

/**
 * 店铺状态枚举类
 * @date 2016年11月15日
 */
public enum JoinType {

	/** 待审核 */
	PENDING(0, "待审核"),

	/** 审核通过 */
	FINISH(1, "审核通过"),

	/** 审核不通过 */
	FAILURE(2, "审核不通过"),

	/** 关闭 */
	CLOSED(3, "关闭");

	private final Integer code;

	private final String msg;

	private JoinType(Integer code, String msg) {
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
