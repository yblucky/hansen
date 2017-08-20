package com.hansen.sysservice;

import com.hansen.vo.SysResourceVo;

import java.util.List;

/**
 * 主界面业务层接口
 */
public interface MainService {

    /**
     * 加载菜单数据
     *
     * @param roleId
     * @return 集合
     */
    public List<SysResourceVo> findMenu(String roleId);

}
