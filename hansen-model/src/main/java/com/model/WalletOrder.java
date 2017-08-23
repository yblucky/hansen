package com.model;

import com.BaseModel;

/**
 * 钱包提币转币订单表
 *
 * @date 2016年12月7日
 */
public class WalletOrder extends BaseModel {

    private static final long serialVersionUID = -2932243349345052L;
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
     * 交易金额
     */
    private Double amount;
    /**
     * 实际到账金额
     */
    private Double confirmAmt;
    /**
     * 手续费
     */
    private Double poundage;
    /**
     * 订单类型 1：用户充币  2 ：用户提币
     */
    private Integer orderType;

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

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
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

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }
}
