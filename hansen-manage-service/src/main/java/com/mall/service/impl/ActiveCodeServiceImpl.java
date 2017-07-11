package com.mall.service.impl;

import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.ActiveCodeMapper;
import com.mall.model.ActiveCode;
import com.mall.service.ActiveCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @date 2016年11月27日
 */
@Service
public class ActiveCodeServiceImpl extends CommonServiceImpl<ActiveCode> implements ActiveCodeService {
    @Autowired
    private ActiveCodeMapper activeCodeMapper;

    @Override
    protected CommonDao<ActiveCode> getDao() {
        return activeCodeMapper;
    }

    @Override
    protected Class<ActiveCode> getModelClass() {
        return ActiveCode.class;
    }

}
