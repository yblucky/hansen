package com.api.model;

import com.api.common.BaseModel;

import java.util.Date;

/**
 * 用户表
 *
 * @date 2016年12月7日
 */
public class User extends BaseModel {

    private static final long serialVersionUID = -2932280543213916052L;
    /**
     * UID
     */
    private Integer uid;
    /**
     * 微信 unionId
     */
    private String unionId;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 帐号
     */
    private String loginName;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 邮箱
     */
    private Double amount;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别：0：男，1：女，2：保密
     */
    private Integer sex;
    /**
     * 盐
     */
    private String salt;
    /**
     * 头像地址
     */
    private String headImgUrl;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 详细地址
     */
    private String addr;

    /***登录时间**/
    private Date loginTime;
    /***微信openid**/
    private String openId;

    /**
     * 支付密码
     */
    private String payWord;
    /**
     * 登陆密码
     */
    private String password;
    /**
     * 一度推荐人
     */
    private String firstReferrer;
    /**
     * 二度推荐人
     */
    private String secondReferrer;
    /**
     * 星级
     */
    private Integer grade;
    /**
     * 当前保单卡等级
     */
    private Integer cardLevel;
    /**
     * 释放时间
     */
    private String releaseTime;
    /**
     * 股权币
     */
    private Double equityAmt;
    /**
     * 支付币
     */
    private Double payAmt;
    /**
     * 交易币
     */
    private Double tradeAmt;
    /**
     * 剩余保单金额
     */
    private Double insuranceAmt;
    /**
     * 预期最大收益
     */
    private Double maxProfits;
    /**
     * 累计收益
     */
    private Double sumProfits;
    /**
     * 累计提现收益
     */
    private Double cashOutProfits;
    /**
     * 接点人
     */
    private String contactUserId;
    /**
     * 激活码个数
     */
    private Integer activeCodeNo;
    /**
     * 注册码个数
     */
    private Integer registerCodeNo;
    /**
     * 所属层级
     */
    private Integer levles;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
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

    public String getFirstReferrer() {
        return firstReferrer;
    }

    public void setFirstReferrer(String firstReferrer) {
        this.firstReferrer = firstReferrer;
    }

    public String getSecondReferrer() {
        return secondReferrer;
    }

    public void setSecondReferrer(String secondReferrer) {
        this.secondReferrer = secondReferrer;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getCardLevel() {
        return cardLevel;
    }

    public void setCardLevel(Integer cardLevel) {
        this.cardLevel = cardLevel;
    }

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public Double getEquityAmt() {
        return equityAmt;
    }

    public void setEquityAmt(Double equityAmt) {
        this.equityAmt = equityAmt;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Double getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(Double tradeAmt) {
        this.tradeAmt = tradeAmt;
    }

    public Double getMaxProfits() {
        return maxProfits;
    }

    public void setMaxProfits(Double maxProfits) {
        this.maxProfits = maxProfits;
    }

    public Double getSumProfits() {
        return sumProfits;
    }

    public void setSumProfits(Double sumProfits) {
        this.sumProfits = sumProfits;
    }

    public Double getCashOutProfits() {
        return cashOutProfits;
    }

    public void setCashOutProfits(Double cashOutProfits) {
        this.cashOutProfits = cashOutProfits;
    }

    public String getContactUserId() {
        return contactUserId;
    }

    public void setContactUserId(String contactUserId) {
        this.contactUserId = contactUserId;
    }

    public Integer getActiveCodeNo() {
        return activeCodeNo;
    }

    public void setActiveCodeNo(Integer activeCodeNo) {
        this.activeCodeNo = activeCodeNo;
    }

    public Integer getRegisterCodeNo() {
        return registerCodeNo;
    }

    public void setRegisterCodeNo(Integer registerCodeNo) {
        this.registerCodeNo = registerCodeNo;
    }

    public Integer getLevles() {
        return levles;
    }

    public void setLevles(Integer levles) {
        this.levles = levles;
    }

    public Double getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(Double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }
}
