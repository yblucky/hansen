package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 用户部门表
 *
 * @date 2016年12月7日
 */
public class BaseUserDetail extends BaseModel {

	private static final long serialVersionUID = 241453373209830052L;
	/** 用户ID */
	private String userId;
	/** 钱包地址类型 1 交易币  2 支付币  3 股权币 */
	private Integer addressType;
	/** 钱包地址 */
	private String equityAddress;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getAddressType() {
		return addressType;
	}

	public void setAddressType(Integer addressType) {
		this.addressType = addressType;
	}

	public String getEquityAddress() {
		return equityAddress;
	}

	public void setEquityAddress(String equityAddress) {
		this.equityAddress = equityAddress;
	}
}
