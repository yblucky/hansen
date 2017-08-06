package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.UserMapper;
import com.hansen.service.UserService;
import com.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class UserServiceImpl extends CommonServiceImpl<User> implements UserService {
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
