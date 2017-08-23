package com.model;

import com.BaseModel;

/**
 * 任务清单表
 *
 * @date 2017年08月15日
 */
public class Task extends BaseModel {

    private static final long serialVersionUID = -2932280541213916052L;

    /**
     * 任务标题
     */
    private String title;
    /**
     * 任务描述
     */
    private String discription;
    /**
     * 完成任务可获取奖励次数
     */
    private Integer rewardNo;
    /**
     * 任务链接
     */
    private String link;
    /**
     * 任务类型
     */
    private Integer taskType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public Integer getRewardNo() {
        return rewardNo;
    }

    public void setRewardNo(Integer rewardNo) {
        this.rewardNo = rewardNo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Integer getTaskType() {
        return taskType;
    }

    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }
}
