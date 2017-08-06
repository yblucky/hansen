package com.model;

import com.common.BaseModel;

/**
 * 用户表
 *
 * @date 2016年12月7日
 */
public class TradeOrder extends BaseModel {

    private static final long serialVersionUID = -2932239345052L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 赠送用户Id
     */
    private String sendUserId;
    /**
     * 接收用户Id
     */
    private String receviceUserId;
    /**
     * 交易金额 此处是人民币 精确到分
     */
    private Double amt;
    /**
     * 实际到账金额
     */
    private Double confirmAmt;
    /**
     * 手续费
     */
    private Double poundage;
    /**
     * 来源： 1：保单，2、直推奖  3、管理奖  4、级差奖    5、平级奖   6、每周收益
     */
    private Integer source;
    /**
     * 一级分销
     */
    private Double firstReferrerScale;
    /**
     * 二级分销
     */
    private Double secondReferrerScale;
    /**
     * 股权币比例
     */
    private Double equityAmtScale;
    /**
     * 支付币比例
     */
    private Double payAmtScale;
    /**
     * 交易币比例
     */
    private Double tradeAmtScale;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getSendUserId() {
        return sendUserId;
    }

    public void setSendUserId(String sendUserId) {
        this.sendUserId = sendUserId;
    }

    public String getReceviceUserId() {
        return receviceUserId;
    }

    public void setReceviceUserId(String receviceUserId) {
        this.receviceUserId = receviceUserId;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getConfirmAmt() {
        return confirmAmt;
    }

    public void setConfirmAmt(Double confirmAmt) {
        this.confirmAmt = confirmAmt;
    }

    public Double getPoundage() {
        return poundage;
    }

    public void setPoundage(Double poundage) {
        this.poundage = poundage;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Double getFirstReferrerScale() {
        return firstReferrerScale;
    }

    public void setFirstReferrerScale(Double firstReferrerScale) {
        this.firstReferrerScale = firstReferrerScale;
    }

    public Double getSecondReferrerScale() {
        return secondReferrerScale;
    }

    public void setSecondReferrerScale(Double secondReferrerScale) {
        this.secondReferrerScale = secondReferrerScale;
    }

    public Double getEquityAmtScale() {
        return equityAmtScale;
    }

    public void setEquityAmtScale(Double equityAmtScale) {
        this.equityAmtScale = equityAmtScale;
    }

    public Double getPayAmtScale() {
        return payAmtScale;
    }

    public void setPayAmtScale(Double payAmtScale) {
        this.payAmtScale = payAmtScale;
    }

    public Double getTradeAmtScale() {
        return tradeAmtScale;
    }

    public void setTradeAmtScale(Double tradeAmtScale) {
        this.tradeAmtScale = tradeAmtScale;
    }
}
