package com.api.service.impl;

import com.api.constant.GradeRecordType;
import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.UserGradeRecordMapper;
import com.api.model.User;
import com.api.model.UserGradeRecord;
import com.api.service.UserGradeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public void addGradeRecord(User user, GradeRecordType recordType, Integer historyGrade){
        UserGradeRecord record = new UserGradeRecord();
        record.setUserId(user.getId());
        record.setCurrencyGrade(user.getGrade());
        record.setRecordType(recordType.getCode());
        record.setRemark(recordType.getMsg());
        record.setHistoryGrade(historyGrade);
        baseUserGradeRecordMapper.create(record);
    }
}
