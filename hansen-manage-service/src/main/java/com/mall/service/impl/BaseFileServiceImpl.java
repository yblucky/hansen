package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseFileMapper;
import com.mall.model.BaseFile;
import com.mall.service.BaseFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class BaseFileServiceImpl extends CommonServiceImpl<BaseFile> implements BaseFileService {
    @Autowired
    private BaseFileMapper BaseFileDao;
    @Override
    protected CommonDao<BaseFile> getDao() {
        return BaseFileDao;
    }

    @Override
    protected Class<BaseFile> getModelClass() {
        return BaseFile.class;
    }
}
