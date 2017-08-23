package com.sysservice.impl;

import com.mapper.SysResourceMapper;
import com.sysservice.MainService;
import com.vo.SysResourceVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 主界面业务层实现类
 */
@Service
public class MainServiceImpl implements MainService {
    @Resource
    public SysResourceMapper sysResourceMapper;

    /**
     * 加载菜单按钮列表
     *
     * @return 集合
     */
    @Override
    public List<SysResourceVo> findMenu(String roleId) {
        return sysResourceMapper.findMenus(roleId);
    }
}
