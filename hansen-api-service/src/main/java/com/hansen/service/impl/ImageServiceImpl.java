package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mappers.ImageMapper;
import com.hansen.service.ImageService;
import com.model.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class ImageServiceImpl extends CommonServiceImpl<Image> implements ImageService {
    @Autowired
    private ImageMapper imageMapper;
    @Override
    protected CommonDao<Image> getDao() {
        return imageMapper;
    }

    @Override
    protected Class<Image> getModelClass() {
        return Image.class;
    }
}
