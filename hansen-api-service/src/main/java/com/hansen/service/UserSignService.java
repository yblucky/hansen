package com.hansen.service;

import com.base.service.CommonService;
import com.model.UserSign;

/**
 * @date 2017年08月15日
 */
public interface UserSignService extends CommonService<UserSign> {

    Boolean UserSign

    addUserSign(String userId, Double amt,String signType,String status);

}
