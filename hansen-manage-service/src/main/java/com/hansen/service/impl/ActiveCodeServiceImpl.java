package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.ActiveCodeMapper;
import com.hansen.service.ActiveCodeService;
import com.model.ActiveCode;
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
