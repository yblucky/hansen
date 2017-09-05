package com.mapper;

import com.base.dao.CommonDao;
import com.model.UserDetail;
import com.vo.UserDetailVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailMapper extends CommonDao<UserDetail> {

    Integer updateForzenEquityAmtByUserId(String userId, Double amt);

    Integer updateForzenPayAmtByUserId(String userId, Double amt);

    Integer updateForzenTradeAmtByUserId(String userId, Double amt);

    List<UserDetailVo> findAll(@Param("model") UserDetailVo vo,@Param("startRow") int pageNumber,@Param("pageSize") int pageSize);
}
