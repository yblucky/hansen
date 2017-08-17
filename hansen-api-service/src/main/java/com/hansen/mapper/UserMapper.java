package com.hansen.mapper;

import com.base.dao.CommonDao;
import com.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends CommonDao<User> {

    Integer updateEquityAmtByUserId(@Param("id") String userId,@Param("equityAmt") Double equityAmt);

    Integer updatePayAmtByUserId(@Param("id")String userId,@Param("payAmt")Double payAmt);

    Integer updateTradeAmtByUserId(@Param("id")String userId,@Param("tradeAmt")Double tradeAmt);

    Integer updateMaxProfitsByUserId(@Param("id")String userId,@Param("maxProfits")Double maxProfits);

    Integer updateCardGradeByUserId(@Param("id")String userId, @Param("id")Integer cardGrade);


}
