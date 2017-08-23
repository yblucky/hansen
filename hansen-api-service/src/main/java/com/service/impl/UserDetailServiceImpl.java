package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.UserDetailMapper;
import com.service.UserDetailService;
import com.model.UserDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class UserDetailServiceImpl extends CommonServiceImpl<UserDetail> implements UserDetailService {
    @Autowired
    private UserDetailMapper userDetailMapper;
    @Override
    protected CommonDao<UserDetail> getDao() {
        return userDetailMapper;
    }

    @Override
    protected Class<UserDetail> getModelClass() {
        return UserDetail.class;
    }

    @Override
    public Integer updateForzenEquityAmtByUserId(String userId, Double amt) {
        return userDetailMapper.updateForzenEquityAmtByUserId(userId,amt);
    }

    @Override
    public Integer updateForzenPayAmtByUserId(String userId, Double amt) {
        return userDetailMapper.updateForzenPayAmtByUserId(userId,amt);
    }

    @Override
    public Integer updateForzenTradeAmtByUserId(String userId, Double amt) {
        return userDetailMapper.updateForzenTradeAmtByUserId(userId,amt);
    }
}
