package com.hansen.sysservice.impl;


import com.hansen.sysmapper.SysResourceMapper;
import com.hansen.sysmapper.SysRolePermissionMapper;
import com.hansen.sysservice.RoleService;
import com.hansen.vo.SysResourceVo;
import com.hansen.vo.SysRolePermissionVo;
import com.model.SysRolePermissionPo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 权限管理业务层实现类
 */
@Service
public class RoleServiceImpl implements RoleService {
	@Resource
	private SysResourceMapper resMapper;
	@Resource
	private SysRolePermissionMapper rolePermMapper;


	@Override
	public List<SysResourceVo> findMenus(String roleId) {
		return resMapper.findRoleMenus(roleId);
	}


	@Override
	public void saveRolePerm(SysRolePermissionVo rolePerm) throws Exception {
		//删除数据
		rolePermMapper.deleteByRoleId(rolePerm.getRoleId());
		//保存数据
		for(SysRolePermissionVo.RolePermVo spVo:rolePerm.getLis()){
			SysRolePermissionPo rolePermPo = new SysRolePermissionPo();
			rolePermPo.setRoleId(rolePerm.getRoleId());
			rolePermPo.setResourceId(spVo.getResourceId());
			rolePermMapper.insert(rolePermPo);
		}
	}
	
}
