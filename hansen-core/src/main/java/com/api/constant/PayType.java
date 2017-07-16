package com.api.constant;

/**
 * 支付枚举类 <br/>
 * @date 2016年11月15日
 */
public enum PayType {

	ALIPAY(1, "直接购买"),

	WEIXINPAY(2, "微信支付"),

	BALANCEPAY(3, "会员卡支付");


	private final Integer code;

	private final String msg;

	private PayType(Integer code, String msg) {
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
