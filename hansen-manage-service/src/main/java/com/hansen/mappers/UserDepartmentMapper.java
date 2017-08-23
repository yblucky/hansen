package com.hansen.mappers;

import com.base.dao.CommonDao;
import com.model.UserDepartment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDepartmentMapper extends CommonDao<UserDepartment> {

    List<UserDepartment> getDirectTeamList(@Param("parentUserId") String parentUserId);

    Double getSumAmt(@Param("parentUserId") String parentUserId);

    Integer updatePerformanceByUserId(String userId, Double amt);
}
