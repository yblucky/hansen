package com.vo;

public class CardGradeVo {

    private static final long serialVersionUID = 2417309879839887L;
    /**
     * 卡等级编号  普通：1、   普卡	2 铜卡	3 银卡	4金卡	 5钻石卡
     */
    private Integer grade;
    /**
     * 需要支付币金额
     */
    private Double payAmt;
    /**
     * 需要交易币金额
     */
    private Double tradeAmt;
    /**
     * 需要股权币金额
     */
    private Double equityAmt;
    /**
     * 保单金额 单位是分
     */
    private Double insuranceAmt;
    /**
     * 释放比例
     */
    private Double releaseScale;
    /**
     * 激活码个数
     */
    private Integer activeCodeNo;
    /**
     * 注册码个数
     */
    private Integer registerCodeNo;
    /**
     * 动静出局倍数
     */
    private Integer outMultiple;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Double getInsuranceAmt() {
        return insuranceAmt;
    }

    public void setInsuranceAmt(Double insuranceAmt) {
        this.insuranceAmt = insuranceAmt;
    }

    public Double getReleaseScale() {
        return releaseScale;
    }

    public void setReleaseScale(Double releaseScale) {
        this.releaseScale = releaseScale;
    }

    public Integer getActiveCodeNo() {
        return activeCodeNo;
    }

    public void setActiveCodeNo(Integer activeCodeNo) {
        this.activeCodeNo = activeCodeNo;
    }

    public Integer getRegisterCodeNo() {
        return registerCodeNo;
    }

    public void setRegisterCodeNo(Integer registerCodeNo) {
        this.registerCodeNo = registerCodeNo;
    }

    public Integer getOutMultiple() {
        return outMultiple;
    }

    public void setOutMultiple(Integer outMultiple) {
        this.outMultiple = outMultiple;
    }

    public Double getPayAmt() {
        return payAmt;
    }

    public void setPayAmt(Double payAmt) {
        this.payAmt = payAmt;
    }

    public Double getTradeAmt() {
        return tradeAmt;
    }

    public void setTradeAmt(Double tradeAmt) {
        this.tradeAmt = tradeAmt;
    }

    public Double getEquityAmt() {
        return equityAmt;
    }

    public void setEquityAmt(Double equityAmt) {
        this.equityAmt = equityAmt;
    }
}
