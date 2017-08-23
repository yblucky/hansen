package com.mapper;

import com.base.dao.CommonDao;
import com.model.SysRolePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 角色权限DAO
 */
@Repository
public interface SysRolePermissionMapper extends CommonDao<SysRolePermission> {

    /**
     * @param roleId
     */
    public void deleteByRoleId(@Param("roleId") String roleId);

}
