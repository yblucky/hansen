package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.UserDepartmentMapper;
import com.service.UserDepartmentService;
import com.model.UserDepartment;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class UserDepartmentServiceImpl extends CommonServiceImpl<UserDepartment> implements UserDepartmentService {
    @Autowired
    private UserDepartmentMapper userDepartmentMapper;
    @Override
    protected CommonDao<UserDepartment> getDao() {
        return userDepartmentMapper;
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
        return userDepartmentMapper.getAll(parentUserId);
    }

    @Override
    public Double getSumAmt(String parentUserId) {
        Double sumAmt= userDepartmentMapper.getSumAmt(parentUserId);
        if (sumAmt==null){
            return 0d;
        }
        return sumAmt;
    }

    @Override
    public Integer getMaxGrade(String parentUserId) {
        Integer maxGrade= userDepartmentMapper.getMaxGrade(parentUserId);
        if (maxGrade==null){
            return 0;
        }
        return maxGrade;
    }

    @Override
    public Boolean createUserDepartment(UserDepartment userDepartment) {
        userDepartment.setPerformance(0d);
        userDepartment.setTeamPerformance(0d);
        userDepartmentMapper.create(userDepartment);
        return  true;
    }

    @Override
    public Integer updatePerformance(String userId, Double performance) {
        return userDepartmentMapper.updatePerformanceByUserId(userId,performance);
    }

    @Override
    public Integer updateTeamPerformanceByUserId(String userId, Double teamPerformance) {
        return userDepartmentMapper.updateTeamPerformanceByUserId(userId,teamPerformance);
    }

    @Override
    public List<UserDepartment> getDirectTeamList(String parentUserId) {
        return userDepartmentMapper.getDirectTeamList(parentUserId);
    }

    @Override
    public List<UserDepartment> getDirectMaxGradeList(String parentUserId) {
        return userDepartmentMapper.getDirectMaxGradeList(parentUserId);
    }
}
