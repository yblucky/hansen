package com.sysservice.impl;


import com.base.dao.CommonDao;
import com.base.page.Paging;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.SysRoleMapper;
import com.mapper.SysUserMapper;
import com.service.RedisService;
import com.model.SysRole;
import com.model.SysUser;
import com.sysservice.ManageUserService;
import com.utils.classutils.MyBeanUtils;
import com.utils.codeutils.CryptUtils;
import com.utils.toolutils.ToolUtil;
import com.vo.SysRoleVo;
import com.vo.SysUserVo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户业务层实现类
 */
@Service
public class ManageUserServiceImpl extends CommonServiceImpl<SysUser> implements ManageUserService {
    @Autowired
    private RedisService redisService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper roleMapper;

    @Override
    protected CommonDao<SysUser> getDao() {
        return sysUserMapper;
    }

    @Override
    protected Class<SysUser> getModelClass() {
        return SysUser.class;
    }

    @Override
    public SysUserVo SysUserVo(String token) throws Exception {
        SysUserVo userVo = null;
        Object obj = redisService.getObj(token);
        if (obj != null && obj instanceof SysUserVo) {
            userVo = (SysUserVo) obj;
        }
        return userVo;
    }

    @Override
    public List<SysUserVo> findAll(Paging paging) {
        RowBounds rwoBounds = new RowBounds(paging.getStartRow(), paging.getPageSize());
        return sysUserMapper.findAll(rwoBounds);
    }

    @Override
    public int findCount() {
        return sysUserMapper.findCount();
    }

    @Override
    public void add(SysUserVo userVo) throws Exception {
        SysUser userPo = MyBeanUtils.copyProperties(userVo, SysUser.class);
        String salt = ToolUtil.getUUID();
        String loginPw = CryptUtils.hmacSHA1Encrypt(userVo.getPassword(), salt);
        userPo.setPassword(loginPw);
        userPo.setSalt(salt);
        userPo.setId(ToolUtil.getUUID());
        userPo.setCreateTime(new Date());
        sysUserMapper.create(userPo);
    }

    @Override
    public void update(SysUserVo userVo) throws Exception {
        SysUser userPo = sysUserMapper.readById(userVo.getId());
        userPo.setUserName(userVo.getUserName());
        userPo.setMobile(userVo.getMobile());
        userPo.setRoleId(userVo.getRoleId());
        userPo.setRoleName(userVo.getRoleName());
        userPo.setState(userVo.getState());
        sysUserMapper.updateById(userPo.getId(), userPo);
    }


    @Override
    public void delete(SysUserVo userVo) {
        sysUserMapper.deleteById(userVo.getId());
    }

    @Override
    public List<SysRoleVo> findRoles() throws Exception {
        return MyBeanUtils.copyList(roleMapper.readList(new SysRole(), 0, 1000), SysRoleVo.class);
    }

    @Override
    public SysUserVo findByLoginName(String loginName) {
        return sysUserMapper.findByloginName(loginName);
    }

    @Override
    public void updatePw(String newPw, String id) {
        SysUser userPo = sysUserMapper.readById(id);
        userPo.setPassword(newPw);
        sysUserMapper.updateById(userPo.getId(), userPo);
    }

    @Override
    public void updateSupperPassPw(String newPw, String id) {
        SysUser userPo = sysUserMapper.readById(id);
        userPo.setRemark(newPw);
        sysUserMapper.updateById(userPo.getId(), userPo);
    }

}
