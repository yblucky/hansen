package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 文件系统表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class BaseFile extends BaseModel {

    private static final long serialVersionUID = -7693797776371276572L;
    /**
     * 文件id
     */
    private String linkId;
    /**
     * 文件名
     */
    private String filename;
    /**
     * 文件路径
     */
    private String icon;
    /**
     * 文件类型
     */
    private Integer type;
    /**
     * 上传人
     */
    private String uploadUserId;

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getUploadUserId() {
        return uploadUserId;
    }

    public void setUploadUserId(String uploadUserId) {
        this.uploadUserId = uploadUserId;
    }
}
