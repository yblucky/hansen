package com.mall.mapper;

import com.mall.core.dao.CommonDao;
import com.mall.model.BaseUserAddress;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseUserAddressMapper extends CommonDao<BaseUserAddress> {

	List<BaseUserAddress> getAddressListByUserId(@Param("userId") String userId);

}
