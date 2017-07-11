package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseUserGradeRecordMapper;
import com.mall.model.BaseUserGradeRecord;
import com.mall.service.BaseUserGradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserGradeRecordServiceImpl extends CommonServiceImpl<BaseUserGradeRecord> implements BaseUserGradeRecordService {
    @Autowired
    private BaseUserGradeRecordMapper baseUserGradeRecordMapper;

    @Override
    protected CommonDao<BaseUserGradeRecord> getDao() {
        return baseUserGradeRecordMapper;
    }

    @Override
    protected Class<BaseUserGradeRecord> getModelClass() {
        return BaseUserGradeRecord.class;
    }

}
