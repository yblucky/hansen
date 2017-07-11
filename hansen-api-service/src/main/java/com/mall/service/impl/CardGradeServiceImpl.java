package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserMapper;
import com.mall.mapper.CardGradeMapper;
import com.mall.model.BaseUser;
import com.mall.model.CardGrade;
import com.mall.service.BaseUserService;
import com.mall.service.CardGradeService;
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
