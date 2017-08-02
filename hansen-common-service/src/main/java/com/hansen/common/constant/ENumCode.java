package com.hansen.common.constant;

public enum ENumCode {
	SUCCESS(0,"成功"),
	ERROR(10000,"请求异常"),
	UNCHECKED(10000,"未确认"),
	CHECKING(10001,"确认中"),
	CHECKED(10002,"已确认");
	
	public int code;
	public String message;
	
	private ENumCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
