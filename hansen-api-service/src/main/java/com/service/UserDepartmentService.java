package com.service;

import com.base.service.CommonService;
import com.model.UserDepartment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface UserDepartmentService extends CommonService<UserDepartment> {

    Integer getMaxGrade(String parentUserId);

    Boolean createUserDepartment(UserDepartment userDepartment);

    List<UserDepartment> getAll(String parentUserId);

    List<UserDepartment> getAllUserDepartment();

    Double getSumAmt(String parentUserId);

    Double getSumDeparmentPerformanceByParentUserId(String parentUserId);

    Integer updatePerformance(String userId, Double performance);

    Integer updateTeamPerformanceByUserId(String userId, Double performance);

    Integer updateDeparmentAndTeamPerformanceByUserId(String userId,  Double performance,Double teamPerformance);

    List<UserDepartment> getDirectTeamList(String parentUserId);

    List<UserDepartment> getDirectMaxGradeList(String parentUserId);
}
