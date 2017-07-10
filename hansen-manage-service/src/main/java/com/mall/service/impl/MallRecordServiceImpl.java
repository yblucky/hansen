package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallRecordMapper;
import com.mall.model.MallRecord;
import com.mall.service.MallRecordService;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
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

    /**
     * 分页查询财务流水记录
     *
     * @param model
     * @param page
     * @return
     */
    @Override
    public PageResult<MallRecord> getPage(MallRecord model, Page page) throws Exception {
        PageResult<MallRecord> pageResult = new PageResult<MallRecord>();
        BeanUtils.copyProperties(pageResult, page);
        Integer count = mallRecordDao.readCount(model);
        pageResult.setTotalSize(count);
        if (count != null && count > 0) {
            List<MallRecord> list = mallRecordDao.readList(model, page.getStartRow(), page.getPageSize());
            if (CollectionUtils.isEmpty(list)) {
                list = Collections.EMPTY_LIST;
            }
            pageResult.setRows(list);
        }
        return pageResult;
    }
}
