package com.vo;

/**
 * 分享注册vo
 *
 * @date 2016年12月7日
 */
public class ShareRegisterUserVo {

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
    private String payword;


    /**
     * 登录密码
     */
    private String password;

    /**
     * 支付密码
     */
    private String confirmPayWord;


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

    /**
     * 邀请人uid
     */
    private Integer firstReferrer;

    /**
     * 接点人
     */
    private Integer contactUserId;

    /**
     * email
     */
    private String email ;
    /**
     * phone
     */
    private String phone ;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public String getPayword() {
        return payword;
    }

    public void setPayword(String payword) {
        this.payword = payword;
    }

    public String getConfirmPayWord() {
        return confirmPayWord;
    }

    public void setConfirmPayWord(String confirmPayWord) {
        this.confirmPayWord = confirmPayWord;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getFirstReferrer() {
        return firstReferrer;
    }

    public void setFirstReferrer(Integer firstReferrer) {
        this.firstReferrer = firstReferrer;
    }

    public Integer getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(Integer contactUserId) {
        this.contactUserId = contactUserId;
    }
}
