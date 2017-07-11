package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.UserGradeRecordMapper;
import com.manage.model.UserGradeRecord;
import com.manage.service.UserGradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseUserGradeRecordServiceImpl extends CommonServiceImpl<UserGradeRecord> implements UserGradeRecordService {
    @Autowired
    private UserGradeRecordMapper baseUserGradeRecordMapper;

    @Override
    protected CommonDao<UserGradeRecord> getDao() {
        return baseUserGradeRecordMapper;
    }

    @Override
    protected Class<UserGradeRecord> getModelClass() {
        return UserGradeRecord.class;
    }

}
