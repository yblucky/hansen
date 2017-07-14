package com.api.service;

import com.api.core.service.CommonService;
import com.api.model.CardGrade;

/**
 * @date 2016年11月27日
 */
public interface CardGradeService extends CommonService<CardGrade> {


    CardGrade getUserCardGrade(Integer cardLevel);
}
