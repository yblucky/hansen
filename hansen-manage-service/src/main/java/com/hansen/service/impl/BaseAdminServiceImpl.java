package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.BaseAdminMapper;
import com.hansen.service.BaseAdminService;
import com.model.BaseAdmin;
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
