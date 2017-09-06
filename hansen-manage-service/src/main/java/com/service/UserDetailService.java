package com.service;

import com.base.service.CommonService;
import com.model.UserDetail;
import com.vo.UserDetailVo;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface UserDetailService extends CommonService<UserDetail> {

    Integer updateForzenEquityAmtByUserId(String userId, Double amt);

    Integer updateForzenPayAmtByUserId(String userId, Double amt);

    Integer updateForzenTradeAmtByUserId(String userId, Double amt);


    List<UserDetailVo> findAll(UserDetailVo vo, int pageNumber, int pageSize, Integer count);
}
