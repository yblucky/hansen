package com.model;

import com.BaseModel;

/**
 * 币种表
 *
 * @date 2016年12月7日
 */
public class WalletCurrency extends BaseModel {

    private static final long serialVersionUID = 24145309879839887L;
    /**
     * 币种名称
     */
    private String currencyName;
    /**
     * 币种类型 1：交易币  2 支付币   3 股权币
     */
    private String currencyType;

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }
}
