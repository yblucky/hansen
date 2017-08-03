package com.hansen.model;


import com.hansen.common.BaseModel;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
public class Parameter extends BaseModel {

    private static final long serialVersionUID = -76937973371276572L;

    private String name;
    private String value;
    private String kind;
    private String title;
    private String remark;
    private String groupType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
