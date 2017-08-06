package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.UserGradeRecordMapper;
import com.hansen.service.UserGradeRecordService;
import com.model.UserGradeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class UserGradeRecordServiceImpl extends CommonServiceImpl<UserGradeRecord> implements UserGradeRecordService {
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
