package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mappers.UserDetailMapper;
import com.hansen.service.UserDetailService;
import com.model.UserDetail;
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

    @Override
    public Integer updateForzenEquityAmtByUserId(String userId, Double amt) {
        return baseUserDetailMapper.updateForzenEquityAmtByUserId(userId,amt);
    }

    @Override
    public Integer updateForzenPayAmtByUserId(String userId, Double amt) {
        return baseUserDetailMapper.updateForzenPayAmtByUserId(userId,amt);
    }

    @Override
    public Integer updateForzenTradeAmtByUserId(String userId, Double amt) {
        return baseUserDetailMapper.updateForzenTradeAmtByUserId(userId,amt);
    }
}
