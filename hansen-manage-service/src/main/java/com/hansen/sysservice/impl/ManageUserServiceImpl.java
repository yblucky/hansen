package com.hansen.sysservice.impl;


import com.common.service.RedisService;
import com.common.utils.classutils.MyBeanUtils;
import com.common.utils.codeutils.CryptUtils;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.resp.Paging;
import com.hansen.sysmapper.SysRoleMapper;
import com.hansen.sysmapper.SysUserMapper;
import com.hansen.sysservice.ManageUserService;
import com.hansen.vo.SysRoleVo;
import com.hansen.vo.SysUserVo;
import com.model.SysUserPo;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 用户业务层实现类
 */
@Service
public class ManageUserServiceImpl implements ManageUserService {
	@Resource
	private RedisService redisService;
	@Resource
	private SysUserMapper sysUserMapper;
	@Resource
	private SysRoleMapper roleMapper;

	@Override
	public SysUserVo SysUserVo(String token) throws Exception {
		SysUserVo userVo = null;
		Object obj = redisService.getObj(token);
		if(obj != null && obj instanceof SysUserVo){
			userVo = (SysUserVo) obj;
		}
		return userVo;
	}

	@Override
	public List<SysUserVo> findAll(Paging paging) {
		RowBounds rwoBounds = new RowBounds(paging.getPageNumber(),paging.getPageSize());
		return sysUserMapper.findAll(rwoBounds);
	}

	@Override
	public long findCount() {
		return sysUserMapper.findCount();
	}

	@Override
	public void add(SysUserVo userVo) throws Exception {
		SysUserPo userPo = MyBeanUtils.copyProperties(userVo, SysUserPo.class);
		String salt = ToolUtil.getUUID();
		String loginPw = CryptUtils.hmacSHA1Encrypt(userVo.getPassword(), salt);
		userPo.setPassword(loginPw);
		userPo.setSalt(salt);
		userPo.setId(ToolUtil.getUUID());
		userPo.setCreateTime(new Date());
		sysUserMapper.insert(userPo);
	}

	@Override
	public void update(SysUserVo userVo) throws Exception {
		SysUserPo userPo = sysUserMapper.selectByPrimaryKey(userVo.getId());
		userPo.setUserName(userVo.getUserName());
		userPo.setMobile(userVo.getMobile());
		userPo.setRoleId(userVo.getRoleId());
		userPo.setRoleName(userVo.getRoleName());
		userPo.setState(userVo.getState());
		sysUserMapper.updateByPrimaryKey(userPo);
	}


	@Override
	public void delete(SysUserVo userVo) {
		sysUserMapper.deleteByPrimaryKey(userVo.getId());
	}

	@Override
	public List<SysRoleVo> findRoles() throws Exception {
		return MyBeanUtils.copyList(roleMapper.selectAll(), SysRoleVo.class);
	}

	@Override
	public SysUserVo findByLoginName(String loginName) {
		return sysUserMapper.findByloginName(loginName);
	}

	@Override
	public void updatePw(String newPw,String id) {
		SysUserPo userPo = sysUserMapper.selectByPrimaryKey(id);
		userPo.setPassword(newPw);
		sysUserMapper.updateByPrimaryKey(userPo);
	}

}
