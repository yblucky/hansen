package com.api.service;

import com.api.constant.GradeRecordType;
import com.api.core.service.CommonService;
import com.api.model.User;
import com.api.model.UserGradeRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface UserGradeRecordService extends CommonService<UserGradeRecord> {


    @Transactional
    void addGradeRecord(User user, GradeRecordType recordType, Integer historyGrade);
}
