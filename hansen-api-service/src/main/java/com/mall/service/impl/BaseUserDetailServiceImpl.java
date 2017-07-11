package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserDepartmentMapper;
import com.mall.mapper.BaseUserDetailMapper;
import com.mall.model.BaseUserDepartment;
import com.mall.model.BaseUserDetail;
import com.mall.service.BaseUserDepartmentService;
import com.mall.service.BaseUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserDetailServiceImpl extends CommonServiceImpl<BaseUserDetail> implements BaseUserDetailService {
    @Autowired
    private BaseUserDetailMapper baseUserDetailMapper;
    @Override
    protected CommonDao<BaseUserDetail> getDao() {
        return baseUserDetailMapper;
    }

    @Override
    protected Class<BaseUserDetail> getModelClass() {
        return BaseUserDetail.class;
    }

}
