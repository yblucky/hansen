package com.api.service;

import com.api.constant.RecordType;
import com.api.core.service.CommonService;
import com.api.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface UserService extends CommonService<User> {


    @Transactional
    void calc(Double incomAmt, String userId, RecordType type);

    @Transactional
    void weeklyIncome(User user);

    @Transactional
    void pushBonus(String pushUserId);

    void manageBonus(String pushUserId);

    void reloadUserGrade(User user) throws Exception;
}
