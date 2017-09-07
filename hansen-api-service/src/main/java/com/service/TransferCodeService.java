package com.service;

import com.base.page.Page;
import com.base.service.CommonService;
import com.model.TransferCode;

import java.util.List;

/**
 * @date 2017年08月15日
 */
public interface TransferCodeService extends CommonService<TransferCode> {
    Integer readCountByUserId(String userId,Integer codeType);

    List<TransferCode> readListByUserId(String userId, Integer codeType,Page page);

}
