package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.constant.Constant;
import com.constant.OrderStatus;
import com.constant.OrderType;
import com.model.CardGrade;
import com.model.Parameter;
import com.model.User;
import com.service.*;
import com.utils.numberutils.CurrencyUtil;
import com.utils.thirdutils.QRCodeUtil;
import com.utils.toolutils.ToolUtil;
import com.utils.toolutils.ValidateUtils;
import com.vo.CardGradeVo;
import com.vo.InnerRegisterUserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.Map;

/**
 *
 * 财务中心
 *
 * Created   on 2017/9/12 0012.
 */
@Controller
@RequestMapping("/financial")
public class FinancialController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserSignService userSignService;

    @Autowired
    private TradeOrderService tradeOrderService;

    @Autowired
    private ParameterService parameterService;

    /**
     *
     *财务中心
     *
     */
    @ResponseBody
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public JsonResult info(HttpServletRequest request) throws Exception {

        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        // 人民币对交易币汇率
        Double tradeRate=parameterService.getScale(Constant.RMB_CONVERT_TRADE_SCALE);
        //人民币对股权币汇率
        Double equityRate=parameterService.getScale(Constant.RMB_CONVERT_EQUITY_SCALE);
        //人民币对支付币汇率
        Double payRate=parameterService.getScale(Constant.RMB_CONVERT_PAY_SCALE);


        //总资产
        Double sumRmbAmt=40000d;
        //支付币数量
        Double payAmt=loginUser.getPayAmt();
        //股权币数量
        Double equityAmt=loginUser.getEquityAmt();
        //交易币数量
        Double tradeAmt=loginUser.getTradeAmt();

        //股权币数量人民币市值
        Double equityRmbAmt= CurrencyUtil.divide(equityAmt,equityRate,2);
        //支付币数量人民币市值
        Double payRmbAmt=CurrencyUtil.divide(payAmt,payRate,2);
        //交易币数量人民币市值
        Double tradeRmbAmt=CurrencyUtil.divide(tradeAmt,tradeRate,2);
        //待释放奖金
        Double waitReleaseRmbAmt=0d;
        Double waitManageRmbAmt=tradeOrderService.readWaiteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),1, OrderType.MANAGE.getCode());
        Double waitPushRmbAmt=tradeOrderService.readWaiteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),4, OrderType.PUSH.getCode());
        Double waitDifferRmbAmt=tradeOrderService.readWaiteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),4, OrderType.DIFFERENT.getCode());
        Double waitSameRmbAmt=tradeOrderService.readWaiteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),4, OrderType.SAME.getCode());

        waitReleaseRmbAmt+=waitManageRmbAmt;
        waitReleaseRmbAmt+=waitPushRmbAmt;
        waitReleaseRmbAmt+=waitDifferRmbAmt;
        waitReleaseRmbAmt+=waitSameRmbAmt;


        //币总支出
        //支付币数量
        Double paySumOutAmt=0d;
        //股权币数量
        Double equitySumOutAmt=0d;
        //交易币数量
        Double tradeSumOutAmt=0d;

        //币总收入
        //支付币数量
        Double paySumInAmt=0d;
        //股权币数量
        Double equitySumInAmt=0d;
        //交易币数量
        Double tradeSumInAmt=0d;

        //奖金总额
        Double rewardSumAmt=userSignService.readSignCount(loginUser.getId());
        Double rewardAllCompeleterReleaseRmbAmt=tradeOrderService.readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), OrderType.RELASE.getCode());
        Double rewardAllCompeleteSameRmbAmt=tradeOrderService.readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), OrderType.SAME.getCode());
        Double rewardAllCompeleteManageRmbAmt=tradeOrderService.readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), OrderType.MANAGE.getCode());
        Double rewardAllCompeletePushRmbAmt=tradeOrderService.readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), OrderType.PUSH.getCode());
        Double rewardAllCompeleteDifferRmbAmt=tradeOrderService.readHasAllCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), OrderType.DIFFERENT.getCode());

        Double rewardPartCompeleterReleaseRmbAmt=tradeOrderService.readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),1, OrderType.RELASE.getCode());
        Double rewardPartCompeleteSameRmbAmt=tradeOrderService.readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),4, OrderType.SAME.getCode());
        Double rewardPartCompeleteManageRmbAmt=tradeOrderService.readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), 1,OrderType.MANAGE.getCode());
        Double rewardPartCompeletePushRmbAmt=tradeOrderService.readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(), 4,OrderType.PUSH.getCode());
        Double rewardPartCompeleteDifferRmbAmt=tradeOrderService.readHasPartCompeleteSumDynamicProfitsCountByReceviceUserIdAndSourceAndStatus(loginUser.getId(),4, OrderType.DIFFERENT.getCode());

        //静态奖
        Double rewardStaticAmt=0d;
        rewardStaticAmt+=rewardAllCompeleterReleaseRmbAmt;
        rewardStaticAmt+=rewardPartCompeleterReleaseRmbAmt;
        //推荐奖
        Double rewardPushAmt=0d;
        rewardPushAmt+=rewardAllCompeletePushRmbAmt;
        rewardPushAmt+=rewardPartCompeletePushRmbAmt;
        //管理奖
        Double rewardManageAmt=0d;
        rewardManageAmt+=rewardAllCompeleteManageRmbAmt;
        rewardManageAmt+=rewardPartCompeleteManageRmbAmt;
        //级差奖
        Double rewardDifferAmt=0d;
        rewardDifferAmt+=rewardAllCompeleteDifferRmbAmt;
        rewardDifferAmt+=rewardPartCompeleteDifferRmbAmt;
        //平级奖
        Double rewardSameAmt=0d;
        rewardSameAmt+=rewardPartCompeleteSameRmbAmt;
        rewardSameAmt+=rewardAllCompeleteSameRmbAmt;
        //已释放
        Double hasReleaseRmbAmt=0d;
        hasReleaseRmbAmt+=rewardStaticAmt;
        hasReleaseRmbAmt+=rewardManageAmt;
        hasReleaseRmbAmt+=rewardDifferAmt;
        hasReleaseRmbAmt+=rewardPushAmt;
        hasReleaseRmbAmt+=rewardSameAmt;
        //冻结
        Double frozenRmbAmt=userSignService.readSumFrozenCount(loginUser.getId());

        Map  map= new HashedMap();
        map.put("sumRmbAmt",sumRmbAmt);
        map.put("payAmt",payAmt);
        map.put("equityAmt",equityAmt);
        map.put("tradeAmt",tradeAmt);
        map.put("equityRmbAmt",equityRmbAmt);
        map.put("payRmbAmt",payRmbAmt);
        map.put("tradeRmbAmt",tradeRmbAmt);
        map.put("waitReleaseRmbAmt",waitReleaseRmbAmt);
        map.put("paySumOutAmt",paySumOutAmt);
        map.put("equitySumOutAmt",equitySumOutAmt);
        map.put("tradeSumOutAmt",tradeSumOutAmt);
        map.put("paySumInAmt",paySumInAmt);
        map.put("tradeSumInAmt",tradeSumInAmt);
        map.put("equitySumInAmt",equitySumInAmt);
        map.put("rewardSumAmt",rewardSumAmt);
        map.put("rewardStaticAmt",rewardStaticAmt);
        map.put("rewardPushAmt",rewardPushAmt);
        map.put("rewardManageAmt",rewardManageAmt);
        map.put("rewardDifferAmt",rewardDifferAmt);
        map.put("hasReleaseRmbAmt",hasReleaseRmbAmt);
        map.put("frozenRmbAmt",frozenRmbAmt);
        return new JsonResult(map);
    }


}
