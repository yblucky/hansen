package com.service;

import com.base.service.CommonService;
import com.model.CardGrade;

/**
 * @date 2016年11月27日
 */
public interface CardGradeService extends CommonService<CardGrade> {


    CardGrade getUserCardGrade(Integer cardLevel);
}
