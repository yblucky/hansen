package com.hansen.service;

import com.base.page.JsonResult;
import com.base.service.CommonService;
import com.common.constant.RecordType;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;
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
    void pushBonus(String pushUserId, TradeOrder order) throws Exception;

    void manageBonus(String pushUserId);

    void reloadUserGrade(User user) throws Exception;

    void differnceBonus(String userId);

    void originUpgrade(String userId, Integer cardGrade);

    void coverageUpgrade(String userId, Integer cardGrade);

    void updateUserRegisterCode(User loginUser, CardGrade cardGrade);

    void updateUserActiveCode(User loginUser, CardGrade cardGrade);

    void updateUserStatus(String userId, Integer status);

    Integer updateEquityAmtByUserId(String userId, Double amt);

    Integer updatePayAmtByUserId(String userId, Double amt);

    Integer updateTradeAmtByUserId(String userId, Double amt);

    Integer updateMaxProfitsByUserId(String userId, Double maxProfits);

    Integer updateCardGradeByUserId(String userId, Integer cardGrade);

    User createRegisterUser(User user, CardGrade cardGrade, User inviterUser) throws Exception;

    User innerRegister(User innerUser, User inviterUser, User createUser, CardGrade cardGrade) throws Exception;

    JsonResult innerActicveUser(User innerUser, User activeUser, CardGrade cardGrade) throws Exception;


}
