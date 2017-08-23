package com.mappers;

import com.base.dao.CommonDao;
import com.model.UserTask;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTaskMapper extends CommonDao<UserTask> {
    UserTask  readLastOne(String userId);
}
