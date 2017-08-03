package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.model.Grade;

/**
 * @date 2016年11月27日
 */
public interface GradeService extends CommonService<Grade> {


    Grade getGradeDetail(Integer grade);

    Grade getUserGrade(String userId) throws Exception;
}