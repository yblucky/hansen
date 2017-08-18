package com.hansen.vo;

/**
 * Created by zzwei on 2017/8/3.
 */
public class CoinInOutVo {

    private String address;
    private Double amount;
    private Integer walletOrderType;
    private String payPassWord;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getWalletOrderType() {
        return walletOrderType;
    }

    public void setWalletOrderType(Integer walletOrderType) {
        this.walletOrderType = walletOrderType;
    }

    public String getPayPassWord() {
        return payPassWord;
    }

    public void setPayPassWord(String payPassWord) {
        this.payPassWord = payPassWord;
    }
}
