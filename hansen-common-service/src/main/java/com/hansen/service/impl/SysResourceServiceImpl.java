package com.hansen.service.impl;


import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.SysResourceMapper;
import com.hansen.service.SysResourceService;
import com.hansen.vo.SysResourceVo;
import com.model.SysResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限管理业务层接口
 */
@Service
public class SysResourceServiceImpl extends CommonServiceImpl<SysResource> implements SysResourceService {
    @Autowired
    private SysResourceMapper sysResourceMapper;

    @Override
    protected CommonDao<SysResource> getDao() {
        return sysResourceMapper;
    }

    @Override
    protected Class<SysResource> getModelClass() {
        return SysResource.class;
    }

    @Override
    public List<SysResourceVo> findMenus(String roleId) {
        return sysResourceMapper.findMenus(roleId);
    }

    @Override
    public List<SysResourceVo> findRoleMenus(String roleId) {
        return sysResourceMapper.findRoleMenus(roleId);
    }
}
