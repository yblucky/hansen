package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 商品类型表
 *
 * @date 2016年12月7日
 */
public class MallSort extends BaseModel {

    private static final long serialVersionUID = 3682914148654318534L;
    /**
     * 名称
     */
    private String name;
    /**
     * 图片
     */
    private String icon;
    /**
     * 排序号
     */
    private Integer rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
