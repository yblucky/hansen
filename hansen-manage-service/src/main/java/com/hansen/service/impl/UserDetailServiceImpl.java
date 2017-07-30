package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.UserDetailMapper;
import com.hansen.model.UserDetail;
import com.hansen.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class UserDetailServiceImpl extends CommonServiceImpl<UserDetail> implements UserDetailService {
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
