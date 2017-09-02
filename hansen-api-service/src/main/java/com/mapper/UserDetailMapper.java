package com.mapper;

import com.base.dao.CommonDao;
import com.model.UserDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailMapper extends CommonDao<UserDetail> {

    Integer updateForzenEquityAmtByUserId(@Param("userId") String userId, @Param("amt")Double amt);

    Integer updateForzenPayAmtByUserId(@Param("userId")String userId, @Param("amt")Double amt);

    Integer updateForzenTradeAmtByUserId(@Param("userId")String userId,@Param("amt") Double amt);

}
