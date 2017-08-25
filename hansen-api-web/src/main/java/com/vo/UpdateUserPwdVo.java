package com.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2016年12月12日
 */
public class UpdateUserPwdVo implements Serializable {

    private static final long serialVersionUID = -8261886280598614119L;

    private String oldPassWord;
    private String  newPassWord; //新密码
    private String confirmPassWord; //确认密码
    private String pwdType;     //选择方式
    private String oldPayWord;//支付密码
    private String newPayWord; //新支付密码
    private String confirmPayWord; //确认支付密码
    private String picCode;
    private String picKey;

    public String getOldPassWord() {
        return oldPassWord;
    }

    public void setOldPassWord(String oldPassWord) {
        this.oldPassWord = oldPassWord;
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

    public String getPwdType() {
        return pwdType;
    }

    public void setPwdType(String pwdType) {
        this.pwdType = pwdType;
    }

    public String getOldPayWord() {
        return oldPayWord;
    }

    public void setOldPayWord(String oldPayWord) {
        this.oldPayWord = oldPayWord;
    }

    public String getNewPayWord() {
        return newPayWord;
    }

    public void setNewPayWord(String newPayWord) {
        this.newPayWord = newPayWord;
    }

    public String getConfirmPayWord() {
        return confirmPayWord;
    }

    public void setConfirmPayWord(String confirmPayWord) {
        this.confirmPayWord = confirmPayWord;
    }

    public String getPicCode() {
        return picCode;
    }

    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }

    public String getPicKey() {
        return picKey;
    }

    public void setPicKey(String picKey) {
        this.picKey = picKey;
    }
}
