package com.hansen.vo;

/**
 * 用户登录信息
 */
public class LoginMvo {
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码key
     */
    private String imgKey;
    /**
     * 验证码
     */
    private String picCode;

    /**
     * @return the imgKey
     */
    public String getImgKey() {
        return imgKey;
    }

    /**
     * @param imgKey the imgKey to set
     */
    public void setImgKey(String imgKey) {
        this.imgKey = imgKey;
    }

    /**
     * @return the picCode
     */
    public String getPicCode() {
        return picCode;
    }

    /**
     * @param picCode the picCode to set
     */
    public void setPicCode(String picCode) {
        this.picCode = picCode;
    }

    /**
     * @return the loginName
     */
    public String getLoginName() {
        return loginName;
    }

    /**
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
