package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallSortMapper;
import com.mall.model.MallSort;
import com.mall.service.MallSortService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class MallSortServiceImpl extends CommonServiceImpl<MallSort> implements MallSortService {
    @Autowired
    private MallSortMapper mallSortDao;
    @Override
    protected CommonDao<MallSort> getDao() {
        return mallSortDao;
    }

    @Override
    protected Class<MallSort> getModelClass() {
        return MallSort.class;
    }
}
