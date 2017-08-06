package com.model;

import com.common.BaseModel;

/**
 * 用户表
 *
 * @date 2016年12月7日
 */
public class UserGradeRecord extends BaseModel {

    private static final long serialVersionUID = -23391605233L;
    /**
     * '使用人id'
     */
    private String userId;
    /**
     * 流水类型：1、会员卡等级变更  2 会员星级变更
     */
    private Integer recordType;
    /**
     * 历史等级
     */
    private Integer historyGrade;
    /**
     * 当前等级
     */
    private Integer currencyGrade;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getRecordType() {
        return recordType;
    }

    public void setRecordType(Integer recordType) {
        this.recordType = recordType;
    }

    public Integer getHistoryGrade() {
        return historyGrade;
    }

    public void setHistoryGrade(Integer historyGrade) {
        this.historyGrade = historyGrade;
    }

    public Integer getCurrencyGrade() {
        return currencyGrade;
    }

    public void setCurrencyGrade(Integer currencyGrade) {
        this.currencyGrade = currencyGrade;
    }
}
