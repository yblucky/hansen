package com.controller;

import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.model.CardGrade;
import com.model.Parameter;
import com.service.CardGradeService;
import com.service.ParamUtil;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.ToolUtil;
import com.vo.CardGradeVo;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 * 获取等级相关信息
 */
@Controller
@RequestMapping("/cardgrade")
public class CardGradeController {
    @Autowired
    private CardGradeService cardGradeService;

    /**
     * 获取等级信息
     *
     * @param request
     * @param response
     * @param cardGrade
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //判断手机号是否存在
        CardGrade conditon = new CardGrade();
        List<CardGrade> modelList = cardGradeService.readList(conditon,1,30,1000);
        if (ToolUtil.isEmpty(modelList)) {
            modelList= Collections.emptyList();
        }
        return new JsonResult(modelList);
    }


    /**
     * 获取等级信息
     *
     * @param request
     * @param response
     * @param cardGrade
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult info(HttpServletRequest request, HttpServletResponse response, Integer cardGrade) throws Exception {

        if (StringUtils.isEmpty(cardGrade)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "等级不能为空");
        }

        //判断手机号是否存在
        CardGrade conditon = new CardGrade();
        conditon.setGrade(cardGrade);
        CardGrade model = cardGradeService.readOne(conditon);
        if (model == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "开卡等级不存在 ");
        }
        CardGradeVo cardGradeVo = new CardGradeVo();
        BeanUtils.copyProperties(cardGradeVo, model);
        Double payAmt = CurrencyUtil.getPoundage(model.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)),2);
        Double tradeAmt = CurrencyUtil.getPoundage(model.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)),2);
        cardGradeVo.setPayAmt(payAmt);
        cardGradeVo.setTradeAmt(tradeAmt);
        cardGradeVo.setEquityAmt(0d);
        return new JsonResult(cardGradeVo);
    }
}
