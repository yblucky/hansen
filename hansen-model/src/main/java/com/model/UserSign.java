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
    /***预计签到时间**/
    private Date preSignTime;
    /***实际签到时间**/
    private Date signTime;
    /**
     * 本次签到领取金额：人民币，精确到分
     */
    private Double amt;

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
}
