package com.vo;

import java.io.Serializable;

/**
 * @date 2016年12月12日
 */
public class UpgradeUserVo implements Serializable {

    private static final long serialVersionUID = -826188614119L;
    //升级的方式
    private  Integer upGradeWay;
    //要升级的卡类等级
    private Integer grade;
    //支付密码
    private String payWord;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getPayWord() {
        return payWord;
    }

    public void setPayWord(String payWord) {
        this.payWord = payWord;
    }

    public Integer getUpGradeWay() {
        return upGradeWay;
    }

    public void setUpGradeWay(Integer upGradeWay) {
        this.upGradeWay = upGradeWay;
    }
}
