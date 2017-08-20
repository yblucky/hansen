package com.hansen.controller;

import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.common.Token;
import com.common.base.TokenUtil;
import com.common.constant.*;
import com.common.utils.DateUtils.DateUtils;
import com.common.utils.WalletUtil;
import com.common.utils.codeutils.Md5Util;
import com.common.utils.numberutils.CurrencyUtil;
import com.common.utils.toolutils.OrderNoUtil;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.service.*;
import com.hansen.vo.InnerRegisterUserVo;
import com.hansen.vo.LoginUserVo;
import com.hansen.vo.UpgradeUserVo;
import com.hansen.vo.UserVo;
import com.model.*;
import com.redis.Strings;
import org.apache.commons.beanutils.BeanUtils;
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
import java.util.List;

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
    @Autowired
    private UserGradeRecordService userGradeRecordService;


    /**
     * 外部用户注册默认账号  等待完善
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
        user.setStatus(UserStatusType.OUTREGISTER_SUCCESSED.getCode());
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
        userDetail.setId(user.getId());
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
     * 市场人员内部注册账号，扣除市场人员的注册码，一并扣除激活码
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/innercreateuser", method = RequestMethod.POST)
    public JsonResult innerCreateUser(HttpServletRequest request, @RequestBody InnerRegisterUserVo vo) throws Exception {
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
        if (ToolUtil.isEmpty(vo.getPayWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户支付密码不能为空");
        }
        if (ToolUtil.isEmpty(vo.getConfirmPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认登录密码不能为空");
        }
        if (ToolUtil.isEmpty(vo.getConfirmpayWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认支付密码不能为空");
        }
        if (vo.getConfirmPassword().equals(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认两次登录密码不一致");
        }
        if (vo.getConfirmpayWord().equals(vo.getPayWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认两次支付密码不一致");
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
        /**扣激活码**/
        User updateUser = new User();
        updateUser.setId(loginUser.getId());
        updateUser.setActiveCodeNo(loginUser.getActiveCodeNo() - cardGrade.getActiveCodeNo());
        updateUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        userService.updateById(updateUser.getId(), updateUser);
        //冻结账号虚拟币 激活账号
        User updateActiveUser = new User();
        updateActiveUser.setId(activeUser.getId());
        updateActiveUser.setInsuranceAmt(cardGrade.getInsuranceAmt());
        //TODO 扣减账号币数量 对应人民币市值换算
        updateActiveUser.setTradeAmt(activeUser.getTradeAmt() - cardGrade.getInsuranceAmt() * 50 / 100);
        updateActiveUser.setPayAmt(activeUser.getPayAmt() - cardGrade.getInsuranceAmt() * 50 / 100);
        updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        //写入最大收益
        updateActiveUser.setMaxProfits(cardGrade.getOutMultiple() * cardGrade.getInsuranceAmt());
        updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        userService.updateById(updateActiveUser.getId(), updateActiveUser);

        UserDetail activeUserDetail = userDetailService.readById(activeUser.getId());
        //写入冻结
        UserDetail updateActiveUserDetailContion = new UserDetail();
        updateActiveUserDetailContion.setId(activeUserDetail.getId());
        updateActiveUserDetailContion.setForzenPayAmt(cardGrade.getInsuranceAmt() * 50 / 100);
        updateActiveUserDetailContion.setForzenTradeAmt(cardGrade.getInsuranceAmt() * 50 / 100);
        userDetailService.updateById(updateActiveUserDetailContion.getId(), updateActiveUserDetailContion);
        //生成保单
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setOrderNo(OrderNoUtil.get());
        tradeOrder.setAmt(cardGrade.getInsuranceAmt());
        tradeOrder.setSendUserId(activeUser.getId());
        tradeOrder.setReceviceUserId(Constant.SYSTEM_USER_ID);
        tradeOrder.setSource(OrderType.INSURANCE.getCode());
        tradeOrder.setRemark(OrderType.INSURANCE.getMsg());
        tradeOrder.setPayAmtScale(0.5);
        tradeOrder.setTradeAmtScale(0.5);
        tradeOrder.setEquityAmtScale(0d);
        tradeOrder.setConfirmAmt(0d);
        tradeOrder.setPoundage(0d);
        tradeOrder.setStatus(OrderStatus.PENDING.getCode());
        tradeOrderService.create(tradeOrder);
        return new JsonResult();
    }

    /**
     * 会员升级
     */
    @ResponseBody
    @RequestMapping(value = "/memberUpgrade", method = RequestMethod.POST)
    public JsonResult memberUpgrade(HttpServletRequest request, @RequestBody UpgradeUserVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (vo.getUpGradeWay() == null || (vo.getUpGradeWay() != UpGradeType.COVERAGEUPGRADE.getCode() && vo.getUpGradeWay() != UpGradeType.ORIGINUPGRADE.getCode())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择升级方式");
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(vo.getGrade());
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "升卡级别有误");
        }
        if (loginUser.getRegisterCodeNo() < cardGrade.getRegisterCodeNo() || loginUser.getActiveCodeNo() < cardGrade.getActiveCodeNo()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户激活码或注册码不足，请先补充激活码或注册码!");
        }

        /**校验虚拟币**/
        if (loginUser.getPayAmt() < CurrencyUtil.divide(cardGrade.getInsuranceAmt() * 50, 100, 4)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付币不足，无法激活");
        }
        if (loginUser.getTradeAmt() < CurrencyUtil.divide(cardGrade.getInsuranceAmt() * 50, 100, 4)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "交易币不足，无法激活");
        }

        /**扣激活码**/
        User updateUser = new User();
        updateUser.setId(loginUser.getId());
        updateUser.setActiveCodeNo(loginUser.getActiveCodeNo() - cardGrade.getActiveCodeNo());
        updateUser.setRegisterCodeNo(loginUser.getRegisterCodeNo() - cardGrade.getRegisterCodeNo());
        updateUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        userService.updateById(updateUser.getId(), updateUser);

        //冻结账号虚拟币 激活账号
        User updateActiveUser = new User();
        updateActiveUser.setId(loginUser.getId());
        //TODO 扣减账号币数量 对应人民币市值换算
        updateActiveUser.setTradeAmt(loginUser.getTradeAmt() - cardGrade.getInsuranceAmt() * 50 / 100);
        updateActiveUser.setPayAmt(loginUser.getPayAmt() - cardGrade.getInsuranceAmt() * 50 / 100);
        updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        userService.updateById(updateActiveUser.getId(), updateActiveUser);

        UserDetail activeUserDetail = userDetailService.readById(loginUser.getId());
        //写入冻结
        UserDetail updateActiveUserDetailContion = new UserDetail();
        updateActiveUserDetailContion.setId(activeUserDetail.getId());
        updateActiveUserDetailContion.setForzenPayAmt(cardGrade.getInsuranceAmt() * 50 / 100);
        updateActiveUserDetailContion.setForzenTradeAmt(cardGrade.getInsuranceAmt() * 50 / 100);
        userDetailService.updateById(updateActiveUserDetailContion.getId(), updateActiveUserDetailContion);

        if (vo.getUpGradeWay().intValue() == UpGradeType.ORIGINUPGRADE.getCode().intValue()) {
            userService.originUpgrade(loginUser.getId(), vo.getGrade());
        } else if (vo.getUpGradeWay().intValue() == UpGradeType.COVERAGEUPGRADE.getCode().intValue()) {
            userService.coverageUpgrade(loginUser.getId(), vo.getGrade());
        }
        return new JsonResult();
    }

    /**
     * 升级记录
     *
     * @param page       分页查询
     * @param upGradeWay 升级方式 1 原点升级 2 覆盖升级
     */
    @ResponseBody
    @RequestMapping(value = "/UpGradeRecord", method = RequestMethod.GET)
    public JsonResult UpGradeRecord(HttpServletRequest request, Page page, Integer upGradeWay) {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (upGradeWay == null || (upGradeWay != UpGradeType.COVERAGEUPGRADE.getCode() && upGradeWay != UpGradeType.ORIGINUPGRADE.getCode())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择升级方式");
        }
        UserGradeRecord model = new UserGradeRecord();
        model.setUserId(token.getId());
        model.setRecordType(GradeRecordType.CARDUPDATE.getCode());
        model.setUpGradeType(upGradeWay);
        int count = userGradeRecordService.readCount(model);
        List<UserGradeRecord> list = userGradeRecordService.readList(model, page.getPageNo(), page.getPageSize(), count);
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return new JsonResult(pageResult);
    }
}
