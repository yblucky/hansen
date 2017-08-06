package com.common;

import java.io.Serializable;

public class Token implements Serializable {

    private static final long serialVersionUID = -8000170763642555845L;

    /**
     * 用户ID
     */
    private String id;

    /**
     * 用户昵称
     */
    private String nickName;
    /**
     * 登录时间
     */
    private Long time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

}
