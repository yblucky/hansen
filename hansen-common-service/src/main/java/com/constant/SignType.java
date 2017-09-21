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

	public static String getName(Integer code){
		if (code == null) {
			return "";
		}
		for(SignType enums : SignType.values()){
			if (enums.getCode().equals(code)) {
				return enums.getMsg();
			}
		}
		return "";
	}

	public static SignType fromCode(Integer code) {
		try {
			for(SignType signType : SignType.values()){
				if(signType.getCode().intValue() == code.intValue()){
					return signType;
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}


	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

}
