package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.model.CardGrade;
import com.model.Parameter;
import com.model.User;
import com.service.CardGradeService;
import com.service.ParamUtil;
import com.service.UserService;
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
    @Autowired
    private UserService userService;

    /**
     * 获取等级信息
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult list(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //判断手机号是否存在
        CardGrade conditon = new CardGrade();
        List<CardGrade> modelList = cardGradeService.readList(conditon, 1, 30, 1000);
        if (ToolUtil.isEmpty(modelList)) {
            modelList = Collections.emptyList();
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
        Double payRmbAmt = CurrencyUtil.getPoundage(model.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 4);
        Double tradeRmbAmt = CurrencyUtil.getPoundage(model.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 4);
        //人民币兑换支付币汇率
        double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
        //人民币兑换交易币汇率
        double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
        cardGradeVo.setPayAmt(CurrencyUtil.multiply(payRmbAmt, payScale, 4));
        cardGradeVo.setTradeAmt(CurrencyUtil.multiply(tradeRmbAmt, tradeScale, 4));
        cardGradeVo.setEquityAmt(0d);
        cardGradeVo.setMaxProfits(CurrencyUtil.multiply(model.getInsuranceAmt(), model.getOutMultiple(), 4));
        return new JsonResult(cardGradeVo);
    }


    /**
     * 补差价获取等级信息
     *
     * @param request
     * @param response
     * @param cardGrade 补差价用户要升级的目标等级
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/differInfo", method = RequestMethod.GET)
    public JsonResult differInfo(HttpServletRequest request, HttpServletResponse response, Integer cardGrade) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (StringUtils.isEmpty(cardGrade)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "等级不能为空");
        }

        //判断手机号是否存在
        CardGrade conditon = new CardGrade();
        conditon.setGrade(cardGrade);
        CardGrade targetModel = cardGradeService.readOne(conditon);
        if (targetModel == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "开卡等级不存在 ");
        }
        //会员的本身等级
        CardGrade userCardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        //计算差价
        Integer differRegisterNo = targetModel.getRegisterCodeNo() - userCardGrade.getRegisterCodeNo();
        Integer differActiceNo = targetModel.getActiveCodeNo() - userCardGrade.getActiveCodeNo();
        Double insuranceAmt = targetModel.getInsuranceAmt() - userCardGrade.getInsuranceAmt();
        Double differPayRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 4);
        Double differTradeRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 4);
        //人民币兑换支付币汇率
        double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
        //人民币兑换交易币汇率
        double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
        CardGradeVo cardGradeVo = new CardGradeVo();
        //人民币兑换支付币汇率
        cardGradeVo.setPayAmt(CurrencyUtil.multiply(differPayRmbAmt, payScale, 4));
        cardGradeVo.setTradeAmt(CurrencyUtil.multiply(differTradeRmbAmt, tradeScale, 4));
        cardGradeVo.setEquityAmt(0d);
        cardGradeVo.setActiveCodeNo(differActiceNo);
        cardGradeVo.setRegisterCodeNo(differRegisterNo);
        Double targetMaxProfit = CurrencyUtil.multiply(targetModel.getInsuranceAmt(), targetModel.getOutMultiple(), 4);
        cardGradeVo.setMaxProfits(CurrencyUtil.add(targetMaxProfit, user.getMaxProfits(), 4));
        return new JsonResult(cardGradeVo);
    }
}
