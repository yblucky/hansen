package com.vo;

/**
 * 内部注册vo
 *
 * @date 2016年12月7日
 */
public class OutActiveUserVo {
    /**
     * 登录名称
     */
    private String loginName;
    /**
     * 性别：0：男，1：女，2：保密
     */
    private Integer sex;
    /**
     * 支付密码
     */
    private String payWord;
    /**
     * 登录密码
     */
    private String password;

    /**
     * 支付密码
     */
    private String confirmpayWord;


    /**
     * 登录密码
     */
    private String confirmPassword;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getPayWord() {
        return payWord;
    }

    public void setPayWord(String payWord) {
        this.payWord = payWord;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmpayWord() {
        return confirmpayWord;
    }

    public void setConfirmpayWord(String confirmpayWord) {
        this.confirmpayWord = confirmpayWord;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
