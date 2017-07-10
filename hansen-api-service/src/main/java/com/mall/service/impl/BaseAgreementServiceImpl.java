package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.BaseAgreementMapper;
import com.mall.model.BaseAgreement;
import com.mall.service.BaseAgreementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class BaseAgreementServiceImpl extends CommonServiceImpl<BaseAgreement> implements BaseAgreementService {

    @Autowired
    private BaseAgreementMapper agreementDao;


    @Override
    protected CommonDao<BaseAgreement> getDao() {
        return agreementDao;
    }

    @Override
    protected Class<BaseAgreement> getModelClass() {
        return BaseAgreement.class;
    }
}
