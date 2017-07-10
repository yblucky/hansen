package com.mall.model;

import com.mall.common.BaseModel;


/**
 * 商铺表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class MallShoppingCard extends BaseModel {

    private static final long serialVersionUID = 5811754554769429761L;
    /**
     * 商品分类ID
     */
    private String userId;
    /**
     * 商品分类ID
     */
    private String goodsId;
    /**
     * 金额
     */
    private Integer amount;

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
