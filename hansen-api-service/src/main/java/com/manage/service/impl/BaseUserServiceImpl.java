package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.UserMapper;
import com.manage.model.User;
import com.manage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserServiceImpl extends CommonServiceImpl<User> implements UserService {
    @Autowired
    private UserMapper baseUserDao;
    @Override
    protected CommonDao<User> getDao() {
        return baseUserDao;
    }

    @Override
    protected Class<User> getModelClass() {
        return User.class;
    }

}
