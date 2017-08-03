package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.GradeMapper;
import com.hansen.model.Grade;
import com.hansen.service.GradeService;
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