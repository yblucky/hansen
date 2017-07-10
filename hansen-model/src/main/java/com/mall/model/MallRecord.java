package com.mall.model;

import com.mall.common.BaseModel;

/**
 * @date 2016年12月7日
 */
public class MallRecord extends BaseModel {

	private static final long serialVersionUID = -8279693845246343459L;
	/** 订单号 */
	private String orderNo;
	/** 用户Id */
	private String userId;
	/** 价格 **/
	private Double price;
	/** 流水类型*/
	private Integer type;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}
}
