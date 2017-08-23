package com.hansen.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.hansen.mapper.ParameterMapper;
import com.hansen.service.ParameterService;
import com.utils.ParamUtil;
import com.utils.httputils.HttpUtil;
import com.model.Parameter;
import net.sf.json.JSONObject;
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
     */
    public void processCacheAfterUpdateById(String id, Parameter model) {
        ParamUtil.getIstance().reloadParam();
    }

    @Override
    public Double getRmbConvertCoinRate(String id, String name) {
        try {
            String url = ParamUtil.getIstance().get(Parameter.RMBCONVERTCOINSCALEBASEURL) + "";
            JSONObject jsonObject = HttpUtil.doGetRequest(url);
            if (jsonObject == null) {
                return 0d;
            }
            if (jsonObject.containsKey("24H_Last_price")) {
                Double last24Price = jsonObject.getDouble("24H_Last_price");
                if (last24Price == null) {
                    last24Price = 0d;
                }
                return last24Price;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取交易平台" + name + "汇率失败");
        }
        return 0d;
    }
}
