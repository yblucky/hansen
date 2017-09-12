package com.service;

import com.base.page.JsonResult;
import com.base.service.CommonService;
import com.constant.RecordType;
import com.constant.UpGradeType;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface UserService extends CommonService<User> {


    @Transactional
    void userIncomeAmt(Double incomAmt, String userId, RecordType type, String orderNo) throws Exception;

    @Transactional
    void weeklyIncomeAmt(User user) throws Exception;

    @Transactional
    void pushBonus(String pushUserId, TradeOrder order) throws Exception;

    Double manageBonus(String pushUserId, TradeOrder order, Double weekAmt) throws Exception;

    void reloadUserGrade(User user) throws Exception;

    void reloadUserGrade(String userId) throws Exception;

    void differnceBonus(String userId, TradeOrder order) throws Exception;

    void originUpgrade(String userId, Integer cardGrade) throws Exception;

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

    Integer updateInsuranceAmtByUserId(String userId, Double insuranceAmt);

    Integer updateSumProfitsCoverByUserId(String userId, Double maxProfits);

    Integer updateInsuranceAmtCoverByUserId(String userId, Double insuranceAmt);

    Integer updateMaxProfitsCoverByUserId(String userId, Double maxProfits);

    Integer updateCardGradeByUserId(String userId, Integer cardGrade);

    Integer updateRemainTaskNoByUserId(String userId, Integer remainTaskNo);

    Integer updateUserStatusByUserId(String userId, Integer status);

    Integer updateUserGradeByUserId(String userId, Integer grade);

    Integer clearUnActiveUserWithIds(List<String> list);

    Integer updateUserCardGradeByUserId(String userId, Integer cardGrade);

    User createRegisterUser(User creatUser, CardGrade cardGrade, User loginUser, User inviterUser) throws Exception;

    User innerRegister(User innerUser, User inviterUser, User createUser, CardGrade cardGrade) throws Exception;

    JsonResult innerActicveUser(User activeUser, CardGrade cardGrade) throws Exception;

    User readUserByLoginName(String loginName);

    Boolean intervalActice(String userId);

    Boolean upGrade(User loginUser, CardGrade cardGrade, UpGradeType upGradeType) throws Exception;

    void differnceBonusNew(String userId, TradeOrder order) throws Exception;

    Boolean isVrticalLine(Integer inviterUid, Integer concatUid) throws Exception;

    Boolean isVrticalLine(String inviterUserId, String concatUserId) throws Exception;

    Boolean regularlyClearUnActiveUser() throws Exception;

    List<User> readUnActiceMoreThanDays();
}
