package com.common.constant;

/**
 * 订单枚举类
 * @date 2016年11月15日
 */
public enum OrderStatus {

	/** 待处理 */
	PENDING(0, "待处理"),

	/** 已出售 */
	SALED(1, "已出售"),

	/** 已购买，待发货 */
	BUYED(2, "待发货"),

	/** 已发货 */
	DELIVERED(3, "已发货");

	private final Integer code;

	private final String msg;

	private OrderStatus(Integer code, String msg) {
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
