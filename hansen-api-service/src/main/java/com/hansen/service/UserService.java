package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.common.constant.RecordType;
import com.hansen.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface UserService extends CommonService<User> {


    @Transactional
    void userIncomeAmt(Double incomAmt, String userId, RecordType type, String orderNo);

    @Transactional
    void weeklyIncomeAmt(User user);

    @Transactional
    void pushBonus(String pushUserId);

    void manageBonus(String pushUserId);

    void reloadUserGrade(User user) throws Exception;

    void differnceBonus(String userId);

    void originUpgrade(String userId, Integer cardGrade);

    void coverageUpgrade(String userId, Integer cardGrade);
}
