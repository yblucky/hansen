package com.hansen.service;

import com.hansen.base.service.CommonService;
import com.hansen.common.constant.GradeRecordType;
import com.hansen.model.User;
import com.hansen.model.UserGradeRecord;
import org.springframework.transaction.annotation.Transactional;

/**
 * @date 2016年11月27日
 */
public interface UserGradeRecordService extends CommonService<UserGradeRecord> {


    @Transactional
    void addGradeRecord(User user, GradeRecordType recordType, Integer historyGrade);
}
