package com.model;

import com.BaseModel;

/**
 * 注册码和邀请码表
 *
 * @date 2016年12月7日
 */
public class CardGrade extends BaseModel {

    private static final long serialVersionUID = 2417309879839887L;
    /**
     * 卡等级编号  普通：1、 普卡	2 铜卡	3 银卡	4金卡	 5钻石卡
     */
    private Integer grade;
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
}
