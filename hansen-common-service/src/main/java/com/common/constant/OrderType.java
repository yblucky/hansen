package com.common.constant;

/**
 * 订单类型枚举类 <br/>
 * 
 * @author zhuzh
 * @date 2016年11月15日
 */
public enum OrderType {
	PURCHASE(1, "直接购买"),

	CARTPAY(2, "购物车购买");

	private final Integer code;

	private final String msg;

	private OrderType(Integer code, String msg) {
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
