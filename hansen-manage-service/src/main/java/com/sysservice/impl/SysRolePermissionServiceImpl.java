package com.sysservice.impl;


import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.SysRolePermissionMapper;
import com.model.SysRolePermission;
import com.sysservice.SysRolePermissionService;
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
