package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserMapper;
import com.mall.model.BaseUser;
import com.mall.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserServiceImpl extends CommonServiceImpl<BaseUser> implements BaseUserService {
    @Autowired
    private BaseUserMapper baseUserDao;
    @Override
    protected CommonDao<BaseUser> getDao() {
        return baseUserDao;
    }

    @Override
    protected Class<BaseUser> getModelClass() {
        return BaseUser.class;
    }

}
