package com.hansen.service.impl;


import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mappers.SysRoleMapper;
import com.hansen.service.RoleService;
import com.hansen.service.SysResourceService;
import com.hansen.service.SysRolePermissionService;
import com.hansen.vo.SysResourceVo;
import com.hansen.vo.SysRolePermissionVo;
import com.model.SysRole;
import com.model.SysRolePermission;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理业务层实现类
 */
@Service
public class RoleServiceImpl extends CommonServiceImpl<SysRole> implements RoleService {
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysResourceService sysResourceService;
    @Resource
    private SysRolePermissionService sysRolePermissionService;

    @Override
    protected CommonDao<SysRole> getDao() {
        return sysRoleMapper;
    }

    @Override
    protected Class<SysRole> getModelClass() {
        return SysRole.class;
    }


    @Override
    public List<SysResourceVo> findMenus(String roleId) {
        return sysResourceService.findRoleMenus(roleId);
    }


    @Override
    public void saveRolePerm(SysRolePermissionVo rolePerm) throws Exception {
        //删除数据
        sysResourceService.deleteById(rolePerm.getRoleId());
        //保存数据
        for (SysRolePermissionVo.RolePermVo spVo : rolePerm.getLis()) {
            SysRolePermission rolePermPo = new SysRolePermission();
            rolePermPo.setRoleId(rolePerm.getRoleId());
            rolePermPo.setResourceId(spVo.getResourceId());
            sysRolePermissionService.create(rolePermPo);
        }
    }

}
