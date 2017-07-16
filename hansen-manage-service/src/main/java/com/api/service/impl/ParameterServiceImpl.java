package com.api.service.impl;

import com.api.core.dao.CommonDao;
import com.api.core.service.impl.CommonServiceImpl;
import com.api.mapper.ParameterMapper;
import com.api.model.Parameter;
import com.api.service.ParameterService;
import com.api.util.ParamUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/6/20.
 */
@Service
public class ParameterServiceImpl extends CommonServiceImpl<Parameter> implements ParameterService {

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    protected CommonDao<Parameter> getDao() {
        return parameterMapper;
    }
    @Override
    protected Class<Parameter> getModelClass() {
        return Parameter.class;
    }

    @Override
    public List<Parameter> getList() throws Exception {
        return parameterMapper.getList();
    }
    /**
     * 修改系统参数成功后，重新加载系统参数
    * */
    public void processCacheAfterUpdateById(String id, Parameter model){
        ParamUtil.getIstance().reloadParam();
    }
}
