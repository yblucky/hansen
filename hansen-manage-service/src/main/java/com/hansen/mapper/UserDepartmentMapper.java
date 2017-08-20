package com.hansen.mapper;

import com.base.dao.CommonDao;
import com.model.UserDepartment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDepartmentMapper extends CommonDao<UserDepartment> {
    Integer updatePerformanceByUserId(@Param("id") String userId, @Param("performance") Double performance);
}
