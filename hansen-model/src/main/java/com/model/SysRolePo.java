package com.model;

import com.common.BaseModel;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 角色Po类
 */
@Table(name="sys_role")
public class SysRolePo  extends BaseModel {
	/**
	 * 主键编号
	 */
	@Id
	private String id;
	/**
	 * 角色名
	 */
	private String roleName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the roleName
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * @param roleName
	 *            the roleName to set
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
}
