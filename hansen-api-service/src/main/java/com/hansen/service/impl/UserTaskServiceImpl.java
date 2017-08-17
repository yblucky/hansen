package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.common.constant.TaskStatusType;
import com.common.constant.UserStatusType;
import com.common.utils.DateUtils.DateTimeUtil;
import com.common.utils.DateUtils.DateUtils;
import com.common.utils.numberutils.RandomUtil;
import com.hansen.mapper.UserTaskMapper;
import com.hansen.service.TaskService;
import com.hansen.service.UserService;
import com.hansen.service.UserTaskService;
import com.model.Task;
import com.model.User;
import com.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @date 2016年11月27日
 */
@Service
public class UserTaskServiceImpl extends CommonServiceImpl<UserTask> implements UserTaskService {
    @Autowired
    private UserTaskMapper userTaskMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;

    @Override
    protected CommonDao<UserTask> getDao() {
        return userTaskMapper;
    }

    @Override
    protected Class<UserTask> getModelClass() {
        return UserTask.class;
    }

    @Override
    public Boolean addUserTask(String userId, String taskId, String title, String discription, String link, Date assignTaskTime, Integer status, String taskType, String rewardNo) {
        UserTask userTask = new UserTask();
        userTask.setUserId(userId);
        userTask.setTaskId(taskId);
        userTask.setTitle(title);
        userTask.setDiscription(discription);
        userTask.setLink(link);
        userTask.setAssignTaskTime(assignTaskTime);
        userTask.setStatus(TaskStatusType.PENDING.getCode());
        userTask.setRemark(TaskStatusType.PENDING.getMsg());
        userTask.setTaskType(1);
        userTask.setRewardNo(1);
        this.create(userTask);
        return true;
    }

    @Override
    public Boolean addUserTask(String userId, Task task, Date assignTime) {
        UserTask userTask = new UserTask();
        userTask.setUserId(userId);
        userTask.setTaskId(task.getId());
        userTask.setTitle(task.getTitle());
        userTask.setDiscription(task.getDiscription());
        userTask.setLink(task.getLink());
        userTask.setAssignTaskTime(new Date());
        userTask.setStatus(TaskStatusType.PENDING.getCode());
        userTask.setRemark(TaskStatusType.PENDING.getMsg());
        userTask.setTaskType(1);
        userTask.setRewardNo(1);
        userTask.setAssignTaskTime(assignTime);
        this.create(userTask);
        return true;
    }

    @Override
    public UserTask readLastOne(String userId) {
        return userTaskMapper.readLastOne(userId);
    }

    @Override
    @Transactional
    public Boolean assignUserTask(String userId) {
        //判断是否要给用户生成任务
        User user = userService.readById(userId);
        if (user == null) {
            return false;
        }
        Integer remainTaskNo = user.getRemainTaskNo();
        if (remainTaskNo == 0) {
            return false;
        }
        UserTask lastUserTask = this.readLastOne(userId);
        if (lastUserTask == null) {
            //从来都没有领取过任务，默认第一天
            Set<String> ids = new HashSet<>();
            String taskId = RandomUtil.getRandomElement(ids);
            Task firstTask = taskService.readById(taskId);
            this.addUserTask(userId, firstTask, new Date());
        } else {
            Integer diffDay = DateUtils.differentDaysByMillisecond(lastUserTask.getAssignTaskTime(), new Date());
            if (diffDay > remainTaskNo) {
                diffDay = remainTaskNo;
            }
            for (int i = 0; i < diffDay; i++) {
                Set<String> ids = new HashSet<>();
                String taskId = RandomUtil.getRandomElement(ids);
                Task task = taskService.readById(taskId);
                task = taskService.readById(taskId);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(lastUserTask.getAssignTaskTime());
                calendar.add(Calendar.DAY_OF_MONTH, (i + 1));
                Date assignTime = calendar.getTime();
                this.addUserTask(userId, task, assignTime);
            }
        }
        return true;
    }

    @Override
    @Transactional
    public Boolean doTask(String userId, String taskId) {
        User user = userService.readById(userId);
        if (user == null) {
            return false;
        }
        if (user.getRemainTaskNo() <= 0) {
            return false;
        }
        UserTask conditon = new UserTask();
        conditon.setUserId(userId);
        conditon.setTaskId(taskId);
        UserTask userTask = this.readOne(conditon);
        if (userTask == null) {
            return false;
        }
        if (TaskStatusType.PENDING.getCode() != userTask.getStatus()) {
            return false;
        }
        if (userTask.getCreateTime().getTime() > System.currentTimeMillis()) {
            return false;
        }
        UserTask updteModel = new UserTask();
        updteModel.setStatus(TaskStatusType.HANDLED.getCode());
        updteModel.setId(userTask.getId());
        updteModel.setRemark("用户于" + DateTimeUtil.formatDate(new Date(), DateTimeUtil.PATTERN_LONG) + "完成任务");
        this.updateById(userTask.getId(), updteModel);
        userService.updateRemainTaskNoByUserId(userId, -1);
        if (user.getRemainTaskNo()==1){
            //冻结用户账号，等待下次激活
            userService.updateUserStatus(userId, UserStatusType.OUT.getCode());
        }
        return true;
    }
}
