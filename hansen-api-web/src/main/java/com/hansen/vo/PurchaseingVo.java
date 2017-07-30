package com.hansen.vo;

import java.io.Serializable;

/**
 * 待付订单购买vo
 *
 * @date 2017年2月4日
 */
public class PurchaseingVo implements Serializable {

	private static final long serialVersionUID = 1945539359683835123L;

	/** 订单号 */
	private String orderNo;
	/** 支付方式 */
	private Integer payPay;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getPayPay() {
		return payPay;
	}

	public void setPayPay(Integer payPay) {
		this.payPay = payPay;
	}
}
