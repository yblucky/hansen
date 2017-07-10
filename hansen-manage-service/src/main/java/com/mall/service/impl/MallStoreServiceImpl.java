package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallStoreMapper;
import com.mall.model.MallStore;
import com.mall.service.MallStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年12月28日
 */
@Service
public class MallStoreServiceImpl extends CommonServiceImpl<MallStore> implements MallStoreService {
    @Autowired
    private MallStoreMapper mallStoreDao;
    @Override
    protected CommonDao<MallStore> getDao() {
        return mallStoreDao;
    }

    @Override
    protected Class<MallStore> getModelClass() {
        return MallStore.class;
    }
}
