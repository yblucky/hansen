package com.service.impl;

import com.base.dao.CommonDao;
import com.base.service.impl.CommonServiceImpl;
import com.constant.Constant;
import com.mapper.ParameterMapper;
import com.model.Parameter;
import com.service.ParamUtil;
import com.service.ParameterService;
import com.utils.httputils.HttpUtil;
import com.utils.toolutils.ToolUtil;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public void processCacheAfterCreate(Parameter model) {
        ParamUtil.getIstance().reloadParam();
    }

    /**
     * 修改系统参数成功后，重新加载系统参数
     */
    public void processCacheAfterDelete(String id) {
        ParamUtil.getIstance().reloadParam();
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

    @Override
    public Map<String, Object> getScale() {
        try {
            Map<String, Object> map = new HashedMap();
            //人民币兑换支付币汇率
            Double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
            //人民币兑换交易币汇率
            Double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
            //人民币兑换股权汇率
            Double rmbConvertEquityScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTEQUITYSCALE), 0d);
            //收益转化成股权币数量比例
            Double rewardConvertEquityScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTEQUITYSCALE), 0d);
            //收益转化成交易币数量比例
            Double rewardConvertTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTTRADESCALE), 0d);
            //收益转化成支付币数量比例
            Double rewardConvertPayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.REWARDCONVERTPAYSCALE), 0d);
            map.put(Constant.RMB_CONVERT_PAY_SCALE, payScale);
            map.put(Constant.RMB_CONVERT_TRADE_SCALE, tradeScale);
            map.put(Parameter.RMBCONVERTEQUITYSCALE, tradeScale);
            map.put(Parameter.REWARDCONVERTEQUITYSCALE, rewardConvertEquityScale);
            map.put(Parameter.REWARDCONVERTTRADESCALE, rewardConvertTradeScale);
            map.put(Parameter.REWARDCONVERTPAYSCALE, rewardConvertPayScale);
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
