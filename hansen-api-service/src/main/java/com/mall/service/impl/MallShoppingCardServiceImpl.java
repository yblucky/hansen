package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallShoppingCardMapper;
import com.mall.model.MallShoppingCard;
import com.mall.service.MallShoppingCardService;
import com.mall.service.MallStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年12月28日
 */
@Service
public class MallShoppingCardServiceImpl extends CommonServiceImpl<MallShoppingCard> implements MallShoppingCardService {
    @Autowired
    private MallShoppingCardMapper mallshoppingDao;
    @Override
    protected CommonDao<MallShoppingCard> getDao() {
        return mallshoppingDao;
    }

    @Override
    protected Class<MallShoppingCard> getModelClass() {
        return MallShoppingCard.class;
    }
}
