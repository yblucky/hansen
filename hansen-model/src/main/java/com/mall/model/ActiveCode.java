package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 用户表
 *
 * @date 2016年12月7日
 */
public class ActiveCode extends BaseModel {

    private static final long serialVersionUID = -293223916052L;
    /**
     * code
     */
    private String code;
    /**
     * 持有者
     */
    private String ownerId;
    /**
     * 1:激活码 2 注册码
     */
    private int type;
    /**
     * '使用人id'
     */
    private String usedUserId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsedUserId() {
        return usedUserId;
    }

    public void setUsedUserId(String usedUserId) {
        this.usedUserId = usedUserId;
    }
}
