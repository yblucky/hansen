package com.mapper;

import com.base.dao.CommonDao;
import com.model.UserSign;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserSignMapper extends CommonDao<UserSign> {

    Double sumUserSignByTime(@Param("startTime") String startTime, @Param("endTime") String endTime);


}
