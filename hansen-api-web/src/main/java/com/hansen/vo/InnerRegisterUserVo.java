package com.hansen.vo;

/**
 * 内部注册vo
 *
 * @date 2016年12月7日
 */
public class InnerRegisterUserVo {

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 登录名称
     */
    private String loginName;
    /**
     * 性别：0：男，1：女，2：保密
     */
    private Integer sex;

    /**
     * 头像地址
     */
    private String headImgUrl;


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

    /**
     * 卡的级别
     */
    private Integer cardGrade;
    /**
     * 邀请人uid
     */
    private Integer uid;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
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


    public Integer getCardGrade() {
        return cardGrade;
    }

    public void setCardGrade(Integer cardGrade) {
        this.cardGrade = cardGrade;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
