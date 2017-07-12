package com.api.model;

import com.api.common.BaseModel;

import java.util.Date;

/**
 * 文件系统表
 *
 * @date 2016年12月7日
 */
public class Image extends BaseModel {

    private static final long serialVersionUID = -7693797776371276572L;


    /**
     * 图片来源id
     */
    private String linkId;
    /**
     * 1:用户图  2：广告图
     */
    private Integer type;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 排序号
     */
    private Integer rank;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 状态：0：禁用，1：启用
     */
    private Integer status;
    /**
     * 备注扩展参数
     */
    private String remark;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public Integer getStatus() {
        return status;
    }

    @Override
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLinkId() {
        return linkId;
    }

    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
