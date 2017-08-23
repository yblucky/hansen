package com.hansen.service;


import com.base.service.CommonService;
import com.hansen.vo.SysResourceVo;
import com.model.SysResource;

import java.util.List;

/**
 * 权限管理业务层接口
 */
public interface SysResourceService extends CommonService<SysResource>{
    public List<SysResourceVo> findMenus(String roleId);

    public List<SysResourceVo> findRoleMenus(String roleId);
}
