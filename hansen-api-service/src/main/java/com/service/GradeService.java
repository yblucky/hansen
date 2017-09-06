package com.service;

import com.base.service.CommonService;
import com.model.Grade;

/**
 * @date 2016年11月27日
 */
public interface GradeService extends CommonService<Grade> {


    Grade getGradeDetail(Integer grade);

    Grade getUserGrade(String userId) throws Exception;

    Grade getUserGrade2(String userId) throws Exception;
}
