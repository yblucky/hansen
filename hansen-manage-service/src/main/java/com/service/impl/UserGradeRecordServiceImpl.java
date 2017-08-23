package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.GradeRecordType;
import com.mappers.UserGradeRecordMapper;
import com.service.UserGradeRecordService;
import com.model.User;
import com.model.UserGradeRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
@Service
public class UserGradeRecordServiceImpl extends CommonServiceImpl<UserGradeRecord> implements UserGradeRecordService {
    @Autowired
    private UserGradeRecordMapper userGradeRecordMapper;

    @Override
    protected CommonDao<UserGradeRecord> getDao() {
        return userGradeRecordMapper;
    }

    @Override
    protected Class<UserGradeRecord> getModelClass() {
        return UserGradeRecord.class;
    }

    @Transactional
    public void addGradeRecord(User user, GradeRecordType recordType, Integer historyGrade, Integer upGradeType, String orderNo){
        UserGradeRecord record = new UserGradeRecord();
        record.setUserId(user.getId());
        record.setCurrencyGrade(user.getGrade());
        record.setRecordType(recordType.getCode());
        record.setUpGradeType(upGradeType);
        record.setRemark(recordType.getMsg());
        record.setHistoryGrade(historyGrade);
        record.setOrderNo(orderNo);
        record.setAmt(user.getInsuranceAmt());
        this.create(record);
    }
}
