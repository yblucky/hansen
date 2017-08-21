package com.hansen.vo;

/**
 * 用户任务
 * Created  on 2018/08/17.
 */
public class TaskVo {
    private String taskId;
    private Integer status;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}
