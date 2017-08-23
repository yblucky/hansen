package com.service;


import com.base.service.CommonService;
import com.model.SysRole;
import com.vo.SysResourceVo;
import com.vo.SysRolePermissionVo;

import java.util.List;

/**
 * 权限管理业务层接口
 */
public interface RoleService extends  CommonService<SysRole>{

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
