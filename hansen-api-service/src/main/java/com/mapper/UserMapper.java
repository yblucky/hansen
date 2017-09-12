package com.mapper;

import com.base.dao.CommonDao;
import com.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper extends CommonDao<User> {

    Integer updateEquityAmtByUserId(@Param("id") String userId, @Param("equityAmt") Double equityAmt);

    Integer updatePayAmtByUserId(@Param("id") String userId, @Param("payAmt") Double payAmt);

    Integer updateTradeAmtByUserId(@Param("id") String userId, @Param("tradeAmt") Double tradeAmt);

    Integer updateSumProfitsCoverByUserId(@Param("id") String userId, @Param("sumProfits") Double sumProfits);

    Integer updateInsuranceAmtCoverByUserId(@Param("id") String userId, @Param("insuranceAmt") Double insuranceAmt);

    Integer updateMaxProfitsCoverByUserId(@Param("id") String userId, @Param("maxProfits") Double maxProfits);

    Integer updateSumProfitsByUserId(@Param("id") String userId, @Param("sumProfits") Double sumProfits);

    Integer updateInsuranceAmtByUserId(@Param("id") String userId, @Param("insuranceAmt") Double insuranceAmt);

    Integer updateMaxProfitsByUserId(@Param("id") String userId, @Param("maxProfits") Double maxProfits);

    Integer updateCardGradeByUserId(@Param("id") String userId, @Param("cardGrade") Integer cardGrade);

    Integer updateUserActiveCode(@Param("id") String userId, @Param("activeCodeNo") Integer activeNo);

    Integer updateUserRegisterCode(@Param("id") String userId, @Param("registerCodeNo") Integer registerNo);

    Integer updateUserRemainTaskNo(@Param("id") String userId, @Param("remainTaskNo") Integer remainTaskNo);

    Integer updateUserStatusByUserId(@Param("id") String userId, @Param("status") Integer status);

    Integer updateUserCardGradeByUserId(@Param("id") String userId, @Param("cardGrade") Integer cardGrade);

    Integer updateUserGradeByUserId(@Param("id") String userId, @Param("grade") Integer grade);

    Integer clearUnActiveUserWithIds(@Param("list") List<String> list);

    User readUserByLoginName(@Param("loginName") String loginName);

    List<User> readUnActiceMoreThanDays();

}
