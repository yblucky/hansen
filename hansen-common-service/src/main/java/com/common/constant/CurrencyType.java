package com.common.constant;

/**
 * @date 2017年08月06日
 */
public enum CurrencyType {

	/** 1  交易币 */
	TRADE(1, "交易币"),
	/** 支付币 */
	PAY(2, "支付币"),
	EQUITY(3, "股权币");


	private final Integer code;

	private final String msg;

	private CurrencyType(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public static CurrencyType fromCode(Integer code) {
		try {
			for(CurrencyType currencyType : CurrencyType.values()){
				if(currencyType.getCode().intValue() == code.intValue()){
					return currencyType;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
}