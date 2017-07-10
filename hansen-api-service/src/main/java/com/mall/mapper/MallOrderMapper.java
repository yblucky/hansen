package com.mall.mapper;

import com.mall.core.dao.CommonDao;
import com.mall.model.MallOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MallOrderMapper extends CommonDao<MallOrder> {

    MallOrder getByOrderNo(@Param("orderNo") String orderNo, @Param("userId") String userId);

}
