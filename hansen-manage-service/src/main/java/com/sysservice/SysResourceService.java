package com.sysservice;


import com.base.service.CommonService;
import com.model.SysResource;
import com.vo.SysResourceVo;

import java.util.List;

/**
 * 权限管理业务层接口
 */
public interface SysResourceService extends CommonService<SysResource>{
    public List<SysResourceVo> findMenus(String roleId);

    public List<SysResourceVo> findRoleMenus(String roleId);
}
