package com.mall.service;

import com.mall.core.service.CommonService;
import com.mall.model.MallGoods;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface MallGoodsService extends CommonService<MallGoods> {

    void batchDel(List<String> ids);
}
