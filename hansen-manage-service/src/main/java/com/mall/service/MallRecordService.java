package com.mall.service;

import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.core.service.CommonService;
import com.mall.model.MallRecord;

import java.util.List;

/**
 * @date 2016年11月27日
 */
public interface MallRecordService extends CommonService<MallRecord> {

    /**
     * 分页查询财务流水记录
     * @param model
     * @param page
     * @return
     */
    PageResult<MallRecord> getPage(MallRecord model, Page page) throws Exception;

}
