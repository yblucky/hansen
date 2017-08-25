package com.vo;

import java.io.Serializable;

/**
 *
 * @date 2017年2月4日
 */
public class ForgetPwdVo implements Serializable {

	private static final long serialVersionUID = 8315942462674630954L;

	private String phoneNumber;
	private String phoneCode;
	private String newPassWord;
	private String confirmPassWord;

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneCode() {
		return phoneCode;
	}

	public void setPhoneCode(String phoneCode) {
		this.phoneCode = phoneCode;
	}

	public String getNewPassWord() {
		return newPassWord;
	}

	public void setNewPassWord(String newPassWord) {
		this.newPassWord = newPassWord;
	}

	public String getConfirmPassWord() {
		return confirmPassWord;
	}

	public void setConfirmPassWord(String confirmPassWord) {
		this.confirmPassWord = confirmPassWord;
	}
}
