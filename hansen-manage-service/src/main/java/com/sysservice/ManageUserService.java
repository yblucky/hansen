/* 
 * 文件名：UserService.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2017年6月12日
 * 版本号：v1.0
*/
package com.sysservice;


import com.base.page.Paging;
import com.base.service.CommonService;
import com.model.SysUser;
import com.vo.SysRoleVo;
import com.vo.SysUserVo;

import java.util.List;

/**
 * 用户业务层接口
 */
public interface ManageUserService extends CommonService<SysUser>{

    /**
     * 获取用户信息
     *
     * @param token 令牌
     * @return 用户对象
     * @throws Exception
     */
    public SysUserVo SysUserVo(String token) throws Exception;

    /**
     * 获取所有数据
     *
     * @param paging
     * @return
     */
    public List<SysUserVo> findAll(Paging paging);

    /**
     * 查找总记录数
     *
     * @return
     */
    public int findCount();

    /**
     * 新增用户
     *
     * @param userVo
     * @throws Exception
     */
    public void add(SysUserVo userVo) throws Exception;

    /**
     * 修改用户
     *
     * @param userVo
     * @throws Exception
     */
    public void update(SysUserVo userVo) throws Exception;

    /**
     * 删除用户
     *
     * @param userVo
     */
    public void delete(SysUserVo userVo);

    /**
     * 查找所有角色
     *
     * @return
     * @throws Exception
     */
    public List<SysRoleVo> findRoles() throws Exception;

    /**
     * 查找登录名是否存在
     *
     * @param loginName
     * @return
     */
    public SysUserVo findByLoginName(String loginName);

    /**
     * 修改密码
     *
     * @param newPw
     * @param id
     */
    public void updatePw(String newPw, String id);

}
