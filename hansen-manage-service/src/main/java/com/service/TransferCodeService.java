package com.service;

import com.base.page.Page;
import com.base.service.CommonService;
import com.model.TransferCode;

import java.util.List;

/**
 * @date 2017年08月15日
 */
public interface TransferCodeService extends CommonService<TransferCode> {
    Integer readCountByUserId(String userId);

    List<TransferCode> readListByUserId(String userId, Page page);

}
