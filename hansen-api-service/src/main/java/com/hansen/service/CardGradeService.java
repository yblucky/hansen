package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.model.CardGrade;

/**
 * @date 2016年11月27日
 */
public interface CardGradeService extends CommonService<CardGrade> {


    CardGrade getUserCardGrade(Integer cardLevel);
}
