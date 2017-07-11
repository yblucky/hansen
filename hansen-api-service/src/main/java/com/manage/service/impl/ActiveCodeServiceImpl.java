package com.manage.service.impl;

import com.manage.core.dao.CommonDao;
import com.manage.core.service.impl.CommonServiceImpl;
import com.manage.mapper.ActiveCodeMapper;
import com.manage.model.ActiveCode;
import com.manage.service.ActiveCodeService;
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
