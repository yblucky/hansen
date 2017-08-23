package com.hansen.mappers;

import com.base.dao.CommonDao;
import com.model.UserDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailMapper extends CommonDao<UserDetail> {

    Integer updateForzenEquityAmtByUserId(String userId,Double amt);

    Integer updateForzenPayAmtByUserId(String userId,Double amt);

    Integer updateForzenTradeAmtByUserId(String userId,Double amt);

}
