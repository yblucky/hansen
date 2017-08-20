package com.hansen.sysservice;


import com.hansen.vo.SysResourceVo;
import com.hansen.vo.SysRolePermissionVo;

import java.util.List;

/**
 * 权限管理业务层接口
 */
public interface RoleService {

    /**
     * 获取所有菜单列表
     *
     * @param roleId
     * @return
     */
    public List<SysResourceVo> findMenus(String roleId);

    /**
     * 保存角色权限关系
     *
     * @param rolePerm
     * @return
     * @throws Exception
     */
    public void saveRolePerm(SysRolePermissionVo rolePerm) throws Exception;

}
