package com.model;

import com.common.BaseModel;

/**
 * 交易流水表
 *
 * @date 2016年12月7日
 */
public class TransferCode extends BaseModel {

    private static final long serialVersionUID = 24145309879839887L;
    /**
     * 转账人
     */
    private String sendUserId;
    /**
     * 接收人
     */
    private String receviceUserId;
    /**
     * 1:激活码 2 注册码
     */
    private Integer type;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 转账数量
     */
    private Integer transferNo;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }
}

