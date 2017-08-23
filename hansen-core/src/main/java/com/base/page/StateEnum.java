package com.base.page;

/**
 * 数据是否有效
 */
public enum StateEnum {
	NORMAL("10","正常"),
	DISABLE("20","禁用");
	private String code;
	private String name;
	
	private StateEnum(String code, String name) {
		this.name = name;
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public String getName() {
		return name;
	}
	
	public static String getName(String code){
		if (code == null) {
			return "";
		}
		for(StateEnum enums : StateEnum.values()){
			if (enums.getCode().equals(code)) {
				return enums.getName();
			}
		}
		return code;
	}
}
