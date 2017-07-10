package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserAddressMapper;
import com.mall.mapper.BaseUserJoinInfoMapper;
import com.mall.model.BaseUserAddress;
import com.mall.model.BaseUserJoinInfo;
import com.mall.service.BaseUserAddressService;
import com.mall.service.BaseUserJoinInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserJoinInfoServiceImpl extends CommonServiceImpl<BaseUserJoinInfo> implements BaseUserJoinInfoService {
    @Autowired
    private BaseUserJoinInfoMapper baseUserJoinInfoDao;
    @Override
    protected CommonDao<BaseUserJoinInfo> getDao() {
        return baseUserJoinInfoDao;
    }

    @Override
    protected Class<BaseUserJoinInfo> getModelClass() {
        return BaseUserJoinInfo.class;
    }

}
