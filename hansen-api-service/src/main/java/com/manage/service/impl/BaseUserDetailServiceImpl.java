package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.UserDetailMapper;
import com.manage.model.UserDetail;
import com.manage.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserDetailServiceImpl extends CommonServiceImpl<UserDetail> implements UserDetailService {
    @Autowired
    private UserDetailMapper baseUserDetailMapper;
    @Override
    protected CommonDao<UserDetail> getDao() {
        return baseUserDetailMapper;
    }

    @Override
    protected Class<UserDetail> getModelClass() {
        return UserDetail.class;
    }

}
