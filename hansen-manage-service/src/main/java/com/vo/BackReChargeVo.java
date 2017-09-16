package com.vo;

/**
 * Created     on 2017/8/22.
 */
public class BackReChargeVo {
    private  Integer uid;
    private  Double  tradeAmt;
    private  Double payAmt;
    private  Double equityAmt;
    private  Double activeCodeNo;
    private  Double registerCodeNo;
    private  String supperPass;

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Double getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(Double tradeAmt) {
        this.tradeAmt = tradeAmt;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Double getEquityAmt() {
        return equityAmt;
    }

    public void setEquityAmt(Double equityAmt) {
        this.equityAmt = equityAmt;
    }

    public Double getActiveCodeNo() {
        return activeCodeNo;
    }

    public void setActiveCodeNo(Double activeCodeNo) {
        this.activeCodeNo = activeCodeNo;
    }

    public Double getRegisterCodeNo() {
        return registerCodeNo;
    }

    public void setRegisterCodeNo(Double registerCodeNo) {
        this.registerCodeNo = registerCodeNo;
    }

    public String getSupperPass() {
        return supperPass;
    }

    public void setSupperPass(String supperPass) {
        this.supperPass = supperPass;
    }
}
