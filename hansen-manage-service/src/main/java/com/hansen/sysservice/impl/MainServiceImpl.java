package com.hansen.sysservice.impl;

import com.hansen.sysmapper.SysResourceMapper;
import com.hansen.sysservice.MainService;
import com.hansen.vo.SysResourceVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 主界面业务层实现类
 */
@Service
public class MainServiceImpl implements MainService {
    @Resource
    public SysResourceMapper resourceMapper;

    /**
     * 加载菜单按钮列表
     *
     * @return 集合
     */
    @Override
    public List<SysResourceVo> findMenu(String roleId) {
        return resourceMapper.findMenus(roleId);
    }
}
