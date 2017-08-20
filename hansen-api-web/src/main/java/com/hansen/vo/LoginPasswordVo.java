package com.hansen.vo;

import java.io.Serializable;

/**
 * @date 2017年08月19日
 */
public class LoginPasswordVo implements Serializable {

	private static final long serialVersionUID = 8315942462674630954L;

	private String phone;
	private String smsCode;
	private String newLoginPass;
	private String newLoginPassConfirm;
	/**
	 *
	 * 一次提交  validType=1
	 *
	 * 兼容分两次校验，第一次只校验手机号和验证码，通过后才输入新密码和确认新密码 此时 validType=2
	 */
	private Integer   validType;
	/**
	 * @return the smsCode
	 */
	public String getSmsCode() {
		return smsCode;
	}
	/**
	 * @param smsCode the smsCode to set
	 */
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	/**
	 * @return the newLoginPass
	 */
	public String getNewLoginPass() {
		return newLoginPass;
	}
	/**
	 * @param newLoginPass the newLoginPass to set
	 */
	public void setNewLoginPass(String newLoginPass) {
		this.newLoginPass = newLoginPass;
	}
	/**
	 * @return the newLoginPassConfirm
	 */
	public String getNewLoginPassConfirm() {
		return newLoginPassConfirm;
	}
	/**
	 * @param newLoginPassConfirm the newLoginPassConfirm to set
	 */
	public void setNewLoginPassConfirm(String newLoginPassConfirm) {
		this.newLoginPassConfirm = newLoginPassConfirm;
	}
	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}
	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getValidType() {
		return validType;
	}

	public void setValidType(Integer validType) {
		this.validType = validType;
	}
}
