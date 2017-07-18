package com.api.model;

import com.api.common.BaseModel;

/**
 * 交易流水表
 *
 * @date 2016年12月7日
 */
public class TradeRecord extends BaseModel {

    private static final long serialVersionUID = 24145309879839887L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 交易数量
     */
    private Double amount;
    /**
     * 流水类型：1、扣除账户交易币   2、 扣除账户支付币  3、直推奖  4、管理奖  5、级差奖    6、平级奖   7、每周收益'
     */
    private Integer recordType;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }
}
