package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 注册码和邀请码表
 *
 * @date 2016年12月7日
 */
public class Grade extends BaseModel {

    private static final long serialVersionUID = 24145337309873387877L;
    /**
     * 等级编号  普通：0、 VIP：1、 一星合伙人：2、 二星合伙人：3、 三星合伙人：4、 四星合伙人：5、 五星合伙人
     */
    private Integer grade;
    /**
     * 总业绩达标条件
     */
    private Double sumPerformance;
    /**
     * 去除最大部门个数
     */
    private Integer removeNo;
    /**
     * 剩余部门达标业绩比例
     */
    private Double remainScale;
    /**
     * 此星级获取级差奖励比例
     */
    private Double rewardScale;

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Double getSumPerformance() {
        return sumPerformance;
    }

    public void setSumPerformance(Double sumPerformance) {
        this.sumPerformance = sumPerformance;
    }

    public Integer getRemoveNo() {
        return removeNo;
    }

    public void setRemoveNo(Integer removeNo) {
        this.removeNo = removeNo;
    }

    public Double getRemainScale() {
        return remainScale;
    }

    public void setRemainScale(Double remainScale) {
        this.remainScale = remainScale;
    }

    public Double getRewardScale() {
        return rewardScale;
    }

    public void setRewardScale(Double rewardScale) {
        this.rewardScale = rewardScale;
    }
}
