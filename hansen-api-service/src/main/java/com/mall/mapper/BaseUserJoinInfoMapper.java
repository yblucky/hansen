package com.mall.mapper;

import com.mall.core.dao.CommonDao;
import com.mall.model.BaseUserAddress;
import com.mall.model.BaseUserJoinInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseUserJoinInfoMapper extends CommonDao<BaseUserJoinInfo> {

}
