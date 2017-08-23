package com.vo;

/**
 * Created by zzwei on 2017/8/3.
 */
public class CoinTransferVo {

    private Integer toUid;
    private Double amount;
    private Integer walletOrderType;
    private String payPassWord;

    public Integer getToUid() {
        return toUid;
    }

    public void setToUid(Integer toUid) {
        this.toUid = toUid;
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
