package com.model;

import com.common.BaseModel;

/**
 * 用户部门表
 *
 * @date 2016年12月7日
 */
public class UserDetail extends BaseModel {

    private static final long serialVersionUID = 241453373209830052L;
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

    private Integer levles;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 详细地址
     */
    private String addr;
 

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

    public Integer getLevles() {
        return levles;
    }

    public void setLevles(Integer levles) {
        this.levles = levles;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
