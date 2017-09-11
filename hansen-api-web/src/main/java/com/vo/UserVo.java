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
     * phone
     */
    private String phone;
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
    private Double equityAmt=0d;
    /**
     * 支付币
     */
    private Double payAmt=0d;
    /**
     * 交易币
     */
    private Double tradeAmt=0d;
    /**
     * 剩余保单金额
     */
    private Double insuranceAmt=0d;
    /**
     * 预期最大收益
     */
    private Double maxProfits=0d;
    /**
     * 累计收益
     */
    private Double sumProfits=0d;
    /**
     * 动态奖金：做任务领取的：管理奖+直推奖+级差奖+平级奖
     */
    private Double dynamicProfits=0d;
    /**
     * 失效需要重新激活时候的冻结奖金
     */
    private Double sumFrozenProfits=0d;
    /**
     * 累计提现收益
     */
    private Double cashOutProfits=0d;
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
    private String email;
    private Integer status;

    private Double rmbConvertPayScale;
    private Double rmbConvertTradeScale;
    private Double payConverRmbScale;
    private Double tradeConverRmbScale;
    private Double tradeCoinOutScale;
    private Double payCoinOutScale;

    /**
     * '收货人'
     */
    private String receiver;
    /**
     * ''收货地址''
     */
    private String shopAddr;
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
    /**
     *
     * 收货人手机号
     *
     */
    private String receiverPhone;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getRmbConvertPayScale() {
        return rmbConvertPayScale;
    }

    public void setRmbConvertPayScale(Double rmbConvertPayScale) {
        this.rmbConvertPayScale = rmbConvertPayScale;
    }

    public Double getRmbConvertTradeScale() {
        return rmbConvertTradeScale;
    }

    public void setRmbConvertTradeScale(Double rmbConvertTradeScale) {
        this.rmbConvertTradeScale = rmbConvertTradeScale;
    }

    public Double getPayConverRmbScale() {
        return payConverRmbScale;
    }

    public void setPayConverRmbScale(Double payConverRmbScale) {
        this.payConverRmbScale = payConverRmbScale;
    }

    public Double getTradeConverRmbScale() {
        return tradeConverRmbScale;
    }

    public void setTradeConverRmbScale(Double tradeConverRmbScale) {
        this.tradeConverRmbScale = tradeConverRmbScale;
    }

    public Double getTradeCoinOutScale() {
        return tradeCoinOutScale;
    }

    public void setTradeCoinOutScale(Double tradeCoinOutScale) {
        this.tradeCoinOutScale = tradeCoinOutScale;
    }

    public Double getPayCoinOutScale() {
        return payCoinOutScale;
    }

    public void setPayCoinOutScale(Double payCoinOutScale) {
        this.payCoinOutScale = payCoinOutScale;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
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

    public Double getDynamicProfits() {
        return dynamicProfits;
    }

    public void setDynamicProfits(Double dynamicProfits) {
        this.dynamicProfits = dynamicProfits;
    }

    public Double getSumFrozenProfits() {
        return sumFrozenProfits;
    }

    public void setSumFrozenProfits(Double sumFrozenProfits) {
        this.sumFrozenProfits = sumFrozenProfits;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }
}
