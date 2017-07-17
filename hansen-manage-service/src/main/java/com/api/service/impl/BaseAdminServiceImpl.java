package com.api.service.impl;

import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.BaseAdminMapper;
import com.api.model.BaseAdmin;
import com.api.service.BaseAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
