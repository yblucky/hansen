package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseAdminMapper;
import com.mall.mapper.BaseUserMapper;
import com.mall.model.BaseAdmin;
import com.mall.model.BaseUser;
import com.mall.service.BaseAdminService;
import com.mall.service.BaseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseAdminServiceImpl extends CommonServiceImpl<BaseAdmin> implements BaseAdminService {
    @Autowired
    private BaseAdminMapper baseAdminDao;
    @Override
    protected CommonDao<BaseAdmin> getDao() {
        return baseAdminDao;
    }

    @Override
    protected Class<BaseAdmin> getModelClass() {
        return BaseAdmin.class;
    }

}
