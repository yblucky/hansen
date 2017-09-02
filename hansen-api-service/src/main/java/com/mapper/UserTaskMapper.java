package com.mapper;

import com.base.dao.CommonDao;
import com.model.UserTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskMapper extends CommonDao<UserTask> {
    UserTask readLastOne(String userId);

    Integer readCompeleteUserTaskCount(@Param("userId") String userId);
}
