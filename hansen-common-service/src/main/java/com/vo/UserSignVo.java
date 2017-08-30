package com.vo;

import com.constant.SignType;
import com.model.UserSign;

/**
 * 用户表红包签到
 * @author qsy
 * @version v1.0
 * @date 2016年11月27日
 */
public class UserSignVo extends UserSign{
	 private double payAmt;
	 private double tradeAmt;
	 private double equityAmt;

	private double payAmtRmb;
	private double  tradeAmtRmb;
	private double  equityAmtRmb;

	private Integer status;
	private String statusName;

	/**
	 * 用户姓名
	 */
	private String nickName;


	public double getPayAmt() {
		return payAmt;
	}

	public void setPayAmt(double payAmt) {
		this.payAmt = payAmt;
	}

	public double getTradeAmt() {
		return tradeAmt;
	}

	public void setTradeAmt(double tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	public double getEquityAmt() {
		return equityAmt;
	}

	public void setEquityAmt(double equityAmt) {
		this.equityAmt = equityAmt;
	}

	public double getPayAmtRmb() {
		return payAmtRmb;
	}

	public void setPayAmtRmb(double payAmtRmb) {
		this.payAmtRmb = payAmtRmb;
	}

	public double getTradeAmtRmb() {
		return tradeAmtRmb;
	}

	public void setTradeAmtRmb(double tradeAmtRmb) {
		this.tradeAmtRmb = tradeAmtRmb;
	}

	public double getEquityAmtRmb() {
		return equityAmtRmb;
	}

	public void setEquityAmtRmb(double equityAmtRmb) {
		this.equityAmtRmb = equityAmtRmb;
	}

	@Override
	public Integer getStatus() {
		return status;
	}

	@Override
	public void setStatus(Integer status) {
		this.status = status;
		this.setStatusName(SignType.getName(status));
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
}
