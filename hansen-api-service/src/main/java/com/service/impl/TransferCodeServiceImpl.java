package com.service.impl;

import com.base.dao.CommonDao;
import com.base.page.Page;
import com.base.service.impl.CommonServiceImpl;
import com.mapper.TransferCodeMapper;
import com.service.TransferCodeService;
import com.model.TransferCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public Integer readCountByUserId(String userId,Integer codeType) {
        return transferCodeMapper.readCountByUserId(userId,codeType);
    }

    @Override
    public List<TransferCode> readListByUserId(String userId,Integer codeType, Page page) {
        return transferCodeMapper.readListByUserId(userId,codeType,page.getStartRow(),page.getPageSize());
    }
}
