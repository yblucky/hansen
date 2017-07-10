package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallOrderMapper;
import com.mall.model.*;
import com.mall.service.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author zhuzh
 * @date 2016年12月28日
 */
@Service
public class MallOrderServiceImpl extends CommonServiceImpl<MallOrder> implements MallOrderService {
	@Autowired
	private MallOrderMapper mallOrderDao;
	@Override
	protected CommonDao<MallOrder> getDao() {
		return mallOrderDao;
	}

	@Override
	protected Class<MallOrder> getModelClass() {
		return MallOrder.class;
	}

	@Override
	public PageResult<MallOrder> getPage(MallOrder model, Page page) throws Exception {
		PageResult<MallOrder> pageResult = new PageResult<MallOrder>();
		BeanUtils.copyProperties(pageResult, page);
		Integer count = mallOrderDao.readCount(model);
		pageResult.setTotalSize(count);
		if (count != null && count > 0) {
			List<MallOrder> list = mallOrderDao.readList(model,page.getStartRow(),page.getPageSize());
			if (CollectionUtils.isEmpty(list)) {
				list= Collections.EMPTY_LIST;
			}
			pageResult.setRows(list);
		}
		return pageResult;
	}
}
