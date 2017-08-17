package com.hansen.service;

import com.base.service.CommonService;
import com.model.UserDepartment;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface UserDepartmentService extends CommonService<UserDepartment> {

    Boolean createUserDepartment(UserDepartment userDepartment);

    List<UserDepartment> getAll(String parentUserId);

    Double getSumAmt(String parentUserId);

    Boolean updatePerformance(String id,Double performance);
}
