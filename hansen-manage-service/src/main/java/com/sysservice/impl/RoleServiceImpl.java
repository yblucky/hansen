package com.sysservice.impl;


import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.SysRoleMapper;
import com.model.SysRole;
import com.model.SysRolePermission;
import com.sysservice.RoleService;
import com.sysservice.SysResourceService;
import com.sysservice.SysRolePermissionService;
import com.vo.SysResourceVo;
import com.vo.SysRolePermissionVo;
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
        sysRolePermissionService.deleteByRoleId(rolePerm.getRoleId());
        //保存数据
        for (SysRolePermissionVo.RolePermVo spVo : rolePerm.getLis()) {
            SysRolePermission rolePermPo = new SysRolePermission();
            rolePermPo.setRoleId(rolePerm.getRoleId());
            rolePermPo.setResourceId(spVo.getResourceId());
            sysRolePermissionService.create(rolePermPo);
        }
    }

}
