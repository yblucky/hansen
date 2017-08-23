package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.FeedBackMapper;
import com.model.FeedBack;
import com.service.FeedBackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zzwei
 * @date 2017年06月27日
 */
@Service
public class FeedBackServiceImpl extends CommonServiceImpl<FeedBack> implements FeedBackService {

    @Autowired
    private FeedBackMapper feedBackMapper;


    @Override
    protected CommonDao<FeedBack> getDao() {
        return feedBackMapper;
    }

    @Override
    protected Class<FeedBack> getModelClass() {
        return FeedBack.class;
    }


}
