package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallOrderDetailMapper;
import com.mall.model.MallOrderDetail;
import com.mall.service.MallOrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jay.zheng
 * @date 2017年05月03日
 */
@Service
public class MallOrderDetailServiceImpl extends CommonServiceImpl<MallOrderDetail> implements MallOrderDetailService {
    @Autowired
    private MallOrderDetailMapper mallOrderDetailDao;
    @Override
    protected CommonDao<MallOrderDetail> getDao() {
        return mallOrderDetailDao;
    }

    @Override
    protected Class<MallOrderDetail> getModelClass() {
        return MallOrderDetail.class;
    }
}
