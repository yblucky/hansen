package com.mall.mapper;

import com.mall.core.dao.CommonDao;
import com.mall.model.BaseUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BaseUserMapper extends CommonDao<BaseUser> {

    BaseUser readByUnionId(@Param("unionId") String unionId);

}
