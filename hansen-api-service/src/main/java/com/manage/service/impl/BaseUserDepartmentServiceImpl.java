package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.UserDepartmentMapper;
import com.manage.model.UserDepartment;
import com.manage.service.UserDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserDepartmentServiceImpl extends CommonServiceImpl<UserDepartment> implements UserDepartmentService {
    @Autowired
    private UserDepartmentMapper baseUserDepartmentMapper;
    @Override
    protected CommonDao<UserDepartment> getDao() {
        return baseUserDepartmentMapper;
    }

    @Override
    protected Class<UserDepartment> getModelClass() {
        return UserDepartment.class;
    }

}
