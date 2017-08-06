package com.hansen.service;

import com.base.service.CommonService;
import com.common.constant.GradeRecordType;
import com.model.User;
import com.model.UserGradeRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface UserGradeRecordService extends CommonService<UserGradeRecord> {


    @Transactional
    void addGradeRecord(User user, GradeRecordType recordType, Integer historyGrade);
}
