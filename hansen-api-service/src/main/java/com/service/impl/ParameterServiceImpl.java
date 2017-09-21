package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.Constant;
import com.mapper.ParameterMapper;
import com.model.Parameter;
import com.service.ParamUtil;
import com.service.ParameterService;
import com.utils.httputils.HttpUtil;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.ToolUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/6/20.
 */
@Service
public class ParameterServiceImpl extends CommonServiceImpl<Parameter> implements ParameterService {
    private static final Logger logger = LoggerFactory.getLogger(ParameterServiceImpl.class);

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
            if (ToolUtil.isEmpty(name)) {
                logger.error("获取交易平台" + name + "汇率失败");
            }
            Double kyp = getKyPRate();
            String url = ParamUtil.getIstance().get(Parameter.RMBCONVERTCOINSCALEBASEURL) + name;
            JSONObject jsonObject = HttpUtil.doGetRequest(url);
            if (jsonObject == null) {
                return 1d;
            }
            if (jsonObject.containsKey("24H_Last_price")) {
                Double last24Price = jsonObject.getDouble("24H_Last_price");
                if (last24Price == null) {
                    last24Price = 1d;
                }
                return CurrencyUtil.divide(1, last24Price * kyp, 4);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取交易平台" + name + "汇率失败");
        }
        return 0d;
    }

    @Override
    public Double getKyPRate() {
        try {
            String url = ParamUtil.getIstance().get(Parameter.RMBCONVERTCOINSCALEBASEURL) + Constant.KYP;
            JSONObject jsonObject = HttpUtil.doGetRequest(url);
            if (jsonObject == null) {
                return 1d;
            }
            if (jsonObject.containsKey("24H_Last_price")) {
                Double last24Price = jsonObject.getDouble("24H_Last_price");
                if (last24Price == null) {
                    last24Price = 1d;
                }
                return last24Price;
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取交易平台" + Constant.KYP + "汇率失败");
        }
        return 0d;
    }


    @Override
    public Map<String, Object> getScale() {
        try {
            Map<String, Object> map = new HashedMap();
            //人民币兑换支付币汇率
            Double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
            //人民币兑换交易币汇率
            Double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
            //人民币兑换交易币汇率
            Double equityScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTEQUITYSCALE), 0d);
            map.put(Constant.RMB_CONVERT_PAY_SCALE, payScale);
            map.put(Constant.RMB_CONVERT_TRADE_SCALE, tradeScale);
            map.put(Constant.RMB_CONVERT_EQUITY_SCALE, equityScale);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("从系统参数表查询汇率出错");
        }
        return null;
    }

    @Override
    public Double getScale(String key) {
        Map<String, Object> map = this.getScale();
        if (ToolUtil.isEmpty(key)) {
            if (map.containsKey(key)) {
                return (Double) map.get(key);
            }
        }
        return 1d;
    }

}
