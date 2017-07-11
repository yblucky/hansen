package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserDepartmentMapper;
import com.mall.model.BaseUserDepartment;
import com.mall.service.BaseUserDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserDepartmentServiceImpl extends CommonServiceImpl<BaseUserDepartment> implements BaseUserDepartmentService {
    @Autowired
    private BaseUserDepartmentMapper baseUserDepartmentMapper;
    @Override
    protected CommonDao<BaseUserDepartment> getDao() {
        return baseUserDepartmentMapper;
    }

    @Override
    protected Class<BaseUserDepartment> getModelClass() {
        return BaseUserDepartment.class;
    }

}
