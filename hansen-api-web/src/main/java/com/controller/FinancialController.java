package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.model.CardGrade;
import com.model.User;
import com.service.CardGradeService;
import com.service.UserService;
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
    private CardGradeService cardGradeService;

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
        Double tradeRate=10000d;
        //人民币对股权币汇率
        Double equityRate=10000d;
        //人民币对支付币汇率
        Double payRate=10000d;


        //总资产
        Double sumRmbAmt=40000d;
        //支付币数量
        Double payAmt=10000d;
        //股权币数量
        Double equityAmt=10000d;
        //交易币数量
        Double tradeAmt=10000d;

        //股权币数量人民币市值
        Double equityRmbAmt=10000d;
        //支付币数量人民币市值
        Double payRmbAmt=10000d;
        //交易币数量人民币市值
        Double tradeRmbAmt=10000d;
        //待释放奖金
        Double waitReleaseRmbAmt=10000d;


        //币总支出
        //支付币数量
        Double paySumOutAmt=10000d;
        //股权币数量
        Double equitySumOutAmt=10000d;
        //交易币数量
        Double tradeSumOutAmt=10000d;

        //币总收入
        //支付币数量
        Double paySumInAmt=10000d;
        //股权币数量
        Double equitySumInAmt=10000d;
        //交易币数量
        Double tradeSumInAmt=10000d;

        //奖金总额
        Double rewardSumAmt=10000d;
        //静态奖
        Double rewardStaticAmt=10000d;
        //推荐奖
        Double rewardPushAmt=10000d;
        //管理奖
        Double rewardManageAmt=10000d;
        //级差奖
        Double rewardDifferAmt=10000d;
        //已释放
        Double hasReleaseRmbAmt=10000d;
        //冻结
        Double frozenRmbAmt=10000d;

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
