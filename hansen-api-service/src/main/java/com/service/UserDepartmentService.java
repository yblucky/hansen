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

    Double getSumAmt(String parentUserId);

    Integer updatePerformance(String userId, Double performance);

    List<UserDepartment> getDirectTeamList(String parentUserId);

    List<UserDepartment> getDirectMaxGradeList(String parentUserId);
}
