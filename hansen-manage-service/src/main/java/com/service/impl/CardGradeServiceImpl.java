package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.CardGradeMapper;
import com.service.CardGradeService;
import com.model.CardGrade;
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

    /**
     * 获取用户保单级别
     * @param cardLevel 会员卡级别
     * */
    @Override
    public CardGrade getUserCardGrade(Integer cardLevel){
        CardGrade card = new CardGrade();
        card.setGrade(cardLevel);
        return cardGradeMapper.readOne(card);
    }
}
