package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 用户加盟信息表
 * Created by Administrator on 2017/5/23.
 */

public class BaseUserJoinInfo extends BaseModel{

    private static final long serialVersionUID = 24149337328156052L;

    /** 加盟用户姓名 */
    private String userName;

    /** 加盟用户电话 */
    private String phone;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
