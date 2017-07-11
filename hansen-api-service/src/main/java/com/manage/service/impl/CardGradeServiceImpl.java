package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.CardGradeMapper;
import com.manage.model.CardGrade;
import com.manage.service.CardGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class CardGradeServiceImpl extends CommonServiceImpl<CardGrade> implements CardGradeService {
    @Autowired
    private CardGradeMapper cardGradeMapper;
    @Override
    protected CommonDao<CardGrade> getDao() {
        return cardGradeMapper;
    }

    @Override
    protected Class<CardGrade> getModelClass() {
        return CardGrade.class;
    }

}
