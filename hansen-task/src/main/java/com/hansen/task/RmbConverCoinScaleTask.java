package com.hansen.task;

import com.hansen.service.ParameterService;
import com.model.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 保单结算定时器
 *
 * @date 2016年12月30日
 */
@Component
public class RmbConverCoinScaleTask extends BaseScheduleTask {
    private static final Logger logger = LoggerFactory.getLogger(RmbConverCoinScaleTask.class);
    @Autowired
    private ParameterService parameterService;


    @Override
    protected void doScheduleTask() {
        Parameter conditon = new Parameter();
        conditon.setName(Parameter.RMBCONVERTCOINSCALEBASEURL);
        Parameter model = parameterService.readOne(conditon);
        Parameter updateModel = new Parameter();
        if (model == null) {
            return;
        }
        conditon.setName(Parameter.RMBCONVERTPAYSCALE);
        Parameter payModel = parameterService.readOne(conditon);
        if (payModel != null) {
            //请求交易平台获取数据
            updateRmbConvertCoinRate(updateModel, payModel, "HQC");
        }
        conditon.setName(Parameter.RMBCONVERTTRADESCALE);
        Parameter tradeModel = parameterService.readOne(conditon);
        if (tradeModel != null) {
            updateRmbConvertCoinRate(updateModel, tradeModel, "HQC");
        }
        conditon.setName(Parameter.RMBCONVERTEQUITYSCALE);
        Parameter equityModel = parameterService.readOne(conditon);
        if (equityModel != null) {
            updateRmbConvertCoinRate(updateModel, equityModel, "KYP");
        }
    }

    private void updateRmbConvertCoinRate(Parameter updateModel, Parameter model, String currencyType) {
        Double rate = parameterService.getRmbConvertCoinRate(model.getId(), currencyType);
        updateModel.setName(model.getName());
        updateModel.setValue(rate.toString());
        parameterService.updateById(model.getId(), updateModel);
    }


}
