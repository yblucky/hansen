package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.UserDepartmentMapper;
import com.hansen.service.UserDepartmentService;
import com.model.UserDepartment;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserDepartmentServiceImpl extends CommonServiceImpl<UserDepartment> implements UserDepartmentService {
    @Autowired
    private UserDepartmentMapper baseUserDepartmentMapper;
    @Override
    protected CommonDao<UserDepartment> getDao() {
        return baseUserDepartmentMapper;
    }

    @Override
    protected Class<UserDepartment> getModelClass() {
        return UserDepartment.class;
    }

    @Override
    public List<UserDepartment> getAll(String parentUserId){
        if(StringUtils.isEmpty(parentUserId)){
            return null;
        }
        return this.getAll(parentUserId);
    }

    @Override
    public Double getSumAmt(String parentUserId) {
        return baseUserDepartmentMapper.getSumAmt(parentUserId);
    }

    @Override
    public Boolean createUserDepartment(UserDepartment userDepartment) {
        userDepartment.setPerformance(0d);
        baseUserDepartmentMapper.create(userDepartment);
        return  true;
    }

    @Override
    public Boolean updatePerformance(String id, Double performance) {
        return null;
    }
}
