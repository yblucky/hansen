package com.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @date 2016年12月12日
 */
public class UserVo implements Serializable {

    private static final long serialVersionUID = -8261886280598614119L;

    private String token;

    private String id;
    /**
     * UID
     */
    private Integer uid;
    /**
     * 登录名称
     */
    private String loginName;
    /**
     * 真实姓名
     */
    private String userName;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 性别：0：男，1：女，2：保密
     */
    private Integer sex;
    /**
     * 头像地址
     */
    private String headImgUrl;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 备注
     */
    private String remark;



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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
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
}
