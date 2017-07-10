package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseCarouselMapper;
import com.mall.model.BaseCarousel;
import com.mall.service.BaseCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class BaseCarouselImpl extends CommonServiceImpl<BaseCarousel> implements BaseCarouselService {
    @Autowired
    private BaseCarouselMapper baseCarouselDao;
    @Override
    protected CommonDao<BaseCarousel> getDao() {
        return baseCarouselDao;
    }

    @Override
    protected Class<BaseCarousel> getModelClass() {
        return BaseCarousel.class;
    }
}
