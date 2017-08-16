package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.ActiveCodeMapper;
import com.hansen.mapper.TaskMapper;
import com.hansen.service.ActiveCodeService;
import com.hansen.service.TaskService;
import com.model.ActiveCode;
import com.model.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class TaskServiceImpl extends CommonServiceImpl<Task> implements TaskService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    protected CommonDao<Task> getDao() {
        return taskMapper;
    }

    @Override
    protected Class<Task> getModelClass() {
        return Task.class;
    }

}
