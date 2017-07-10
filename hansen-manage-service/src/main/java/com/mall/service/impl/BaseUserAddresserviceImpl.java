package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserAddressMapper;
import com.mall.model.BaseUserAddress;
import com.mall.service.BaseUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserAddresserviceImpl extends CommonServiceImpl<BaseUserAddress> implements BaseUserAddressService{
    @Autowired
    private BaseUserAddressMapper baseUserAddressDao;
    @Override
    protected CommonDao<BaseUserAddress> getDao() {
        return baseUserAddressDao;
    }

    @Override
    protected Class<BaseUserAddress> getModelClass() {
        return BaseUserAddress.class;
    }
}
