package com.manage.vo;

import java.io.Serializable;

/**
 * @date 2017年2月4日
 */
public class CartVo implements Serializable {

	private static final long serialVersionUID = 8041418455885782640L;

	/** 商品Id */
	private String goodsId;
	/** 商品数量 */
	private Integer num;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
