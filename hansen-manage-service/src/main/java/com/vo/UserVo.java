package com.vo;

import com.constant.CardLevelType;
import com.constant.GradeType;
import com.constant.UserStatusType;
import com.model.User;
import com.model.UserDetail;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;


public class UserVo  extends User {
    private Integer uid;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邀请人uid
     */
    private String firstReferrer;
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;
    @JsonIgnore
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date stopTime;

    private Integer levles;
    private String gradeName;
    private Integer grade;

    private Integer loginName;
    /**
     * 当前保单卡等级
     */
    private Integer cardGrade;
    /**
     * 当前保单卡等级
     */
    private String cardGradeName;
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
     * 激活码个数
     */
    private Integer activeCodeNo;
    /**
     * 注册码个数
     */
    private Integer registerCodeNo;
    /**
     * 状态
     */
    protected Integer status;
    /**
     * 状态
     */
    protected String statusName;

    private UserDetail userDetail;


    /**
     * '收货人'
     */
    private String receiver;
    /**
     * '收货人手机号
     */
    private String receiverPhone;
    /**
     * ''收货地址''
     */
    private String shopAddr;
    /**
     * '用户名'
     */
    private String userName;
    /**
     * '银行名称'
     */
    private String bankName;
    /**
     * '银行所属型'
     */
    private String bankType;
    /**
     * 银行卡号
     */
    private String bankCardNo;
    private String confirmPassword;
    private String confirmPayWord;

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFirstReferrer() {
        return firstReferrer;
    }

    public void setFirstReferrer(String firstReferrer) {
        this.firstReferrer = firstReferrer;
    }

    public Date getFromTime() {
        return fromTime;
    }

    public void setFromTime(Date fromTime) {
        this.fromTime = fromTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public Integer getLevles() {
        return levles;
    }

    public void setLevles(Integer levles) {
        this.levles = levles;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
        this.setGradeName(GradeType.getName(this.getGrade()));

    }


    public Integer getCardGrade() {
        return cardGrade;
    }

    public void setCardGrade(Integer cardGrade) {
        this.cardGrade = cardGrade;
        this.setCardGradeName(CardLevelType.getName(this.cardGrade));
    }

    public String getCardGradeName() {
        return cardGradeName;
    }

    public void setCardGradeName(String cardGradeName) {
        this.cardGradeName = cardGradeName;
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

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
        this.setStatusName(UserStatusType.getName(this.status));
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankCardNo() {
        return bankCardNo;
    }

    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmPayWord() {
        return confirmPayWord;
    }

    public void setConfirmPayWord(String confirmPayWord) {
        this.confirmPayWord = confirmPayWord;
    }
}
