package com.api.service;

import com.api.core.service.CommonService;
import com.api.model.UserDepartment;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface UserDepartmentService extends CommonService<UserDepartment> {

    List<UserDepartment> getAll(String parentUserId);

    Double getSumAmt(String parentUserId);
}
