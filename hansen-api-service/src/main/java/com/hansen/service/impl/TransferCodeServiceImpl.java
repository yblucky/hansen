package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.ActiveCodeMapper;
import com.hansen.mapper.TransferCodeMapper;
import com.hansen.service.ActiveCodeService;
import com.hansen.service.TransferCodeService;
import com.model.ActiveCode;
import com.model.TransferCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class TransferCodeServiceImpl extends CommonServiceImpl<TransferCode> implements TransferCodeService {
    @Autowired
    private TransferCodeMapper transferCodeMapper;

    @Override
    protected CommonDao<TransferCode> getDao() {
        return transferCodeMapper;
    }

    @Override
    protected Class<TransferCode> getModelClass() {
        return TransferCode.class;
    }

}
