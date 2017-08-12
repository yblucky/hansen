package com.hansen.mapper;

import com.base.dao.CommonDao;
import com.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends CommonDao<User> {

    Integer updateEquityAmtByUserId(String userId,Double amt);

    Integer updatePayAmtByUserId(String userId,Double amt);

    Integer updateTradeAmtByUserId(String userId,Double amt);
}
