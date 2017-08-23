package com.constant;

/**
 * 用户任务状态 <br/>
 *
 * @date 2016年11月15日
 */
public enum TaskStatusType {

    /**
     * 任务已分配
     */
    PENDING(1, "任务已分配"),
    /**
     * 任务已完成
     */
    HANDLED(2, "任务已完成"),
    /**
     * 任务已失效
     */
    CANNELED(3, "任务已失效");

    private final Integer code;

    private final String msg;

    private TaskStatusType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static TaskStatusType fromCode(Integer code) {
        try {
            for (TaskStatusType taskStatusType : TaskStatusType.values()) {
                if (taskStatusType.getCode().intValue() == code.intValue()) {
                    return taskStatusType;
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
