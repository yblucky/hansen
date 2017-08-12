package com.hansen.controller;

import com.base.page.JsonResult;
import com.common.Token;
import com.common.base.TokenUtil;
import com.common.constant.*;
import com.common.utils.DateUtils.DateUtils;
import com.common.utils.WalletUtil;
import com.common.utils.codeutils.Md5Util;
import com.common.utils.toolutils.OrderNoUtil;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.service.CardGradeService;
import com.hansen.service.TradeOrderService;
import com.hansen.service.UserDetailService;
import com.hansen.service.UserService;
import com.hansen.vo.LoginUserVo;
import com.hansen.vo.UserVo;
import com.model.CardGrade;
import com.model.TradeOrder;
import com.model.User;
import com.model.UserDetail;
import com.redis.Strings;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.paradoxs.bitcoin.client.BitcoinClient;

import javax.servlet.http.HttpServletRequest;

import static com.common.utils.WalletUtil.getBitCoinClient;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CardGradeService cardGradeService;
    @Autowired
    private TradeOrderService tradeOrderService;

    @ResponseBody
    @RequestMapping(value = "/token", method = RequestMethod.GET)
    public JsonResult loginByWeixin(HttpServletRequest request, String id) throws Exception {
        if (StringUtils.isBlank(id)) {
            return new JsonResult(-1, "id不能為空");
        }
        User user = userService.readById(id);
        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        user.setRemark(token);
        return new JsonResult(user);
    }

    /**
     * 注册默认账号
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public JsonResult register(HttpServletRequest request, @RequestBody LoginUserVo vo) throws Exception {
        if (ToolUtil.isEmpty(vo.getLoginName())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录名称不能为空");
        }
        if (ToolUtil.isEmpty(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录密码不能为空");
        }
        User regisUserContion = new User();
        regisUserContion.setLoginName(vo.getLoginName());
        User regisUser = userService.readOne(regisUserContion);
        if (regisUser != null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "此账号已被注册");
        }
        User user = new User();
        BeanUtils.copyProperties(user, vo);
        user.setStatus(UserStatusType.REGISTER.getCode());
        BitcoinClient payBitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
        BitcoinClient equityBitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
        BitcoinClient tradeBitcoinClient = getBitCoinClient("127.0.0.1", "user", "password", 20099);
        /**生成钱包地址**/
        String payAddress = WalletUtil.getAccountAddress(payBitcoinClient, vo.getLoginName());
        String equityAddress = WalletUtil.getAccountAddress(equityBitcoinClient, vo.getLoginName());
        String tradeAddress = WalletUtil.getAccountAddress(tradeBitcoinClient, vo.getLoginName());
        user.setPassword(Md5Util.MD5Encode(vo.getPassword(), DateUtils.currentDateToggeter()));
        user.setSalt(DateUtils.currentDateToggeter());
        userService.create(user);
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getId());
        userDetail.setInEquityAddress(equityAddress);
        userDetail.setInTradeAddress(tradeAddress);
        userDetail.setInPayAddress(payAddress);
        userDetail.setLevles(0);
        userDetailService.create(userDetail);

        // 登录
        String token = TokenUtil.generateToken(user.getId(), user.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + user.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userVo, vo);
        userVo.setToken(token);
        return new JsonResult(user);
    }


    /**
     * 市场人员内部注册账号
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/createuser", method = RequestMethod.POST)
    public JsonResult createUser(HttpServletRequest request, @RequestBody LoginUserVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());

        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (ToolUtil.isEmpty(vo.getLoginName())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户登录名称不能为空");
        }
        if (ToolUtil.isEmpty(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户登录密码不能为空");
        }
        if (vo.getCardGrade() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择开卡级别");
        }
        if (vo.getUid() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "邀请人uid不能为空");
        }
        User regisUserContion = new User();
        regisUserContion.setLoginName(vo.getLoginName());
        User regisUser = userService.readOne(regisUserContion);
        if (regisUser != null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "此账号已被注册");
        }
        CardGrade condition = new CardGrade();
        condition.setGrade(vo.getCardGrade());
        CardGrade cardGrade = cardGradeService.readOne(condition);
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "开卡级别有误");
        }
        if (loginUser.getActiveCodeNo() < cardGrade.getRegisterCodeNo()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "注册码个数不足");
        }
        User inviterCondition = new User();
        inviterCondition.setUid(vo.getUid());

        User inviterUser = userService.readOne(inviterCondition);
        if (inviterUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "邀请人信息有误");
        }

        User model = new User();
        BeanUtils.copyProperties(model, vo);
        userService.innerRegister(loginUser, inviterUser, model, cardGrade);
        return new JsonResult(model);
    }


    /**
     * 市场人员内部激活账号
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/activeuser", method = RequestMethod.POST)
    public JsonResult activeUser(HttpServletRequest request, @RequestBody LoginUserVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (ToolUtil.isEmpty(vo.getLoginName())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户登录名称不能为空");
        }
        if (ToolUtil.isEmpty(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户登录密码不能为空");
        }
        if (vo.getCardGrade() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请填写开卡级别");
        }
        CardGrade condition = new CardGrade();
        condition.setGrade(vo.getCardGrade());
        CardGrade cardGrade = cardGradeService.readOne(condition);
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "开卡级别有误");
        }
        User inviterCondition = new User();
        inviterCondition.setUid(vo.getUid());

        User inviterUser = userService.readOne(inviterCondition);
        if (inviterUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "邀请人信息有误");
        }
        //判断用户账号是否有足够的虚拟币
        User activeUserContion = new User();
        activeUserContion.setLoginName(vo.getLoginName());
        User activeUser = userService.readOne(activeUserContion);
        if (activeUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "激活账号信息有误");
        }
        if (!Md5Util.MD5Encode(vo.getPassword(), activeUser.getSalt()).equals(activeUser.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "激活账号密码有误");
        }
        /**校验虚拟币**/
        if (activeUser.getPayAmt() < cardGrade.getInsuranceAmt() * 50 / 100) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付币不足，无法激活");
        }
        if (activeUser.getTradeAmt() < cardGrade.getInsuranceAmt() * 50 / 100) {
            return new JsonResult(ResultCode.ERROR.getCode(), "交易币不足，无法激活");
        }
        userService.innerActicveUser(loginUser, activeUser,cardGrade);

        return new JsonResult();
    }



}
