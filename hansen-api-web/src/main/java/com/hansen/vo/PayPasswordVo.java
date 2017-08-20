package com.hansen.vo;

import java.io.Serializable;

/**
 * @date 2017年08月19日
 */
public class PayPasswordVo implements Serializable {

    private static final long serialVersionUID = 8315942462674630954L;

    private String phone;
    private String smsCode;
    private String newPayPass;
    private String newPayPassConfirm;
    /**
     *
     * 一次提交  validType=1
     *
     * 兼容分两次校验，第一次只校验手机号和验证码，通过后才输入新密码和确认新密码 此时 validType=2
     */
    private Integer   validType;
    public String getSmsCode() {
        return smsCode;
    }

    public void setSmsCode(String smsCode) {
        this.smsCode = smsCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNewPayPass() {
        return newPayPass;
    }

    public void setNewPayPass(String newPayPass) {
        this.newPayPass = newPayPass;
    }

    public String getNewPayPassConfirm() {
        return newPayPassConfirm;
    }

    public void setNewPayPassConfirm(String newPayPassConfirm) {
        this.newPayPassConfirm = newPayPassConfirm;
    }

    public Integer getValidType() {
        return validType;
    }

    public void setValidType(Integer validType) {
        this.validType = validType;
    }
}
