package com.service;

import com.base.page.JsonResult;
import com.base.service.CommonService;
import com.constant.RecordType;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface UserService extends CommonService<User> {


    @Transactional
    void userIncomeAmt(Double incomAmt, String userId, RecordType type, String orderNo)  throws Exception;

    @Transactional
    void weeklyIncomeAmt(User user)throws Exception;

    @Transactional
    void pushBonus(String pushUserId, TradeOrder order) throws Exception;

    void manageBonus(String pushUserId, TradeOrder order) throws Exception;

    void reloadUserGrade(User user) throws Exception;

    void differnceBonus(String userId, TradeOrder order) throws Exception;

    void originUpgrade(String userId, Integer cardGrade);

    void coverageUpgrade(String userId, Integer cardGrade) throws Exception;

    void updateUserRegisterCode(User loginUser, CardGrade cardGrade);

    void updateUserActiveCode(String userId, Integer activeNo);

    void updateUserRegisterCode(String userId, Integer activeNo);

    void updateUserStatus(String userId, Integer status);

    Integer updateEquityAmtByUserId(String userId, Double amt);

    Integer updatePayAmtByUserId(String userId, Double amt);

    Integer updateTradeAmtByUserId(String userId, Double amt);

    Integer updateMaxProfitsByUserId(String userId, Double maxProfits);

    Integer updateSumProfitsByUserId(String userId, Double maxProfits);

    Integer updateCardGradeByUserId(String userId, Integer cardGrade);

    Integer updateRemainTaskNoByUserId(String userId, Integer remainTaskNo);

    Integer updateUserStatusByUserId(String userId, Integer status);

    Integer updateUserGradeByUserId(String userId, Integer grade);

    Integer updateUserCardGradeByUserId(String userId, Integer cardGrade);

    User createRegisterUser(User user, CardGrade cardGrade, User inviterUser) throws Exception;

    User innerRegister(User innerUser, User inviterUser, User createUser, CardGrade cardGrade) throws Exception;

    JsonResult innerActicveUser(User activeUser, CardGrade cardGrade) throws Exception;


}
