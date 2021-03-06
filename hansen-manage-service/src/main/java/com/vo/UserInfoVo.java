package com.vo;

import com.BaseModel;

import java.io.Serializable;
import java.util.Date;


public class UserInfoVo extends BaseModel implements Serializable {
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
    private Integer cardGrade;
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
     * 用户剩余可做任务次数
     */
    private Integer remainTaskNo;
    /**
     * 1:市场人员内部注册   2：用户自己注册
     */
    private Integer createType;

    /**
     * 冻结支付币的数量
     */
    private Double forzenPayAmt;
    /**
     * 冻结交易币的数量
     */
    private Double forzenTradeAmt;
    /**
     * 冻结股权币的数量
     */
    private Double forzenEquityAmt;
    /**
     * 充币钱包地址
     */

    private String inPayAddress;

    private String inTradeAddress;

    private String inEquityAddress;
    /**
     * 提币钱包地址
     */
    private String outPayAddress;

    private String outEquityAddress;

    private String outTradeAddress;

    private Integer levles;
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

    public Integer getCardGrade() {
        return cardGrade;
    }

    public void setCardGrade(Integer cardGrade) {
        this.cardGrade = cardGrade;
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

    public Double getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(Double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
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

    public Integer getRemainTaskNo() {
        return remainTaskNo;
    }

    public void setRemainTaskNo(Integer remainTaskNo) {
        this.remainTaskNo = remainTaskNo;
    }

    public Integer getCreateType() {
        return createType;
    }

    public void setCreateType(Integer createType) {
        this.createType = createType;
    }

    public Double getForzenPayAmt() {
        return forzenPayAmt;
    }

    public void setForzenPayAmt(Double forzenPayAmt) {
        this.forzenPayAmt = forzenPayAmt;
    }

    public Double getForzenTradeAmt() {
        return forzenTradeAmt;
    }

    public void setForzenTradeAmt(Double forzenTradeAmt) {
        this.forzenTradeAmt = forzenTradeAmt;
    }

    public Double getForzenEquityAmt() {
        return forzenEquityAmt;
    }

    public void setForzenEquityAmt(Double forzenEquityAmt) {
        this.forzenEquityAmt = forzenEquityAmt;
    }

    public String getInPayAddress() {
        return inPayAddress;
    }

    public void setInPayAddress(String inPayAddress) {
        this.inPayAddress = inPayAddress;
    }

    public String getInTradeAddress() {
        return inTradeAddress;
    }

    public void setInTradeAddress(String inTradeAddress) {
        this.inTradeAddress = inTradeAddress;
    }

    public String getInEquityAddress() {
        return inEquityAddress;
    }

    public void setInEquityAddress(String inEquityAddress) {
        this.inEquityAddress = inEquityAddress;
    }

    public String getOutPayAddress() {
        return outPayAddress;
    }

    public void setOutPayAddress(String outPayAddress) {
        this.outPayAddress = outPayAddress;
    }

    public String getOutEquityAddress() {
        return outEquityAddress;
    }

    public void setOutEquityAddress(String outEquityAddress) {
        this.outEquityAddress = outEquityAddress;
    }

    public String getOutTradeAddress() {
        return outTradeAddress;
    }

    public void setOutTradeAddress(String outTradeAddress) {
        this.outTradeAddress = outTradeAddress;
    }

    public Integer getLevles() {
        return levles;
    }

    public void setLevles(Integer levles) {
        this.levles = levles;
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
}
