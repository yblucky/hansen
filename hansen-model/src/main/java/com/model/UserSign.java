package com.model;

import com.BaseModel;

import java.util.Date;

/**
 * 用户表红包签到
 *
 * @date 2017年08月15日
 */
public class UserSign extends BaseModel {

    private static final long serialVersionUID = -2932280543213916052L;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户id
     */
    private Integer uid;
    /***预计签到时间**/
    private Date preSignTime;
    /***实际签到时间**/
    private Date signTime;
    /**
     * 本次签到领取金额：人民币，精确到分
     */
    private Double amt;
    /**
     * 当天人民币兑换股权币的汇率
     */
    private Double rmbCovertEquityScale;
    /**
     * 当天人民币兑换支付币的汇率
     */
    private Double rmbCovertPayAmtScale;
    /**
     * 当天人民币兑换交易币币的汇率
     */
    private Double rmbCovertTradeAmtScale;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getPreSignTime() {
        return preSignTime;
    }

    public void setPreSignTime(Date preSignTime) {
        this.preSignTime = preSignTime;
    }

    public Date getSignTime() {
        return signTime;
    }

    public void setSignTime(Date signTime) {
        this.signTime = signTime;
    }

    public Double getAmt() {
        return amt;
    }

    public void setAmt(Double amt) {
        this.amt = amt;
    }

    public Double getRmbCovertEquityScale() {
        return rmbCovertEquityScale==null || rmbCovertEquityScale==0?1d:rmbCovertEquityScale;
    }

    public void setRmbCovertEquityScale(Double rmbCovertEquityScale) {
        this.rmbCovertEquityScale = rmbCovertEquityScale;
    }

    public Double getRmbCovertPayAmtScale() {
        return rmbCovertPayAmtScale==null || rmbCovertPayAmtScale==0?1d:rmbCovertPayAmtScale;
    }

    public void setRmbCovertPayAmtScale(Double rmbCovertPayAmtScale) {
        this.rmbCovertPayAmtScale = rmbCovertPayAmtScale;
    }

    public Double getRmbCovertTradeAmtScale() {
        return rmbCovertTradeAmtScale==null || rmbCovertTradeAmtScale==0?1d:rmbCovertTradeAmtScale;
    }

    public void setRmbCovertTradeAmtScale(Double rmbCovertTradeAmtScale) {
        this.rmbCovertTradeAmtScale = rmbCovertTradeAmtScale;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }
}
