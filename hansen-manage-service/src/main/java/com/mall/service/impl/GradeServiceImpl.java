package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.GradeMapper;
import com.mall.model.Grade;
import com.mall.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class GradeServiceImpl extends CommonServiceImpl<Grade> implements GradeService {
    @Autowired
    private GradeMapper gradeMapper;
    @Override
    protected CommonDao<Grade> getDao() {
        return gradeMapper;
    }

    @Override
    protected Class<Grade> getModelClass() {
        return Grade.class;
    }

}
