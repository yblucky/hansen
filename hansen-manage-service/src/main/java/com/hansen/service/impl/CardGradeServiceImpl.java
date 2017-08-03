package com.hansen.service.impl;

import com.hansen.base.dao.CommonDao;
import com.hansen.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.CardGradeMapper;
import com.hansen.model.CardGrade;
import com.hansen.service.CardGradeService;
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