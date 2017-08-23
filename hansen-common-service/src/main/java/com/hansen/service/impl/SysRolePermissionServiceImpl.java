package com.hansen.service.impl;


import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mappers.SysRolePermissionMapper;
import com.hansen.service.SysRolePermissionService;
import com.model.SysRolePermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 权限管理业务层接口
 */
@Service
public class SysRolePermissionServiceImpl extends CommonServiceImpl<SysRolePermission> implements SysRolePermissionService {
    @Autowired
    private SysRolePermissionMapper sysRolePermissionMapper;

    @Override
    protected CommonDao<SysRolePermission> getDao() {
        return sysRolePermissionMapper;
    }

    @Override
    protected Class<SysRolePermission> getModelClass() {
        return SysRolePermission.class;
    }
}
