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
     * 用户ID
     */
    private String userId;
    /**
     * 币的数量
     */
    private Double num;
    /**
     * 冻结币的数量
     */
    private Double forzenNum;
    /**
     * 充币钱包地址类型  1 交易币  2 支付币  3 股权币
     */
    private String currencyType;
    /**
     * 充币钱包地址
     */
    private Integer inAddress;
    /**
     * 提币钱包地址
     */
    private String outAddress;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Double getNum() {
        return num;
    }

    public void setNum(Double num) {
        this.num = num;
    }

    public Double getForzenNum() {
        return forzenNum;
    }

    public void setForzenNum(Double forzenNum) {
        this.forzenNum = forzenNum;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public Integer getInAddress() {
        return inAddress;
    }

    public void setInAddress(Integer inAddress) {
        this.inAddress = inAddress;
    }

    public String getOutAddress() {
        return outAddress;
    }

    public void setOutAddress(String outAddress) {
        this.outAddress = outAddress;
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
