package com.api.model;

import com.api.common.BaseModel;

/**
 * 用户表
 *
 * @date 2016年12月7日
 */
public class TradePerformanceRecord extends BaseModel {

    private static final long serialVersionUID = -2932239163052L;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 交易金额 此处是人民币 精确到分
     */
    private Double amt;
    /**
     * 来源： 1：保单
     */
    private Double source;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getSource() {
        return source;
    }

    public void setSource(Double source) {
        this.source = source;
    }
}
