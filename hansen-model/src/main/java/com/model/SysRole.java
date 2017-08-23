package com.model;

import com.BaseModel;

/**
 * 角色Po类
 */
public class SysRole extends BaseModel {
    /**
     * 主键编号
     */
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
     * @param id the id to set
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
     * @param roleName the roleName to set
     */
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
