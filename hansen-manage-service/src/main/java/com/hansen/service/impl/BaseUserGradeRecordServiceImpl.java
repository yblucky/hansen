package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.GradeRecordType;
import com.hansen.mapper.UserGradeRecordMapper;
import com.hansen.service.UserGradeRecordService;
import com.model.User;
import com.model.UserGradeRecord;
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