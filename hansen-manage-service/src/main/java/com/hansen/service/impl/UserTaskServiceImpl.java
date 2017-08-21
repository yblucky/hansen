package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.common.constant.SignType;
import com.common.constant.TaskStatusType;
import com.common.constant.UserStatusType;
import com.common.utils.DateUtils.DateTimeUtil;
import com.common.utils.DateUtils.DateUtils;
import com.common.utils.ParamUtil;
import com.common.utils.numberutils.RandomUtil;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.mapper.UserTaskMapper;
import com.hansen.service.*;
import com.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @date 2017年08月17日
 */
@Service
public class UserTaskServiceImpl extends CommonServiceImpl<UserTask> implements UserTaskService {
    @Autowired
    private UserTaskMapper userTaskMapper;
    @Autowired
    private TaskService taskService;
    @Autowired
    private UserService userService;
    @Autowired
    private TradeOrderService tradeOrderService;
    @Autowired
    private UserSignService userSignService;

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
    public Boolean doTask(String userId, String taskId) throws Exception {
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
        if (user.getRemainTaskNo() == 1) {
            //冻结用户账号，等待下次激活
            userService.updateUserStatus(userId, UserStatusType.OUT.getCode());
        }
        List<TradeOrder> orderList = tradeOrderService.readRewardList(new Date(), 1, 100);
        //需要更新任务次数的id集合
        List<String> orderIdList1 = new ArrayList<>();
        //需要更新领取奖励次数的id集合
        List<String> orderIdList2 = new ArrayList<>();
        List<TradeOrder> orderIdList21 = new ArrayList<>();
        //如果是最后一个周期，更新奖金订单状态为完成发放
        List<String> orderIdList3 = new ArrayList<>();

        for (TradeOrder order : orderList) {
            orderIdList1.add(order.getId());
            if (order.getTaskCycle() == 1) {
                orderIdList21.add(order);
                orderIdList2.add(order.getId());
            }
            if (order.getSignCycle()==1){
                orderIdList3.add(order.getId());
            }
        }
        if (ToolUtil.isNotEmpty(orderIdList1)){
            tradeOrderService.batchUpdateTaskCycle(orderIdList1);
        }
        if (ToolUtil.isNotEmpty(orderIdList2)){
            //完成7次奖励，一次奖励发放，任务周期归为系统设置默认值
            Integer taskCycle = Integer.valueOf(ParamUtil.getIstance().get(Parameter.TASKINTERVAL));
            tradeOrderService.batchUpdateTaskCycleDefault(orderIdList2,taskCycle);
            //逐条写入奖励发放记录
            for (TradeOrder order :orderIdList21){
                //TODO 一天若有奖金 需要合并，待完善
                userSignService.addUserSign(order.getReceviceUserId(),order.getAmt()/order.getSignCycle(), SignType.WAITING_SIGN,"完成一个任务周期，新增奖励发放记录");
            }
        }
        //如果是最后一个周期，更新奖金订单状态为完成发放
        if (ToolUtil.isNotEmpty(orderIdList3)){
            tradeOrderService.batchUpdateOrderStatus(orderIdList3);
        }
        return true;
    }
}
