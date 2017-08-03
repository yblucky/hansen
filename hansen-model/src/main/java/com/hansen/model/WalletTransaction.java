package com.hansen.model;

import com.hansen.common.BaseModel;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 钱包提币转币订单表
 *
 * @date 2016年12月7日
 */
public class WalletTransaction extends BaseModel {

    private static final long serialVersionUID = -293223345052L;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 交易id
     */
    private String txtId;
    /**
     * 预付单号
     */
    private String prepayId;
    /**
     * 交易时间戳
     */
    private Long transactionLongTime;
    /**
     * 交易时间
     */
    private Date transactionTime;
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 交易类型
     */
    private String category;
    /**
     * 费用
     */
    private Double fee;
    /**
     * 确认个数
     */
    private Integer confirmations;
    /**
     * 交易状态
     */
    private String transactionStatus;
    /**
     * 交易状态
     */
    private String message;
    /**
     * 交易描述
     */
    private Map<String, Object> details;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTxtId() {
        return txtId;
    }

    public void setTxtId(String txtId) {
        this.txtId = txtId;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public Long getTransactionLongTime() {
        return transactionLongTime;
    }

    public void setTransactionLongTime(Long transactionLongTime) {
        this.transactionLongTime = transactionLongTime;
    }

    public Date getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(Date transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getFee() {
        return fee;
    }

    public void setFee(Double fee) {
        this.fee = fee;
    }

    public Integer getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Integer confirmations) {
        this.confirmations = confirmations;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(String transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getDetails() {
        return details;
    }

    public void setDetails(Map<String, Object> details) {
        this.details = details;
    }
}
