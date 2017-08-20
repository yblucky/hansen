package com.hansen.sysmapper;

import com.hansen.base.BaseMapper;
import com.model.SysRolePermissionPo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 角色权限DAO
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermissionPo> {

    /**
     * @param roleId
     */
    @Select("delete from sys_role_permission where roleId=#{roleId}")
    public void deleteByRoleId(@Param("roleId") String roleId);

}
