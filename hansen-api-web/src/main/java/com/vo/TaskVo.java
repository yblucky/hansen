package com.vo;

/**
 * 用户点击任务
 * Created by Administrator on 2018/08/17.
 */
public class TaskVo {

    private String userTaskId;
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getUserTaskId() {
        return userTaskId;
    }

    public void setUserTaskId(String userTaskId) {
        this.userTaskId = userTaskId;
    }
}
