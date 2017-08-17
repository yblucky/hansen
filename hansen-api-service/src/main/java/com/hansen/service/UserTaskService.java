package com.hansen.service;

import com.base.service.CommonService;
import com.model.Task;
import com.model.UserTask;

import java.util.Date;
import java.util.List;

/**
 * @date 2017年08月15日
 */
public interface UserTaskService extends CommonService<UserTask> {
    Boolean addUserTask(String userId, String taskId, String title, String discription , String link, Date assignTaskTime, Integer status , String taskType, String rewardNo);

    Boolean addUserTask(String userId, Task task,Date assignTime);

    UserTask readLastOne(String userId);

    Boolean  assignUserTask(String userId);

    Boolean  doTask(String userId,String taskId);


}
