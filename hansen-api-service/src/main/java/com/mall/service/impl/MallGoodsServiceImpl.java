package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallGoodsMapper;
import com.mall.model.MallGoods;
import com.mall.service.MallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class MallGoodsServiceImpl extends CommonServiceImpl<MallGoods> implements MallGoodsService {
    @Autowired
    private MallGoodsMapper mallGoodsDao;

    @Override
    protected CommonDao<MallGoods> getDao() {
        return mallGoodsDao;
    }

    @Override
    protected Class<MallGoods> getModelClass() {
        return MallGoods.class;
    }
}
