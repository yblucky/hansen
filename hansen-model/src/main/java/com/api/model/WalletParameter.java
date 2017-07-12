package com.api.model;

import com.api.common.BaseModel;

/**
 * 币种表
 *
 * @date 2016年12月7日
 */
public class WalletParameter extends BaseModel {

    private static final long serialVersionUID = 24145309879839887L;
    /**
     * 币种类型 1：交易币  2 支付币   3 股权币
     */
    private String currencyType;
    /**
     * 名
     */
    private String name;
    /**
     * 值
     */
    private String value;
    /**
     * 字段种类
     */
    private String kind;
    /**
     * 字段名
     */
    private String title;

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
