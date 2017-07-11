package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.GradeMapper;
import com.manage.model.Grade;
import com.manage.service.GradeService;
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
