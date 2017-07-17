package com.api.service;

import com.api.core.service.CommonService;
import com.api.model.Grade;

/**
 * @date 2016年11月27日
 */
public interface GradeService extends CommonService<Grade> {


    Grade getGradeDetail(Integer grade);

    Grade getUserGrade(String userId) throws Exception;
}
