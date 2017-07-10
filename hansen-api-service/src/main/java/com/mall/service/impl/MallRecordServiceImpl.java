package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallRecordMapper;
import com.mall.model.MallRecord;
import com.mall.service.MallRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ZHUZIHUI
 * @date 2016年11月27日
 */
@Service
public class MallRecordServiceImpl extends CommonServiceImpl<MallRecord> implements MallRecordService {
    @Autowired
    private MallRecordMapper mallRecordDao;

    @Override
    protected CommonDao<MallRecord> getDao() {
        return mallRecordDao;
    }

    @Override
    protected Class<MallRecord> getModelClass() {
        return MallRecord.class;
    }
}
