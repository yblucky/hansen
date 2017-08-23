package com.model;

import com.BaseModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色权限Po类
 */
@Table(name = "sys_role_permission")
public class SysRolePermission extends BaseModel {
	/**
	 * 角色编号
	 */
	@Id
	private String roleId;
	/**
	 * 资源编号
	 */
	@Id
	private String resourceId;
	/**
	 * @return the roleId
	 */
	public String getRoleId() {
		return roleId;
	}
	/**
	 * @param roleId the roleId to set
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	/**
	 * @return the resourceId
	 */
	public String getResourceId() {
		return resourceId;
	}
	/**
	 * @param resourceId the resourceId to set
	 */
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	
	
}
