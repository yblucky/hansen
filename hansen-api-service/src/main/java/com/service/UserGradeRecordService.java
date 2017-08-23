package com.service;

import com.base.service.CommonService;
import com.constant.GradeRecordType;
import com.model.User;
import com.model.UserGradeRecord;

/**
 * @date 2016年11月27日
 */
public interface UserGradeRecordService extends CommonService<UserGradeRecord> {

    void addGradeRecord(User user, GradeRecordType recordType, Integer historyGrade, Integer upGradeType, String orderNo);
}
