package com.hansen.service;

import com.base.service.CommonService;
import com.model.UserDepartment;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface UserDepartmentService extends CommonService<UserDepartment> {

    List<UserDepartment> getAll(String parentUserId);

    Double getSumAmt(String parentUserId);
}
