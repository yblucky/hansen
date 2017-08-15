package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.UserSignMapper;
import com.hansen.mapper.UserTaskMapper;
import com.hansen.service.UserSignService;
import com.hansen.service.UserTaskService;
import com.model.UserSign;
import com.model.UserTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class UserSignServiceImpl extends CommonServiceImpl<UserSign> implements UserSignService {
    @Autowired
    private UserSignMapper userSignMapper;

    @Override
    protected CommonDao<UserSign> getDao() {
        return userSignMapper;
    }

    @Override
    protected Class<UserSign> getModelClass() {
        return UserSign.class;
    }

}
