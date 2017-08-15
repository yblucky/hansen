package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.TaskMapper;
import com.hansen.mapper.UserTaskMapper;
import com.hansen.service.TaskService;
import com.hansen.service.UserTaskService;
import com.model.Task;
import com.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class UserTaskServiceImpl extends CommonServiceImpl<UserTask> implements UserTaskService {
    @Autowired
    private UserTaskMapper userTaskMapper;

    @Override
    protected CommonDao<UserTask> getDao() {
        return userTaskMapper;
    }

    @Override
    protected Class<UserTask> getModelClass() {
        return UserTask.class;
    }

}
