package com.vo;

import com.constant.SignType;
import com.model.UserSign;

/**
 * 用户表红包签到
 *
 * @author qsy
 * @version v1.0
 * @date 2016年11月27日
 */
public class UserDetailVo extends UserSign {
    private static final long serialVersionUID = 241453373209830876L;

    private String id;
    private Integer uid;
    private String nickName;
    private String phone;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
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
}
