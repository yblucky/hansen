package com.sysservice;


import com.base.service.CommonService;
import com.model.SysRolePermission;

/**
 * 权限管理业务层接口
 */
public interface SysRolePermissionService extends CommonService<SysRolePermission>{
    void deleteByRoleId(String roleId);
}
