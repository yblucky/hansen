package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.*;
import com.model.*;
import com.redis.Strings;
import com.service.*;
import com.utils.DateUtils.DateUtils;
import com.utils.classutils.MyBeanUtils;
import com.utils.codeutils.Md5Util;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.RedisLock;
import com.utils.toolutils.ToolUtil;
import com.utils.toolutils.ValidateUtils;
import com.vo.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.StringUtils;
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
import java.util.*;

import static com.service.WalletUtil.getBitCoinClient;


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
    @Autowired
    private UserDepartmentService userDepartmentService;
    @Autowired
    private ParameterService parameterService;


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
        user.setStatus(UserStatusType.OUT_SHARE_REGISTER_SUCCESSED.getCode());
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
        if (ToolUtil.isEmpty(vo.getPayword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户支付密码不能为空");
        }
        if (ToolUtil.isEmpty(vo.getConfirmPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认登录密码不能为空");
        }
        if (ToolUtil.isEmpty(vo.getConfirmPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认支付密码不能为空");
        }
        if (!vo.getConfirmPassword().equals(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认两次登录密码不一致");
        }
        if (!vo.getConfirmPayWord().equals(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新建用户确认两次支付密码不一致");
        }
        if (vo.getCardGrade() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择开卡级别");
        }
        if (vo.getFirstReferrer() == null) {
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
//        if (loginUser.getRegisterCodeNo() < cardGrade.getRegisterCodeNo()) {
//            return new JsonResult(ResultCode.ERROR.getCode(), "注册码个数不足");
//        }
        if (loginUser.getActiveCodeNo() < cardGrade.getActiveCodeNo()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "消费码个数不足");
        }
        User inviterUser = null;
        if (vo.getContactUserId().intValue() != loginUser.getUid().intValue()) {
            User inviterCondition = new User();
            inviterCondition.setUid(vo.getContactUserId());
            inviterUser = userService.readOne(inviterCondition);
            if (inviterUser == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "接点人信息有误");
            }
            Boolean flag = userService.isVrticalLine(loginUser.getId(), inviterUser.getId());
            if (!flag) {
                return new JsonResult(ResultCode.ERROR.getCode(), "接点人必须注册在邀请人下");
            }
        } else {
            inviterUser = loginUser;
        }
        User model = new User();
        BeanUtils.copyProperties(model, vo);
        model.setPayWord(vo.getPayword());
        userService.innerRegister(loginUser, inviterUser, model, cardGrade);
        //返回用户uid和手机号
        User con = new User();
        con.setLoginName(vo.getLoginName());
        User resultUser = userService.readOne(regisUserContion);
        if (resultUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "注册成功,返回注册用户信息失败");
        }
        return new JsonResult(ResultCode.SUCCESS.getCode(), "注册成功", resultUser);
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
            return new JsonResult(ResultCode.ERROR.getCode(), "购物币不足，无法激活");
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(vo.getPayWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择输入支付密码");
        }
        if (!loginUser.getPassword().equals(Md5Util.MD5Encode(vo.getPayWord(), loginUser.getSalt()))) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付密码错误");
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(vo.getGrade());
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "升卡级别有误");
        }
        if (loginUser.getCardGrade().intValue() >= cardGrade.getGrade().intValue()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "升级只能从低往高升级");
        }
        //会员的本身等级
        CardGrade userCardGrade = cardGradeService.getUserCardGrade(loginUser.getCardGrade());
        //计算差价
        Integer differRegisterNo = cardGrade.getRegisterCodeNo() - userCardGrade.getRegisterCodeNo();
        Integer differActiceNo = cardGrade.getActiveCodeNo() - userCardGrade.getActiveCodeNo();
        Double insuranceAmt = cardGrade.getInsuranceAmt() - userCardGrade.getInsuranceAmt();
        Double differPayRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 4);
        Double differTradeRmbAmt = CurrencyUtil.getPoundage(insuranceAmt, Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 4);
        //人民币兑换支付币汇率
        double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
        //人民币兑换交易币汇率
        double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);


        if (UpGradeType.COVERAGEUPGRADE.getCode() == vo.getUpGradeWay()) {
            differActiceNo = cardGrade.getActiveCodeNo();
            differRegisterNo = cardGrade.getRegisterCodeNo();
            differPayRmbAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE)), 4);
            differTradeRmbAmt = CurrencyUtil.getPoundage(cardGrade.getInsuranceAmt(), Double.valueOf(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE)), 4);
        }
        Double differPayAmt = CurrencyUtil.multiply(differPayRmbAmt, payScale, 4);
        Double differTradeAmt = CurrencyUtil.multiply(differTradeRmbAmt, tradeScale, 4);
        if (loginUser.getRegisterCodeNo() < differActiceNo || loginUser.getActiveCodeNo() < differRegisterNo) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户消费码不足!");
        }
        /**校验虚拟币**/
        if (loginUser.getPayAmt() < differPayAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "购物币不足，无法激活");
        }
        if (loginUser.getTradeAmt() < differTradeAmt) {
            return new JsonResult(ResultCode.ERROR.getCode(), "交易币不足，无法激活");
        }
        Boolean f = RedisLock.redisLock(RedisKey.UPGRADE.getKey() + loginUser.getUid(), loginUser.getId(), RedisKey.UPGRADE.getSeconds());
        if (!f) {
            return new JsonResult(ResultCode.ERROR.getCode(), "正在处理，请不要重复请求");
        }
        Boolean flag = userService.upGrade(loginUser, cardGrade, UpGradeType.fromCode(vo.getUpGradeWay()));
        if (flag) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "升级报单成功，等待结算");
        }
        return new JsonResult(ResultCode.ERROR.getCode(), "升级失败，请联系客服");
    }


    /**
     * 升级记录
     *
     * @param page 分页查询
     */
    @ResponseBody
    @RequestMapping(value = "/upGradeRecord", method = RequestMethod.GET)
    public JsonResult UpGradeRecord(HttpServletRequest request, Page page) {
        try {
            Token token = TokenUtil.getSessionUser(request);
            User loginUser = userService.readById(token.getId());
            if (loginUser == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
            }
            /*if (upGradeWay == null || (upGradeWay != UpGradeType.COVERAGEUPGRADE.getCode() && upGradeWay != UpGradeType.ORIGINUPGRADE.getCode())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "请选择升级方式");
            }*/
            UserGradeRecord model = new UserGradeRecord();
            model.setUserId(token.getId());
            model.setRecordType(GradeRecordType.CARDUPDATE.getCode());
            /*if (upGradeWay == null || upGradeWay < 0) {
                model.setUpGradeType(upGradeWay);
            }*/
            Integer count = userGradeRecordService.readCount(model);
            List<UserGradeRecordVo> rslist = null;
            if (count != null && count > 0) {
                List<UserGradeRecord> list = userGradeRecordService.readList(model, page.getPageNo(), page.getPageSize(), count);
                UserGradeRecordVo po = null;
                rslist = new ArrayList<UserGradeRecordVo>();
                for (UserGradeRecord record : list) {
                    po = MyBeanUtils.copyProperties(record, UserGradeRecordVo.class);
                    po.setUpGradeType(record.getUpGradeType());
                    po.setHistoryGrade(record.getHistoryGrade());
                    po.setCurrencyGrade(record.getCurrencyGrade());
                    rslist.add(po);
                }
            }
            PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, rslist);
            return new JsonResult(pageResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonResult();
    }

    /**
     * 我的团队
     *
     * @param page 分页查询
     * @param
     */
    @ResponseBody
    @RequestMapping(value = "/myteam", method = RequestMethod.GET)
    public JsonResult myteam(HttpServletRequest request, Page page, String parentUserId) {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        //获取部门总业绩
        Double sumDeparmentPerformance = userDepartmentService.getSumDeparmentPerformanceByParentUserId(parentUserId);

        List<UserDepartment> userDepartments = userDepartmentService.getDirectTeamList(parentUserId);
        if (loginUser.getId().equals(parentUserId)) {
            for (UserDepartment department : userDepartments) {
                if (department.getGrade() == null || department.getGrade() == 0) {
                    department.setRemark("普通会员");
                } else {
                    department.setRemark(GradeType.getName(department.getGrade()));
                }

            }
        }
        if (userDepartments == null) {
            userDepartments = Collections.emptyList();
        }
        int count = userDepartments.size();
        PageResult<UserDepartment> pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, userDepartments);
        pageResult.getExtend().put("sumDeparmentPerformance", sumDeparmentPerformance);
        return new JsonResult(pageResult);
    }


    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public JsonResult updateUserInfo(HttpServletRequest request, @RequestBody UserVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        User updateUser = new User();
        UserDetail upateDetail = new UserDetail();
        if (ToolUtil.isNotEmpty(vo.getNickName())) {
            updateUser.setNickName(vo.getNickName());
        }
        if (ToolUtil.isNotEmpty(vo.getUserName())) {
            updateUser.setUserName(vo.getUserName());
        }
        if (ToolUtil.isNotEmpty(vo.getHeadImgUrl())) {
            updateUser.setHeadImgUrl(vo.getHeadImgUrl());
        }
        if (ToolUtil.isNotEmpty(vo.getContactUserId())) {
            updateUser.setNickName(vo.getContactUserId());
        }
        if (ToolUtil.isNotEmpty(vo.getPhone())) {
            updateUser.setPhone(vo.getPhone());
        }
        if (ToolUtil.isNotEmpty(vo.getEmail())) {
            updateUser.setEmail(vo.getEmail());
        }
        if (ToolUtil.isNotEmpty(vo.getOutEquityAddress())) {
            if (vo.getOutEquityAddress().length()<20 || vo.getOutEquityAddress().length()>40){
                return new JsonResult(ResultCode.ERROR.getCode(), "提币地址不合法");
            }
            upateDetail.setOutEquityAddress(vo.getOutEquityAddress());
        }
        if (ToolUtil.isNotEmpty(vo.getOutPayAddress())) {
            if (vo.getOutPayAddress().length()<20 || vo.getOutPayAddress().length()>40){
                return new JsonResult(ResultCode.ERROR.getCode(), "提币地址不合法");
            }
            upateDetail.setOutPayAddress(vo.getOutPayAddress());
        }
        if (ToolUtil.isNotEmpty(vo.getOutTradeAddress())) {
            if (vo.getOutTradeAddress().length()<20 || vo.getOutTradeAddress().length()>40){
                return new JsonResult(ResultCode.ERROR.getCode(), "提币地址不合法");
            }
            upateDetail.setOutTradeAddress(vo.getOutTradeAddress());
        }

        if (ToolUtil.isNotEmpty(vo.getUserName())) {
            upateDetail.setUserName(vo.getUserName());
        }

        if (ToolUtil.isNotEmpty(vo.getReceiver())) {
            upateDetail.setReceiver(vo.getReceiver());
        }

        if (ToolUtil.isNotEmpty(vo.getShopAddr())) {
            upateDetail.setShopAddr(vo.getShopAddr());
        }

        if (ToolUtil.isNotEmpty(vo.getBankCardNo())) {
            if (!ValidateUtils.checkBankCard(vo.getBankCardNo())){
                return new JsonResult(ResultCode.ERROR.getCode(), "银行卡号不合法");
            }
            upateDetail.setBankCardNo(vo.getBankCardNo());
        }

        if (ToolUtil.isNotEmpty(vo.getBankType())) {
            upateDetail.setBankType(vo.getBankType());
        }

        if (ToolUtil.isNotEmpty(vo.getReceiverPhone())) {
            if (!ValidateUtils.mobile(vo.getReceiverPhone())){
                return new JsonResult(ResultCode.ERROR.getCode(), "收货人手机号有误");
            }
            upateDetail.setReceiverPhone(vo.getReceiverPhone());
        }


        // 更新用户信息
        userService.updateById(loginUser.getId(), updateUser);
        userDetailService.updateById(loginUser.getId(), upateDetail);
        return new JsonResult(ResultCode.SUCCESS.getCode(), "保存成功");
    }


    /**
     * 获取等级信息
     *
     * @param grade 会员等级
     */
    @ResponseBody
    @RequestMapping(value = "/findUserCardGrade", method = RequestMethod.GET)
    public JsonResult findUserCardGrade(HttpServletRequest request, Integer grade) {
        try {
            Token token = TokenUtil.getSessionUser(request);
            User loginUser = userService.readById(token.getId());
            if (loginUser == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
            }
            if (grade == null || grade < 0) {
                return new JsonResult(ResultCode.ERROR.getCode(), "请选择升级类别");
            }

            CardGrade userCard = cardGradeService.getUserCardGrade(grade);
            //人民币兑换支付币汇率
            double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
            //人民币兑换交易币汇率
            double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
            //生成保单时支付币必须有的比例数量
            double insurancePayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE), 0d);
            //生成保单时交易币必须有的比例数量
            double insuranceTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE), 0d);
            //需要支付币的总金额
            double payAmount = payScale * userCard.getInsuranceAmt() * insurancePayScale;
            //需要交易币的总金额
            double tradeScaleAmount = tradeScale * userCard.getInsuranceAmt() * insuranceTradeScale;
            //需要补充激活码数量
            int needActiveNum = userCard.getActiveCodeNo() - loginUser.getActiveCodeNo();
            needActiveNum = needActiveNum > 0 ? needActiveNum : 0;
            //需要补充购物币数量
            Double needBuyNum = tradeScaleAmount - loginUser.getTradeAmt();
            needBuyNum = needBuyNum > 0 ? needBuyNum : 0d;
            //需要补充交易数量
            Double needChangeNum = payAmount - loginUser.getPayAmt();
            needChangeNum = needChangeNum > 0 ? needChangeNum : 0d;
            //最大收益
            Double cardMaxproft = userCard.getInsuranceAmt() * userCard.getOutMultiple();
            Map<String, String> rs = new HashedMap();
            rs.put("needActiveNum", needActiveNum + "");
            rs.put("needBuyNum", needBuyNum + "");
            rs.put("needChangeNum", needChangeNum + "");
            rs.put("cardMaxproft", cardMaxproft + "");
            return new JsonResult(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JsonResult();
    }

    /**
     * 修改用户密码信息
     */
    @ResponseBody
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    public JsonResult updatePwd(HttpServletRequest request, @RequestBody UpdateUserPwdVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (StringUtils.isEmpty(vo.getPwdType())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择修改密码方式");
        }
        if (StringUtils.isEmpty(vo.getOldPassWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "旧登录密码不能为空");
        }
        if (StringUtils.isEmpty(vo.getNewPassWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新登录密码不能为空");
        }
        if (StringUtils.isEmpty(vo.getConfirmPassWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "确认新登录密码不能为空");
        }
        if (!loginUser.getPassword().equals(Md5Util.MD5Encode(vo.getOldPassWord(), loginUser.getSalt()))) {
            return new JsonResult(ResultCode.ERROR.getCode(), "旧登录密码不正确");
        }
        if (!vo.getConfirmPassWord().trim().equals(vo.getNewPassWord().trim())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "确认新登录密码和新登录密码不一致");
        }

        User updateUser = new User();

        if ("1".equals(vo.getPwdType())) {
            //修改登录密码
            updateUser.setPassword(Md5Util.MD5Encode(vo.getConfirmPassWord(), loginUser.getSalt()));
        } else if ("2".equals(vo.getPwdType())) {
            //修改支付密码
            updateUser.setPayWord(Md5Util.MD5Encode(vo.getConfirmPassWord(), loginUser.getSalt()));
        }

        // 更新用户信息
        updateUser.setUpdateTime(new Date());
        userService.updateById(loginUser.getId(), updateUser);
        return new JsonResult();
    }

    /**
     * 忘记密码
     */
    @ResponseBody
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    public JsonResult updatePwd(HttpServletRequest request, @RequestBody ForgetPwdVo vo) throws Exception {

        if (StringUtils.isEmpty(vo.getPhoneNumber())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "手机号不能为空");
        }
        if (StringUtils.isEmpty(vo.getPhoneCode())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "验证码不能为空");
        }
        if (StringUtils.isEmpty(vo.getNewPassWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "新密码不能为空");
        }
        if (!vo.getNewPassWord().equals(vo.getConfirmPassWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "两次密码输入不一致");
        }
        String rsCode = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhoneNumber());
        if (org.springframework.util.StringUtils.isEmpty(rsCode) || !rsCode.equalsIgnoreCase(vo.getPhoneCode())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "验证码错误或者失效了");
        }

        User model = new User();
        model.setPhone(vo.getPhoneNumber());
        User loginUser = userService.readOne(model);
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "手机号未被绑定");
        }
        User updateUser = new User();
        //修改登录密码
        updateUser.setPassword(Md5Util.MD5Encode(vo.getConfirmPassWord(), loginUser.getSalt()));
        updateUser.setUpdateTime(new Date());

        //修改成功，删除验证码
        Strings.del(RedisKey.SMS_CODE.getKey() + vo.getPhoneNumber());

        // 更新用户信息
        userService.updateById(loginUser.getId(), updateUser);
        return new JsonResult();
    }


    /**
     * 用户手动点击激活账号:市场人员内部注册，分享注册
     */
    @ResponseBody
    @RequestMapping(value = "/firstActicveUser", method = RequestMethod.GET)
    public JsonResult firstActicveUser(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(ResultCode.NO_LOGIN.getCode(), ResultCode.NO_LOGIN.getMsg());
        }
        User loginUser = userService.readById(token.getId());
        JsonResult x = checkLoginUser(loginUser);
        if (x != null) return x;
        if (loginUser.getStatus() == UserStatusType.INNER_REGISTER_SUCCESSED.getCode()) {
            CardGrade cardGradeCondition = new CardGrade();
            cardGradeCondition.setGrade(loginUser.getCardGrade());
            CardGrade cardGrade = cardGradeService.readOne(cardGradeCondition);
            if (cardGrade == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "开卡级别有误");
            }
            User updateUser = new User();
            updateUser.setLoginTime(new Date());
            updateUser.setUpdateTime(new Date());
            userService.updateById(loginUser.getId(), updateUser);
            //如果用户状态是内部注册成功，已经代为扣除激活码的状态，则走此流程，此流程走完，满足条件的情况下，用户账号即被激活成功
            Boolean f = RedisLock.redisLock(RedisKey.ACTIVE.getKey() + loginUser.getUid(), loginUser.getId(), RedisKey.ACTIVE.getSeconds());
            if (!f) {
                return new JsonResult(ResultCode.ERROR.getCode(), "正在处理，请不要重复请求");
            }
            return userService.innerActicveUser(loginUser, cardGrade);
        } else if (loginUser.getStatus() == UserStatusType.ACTIVATESUCCESSED.getCode()) {
            return new JsonResult(ResultCode.SUCCESS.getCode(), "账号已经是激活状态");
        } else if (UserStatusType.OUT_SHARE_REGISTER_SUCCESSED.getCode()==loginUser.getStatus() && UserType.OUT.getCode()==loginUser.getCreateType()) {
            return userService.shareActicveUser(loginUser.getId());
        }
        return new JsonResult(ResultCode.ERROR.getCode(), "无法激活");
    }


    /**
     * 根据用户开卡等级，获取激活详细条件
     */
    @ResponseBody
    @RequestMapping(value = "/activeInfo", method = RequestMethod.GET)
    public JsonResult activeInfo(HttpServletRequest request) {
        try {
            Token token = TokenUtil.getSessionUser(request);
            if (token == null) {
                return new JsonResult(ResultCode.NO_LOGIN.getCode(), ResultCode.NO_LOGIN.getMsg());
            }
            User loginUser = userService.readById(token.getId());
            JsonResult x = checkLoginUser(loginUser);
            if (x != null) return x;
            UserDetail userDetail = userDetailService.readById(loginUser.getId());
            if (userDetail == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "获取用户信息失败");
            }
            Map<String, String> rs = new HashedMap();
            if (UserStatusType.INNER_REGISTER_SUCCESSED.getCode() == loginUser.getStatus()) {
                CardGrade userCard = cardGradeService.getUserCardGrade(loginUser.getCardGrade());
                if (userCard == null) {
                    return new JsonResult(ResultCode.ERROR.getCode(), "无法查询到会员开卡等级");
                }
                //人民币兑换支付币汇率
                Double payScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTPAYSCALE), 0d);
                //人民币兑换交易币汇率
                Double tradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.RMBCONVERTTRADESCALE), 0d);
                //支付币兑换人民币汇率
                Double payConverRmbScale = CurrencyUtil.getPoundage(1 / payScale, 1d, 2);
                //交易币兑换人民币汇率
                Double tradeConverRmbScale = CurrencyUtil.getPoundage(1 / tradeScale, 1d, 2);
                //生成保单时支付币必须有的比例数量
                Double insurancePayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE), 0d);
                //生成保单时交易币必须有的比例数量
                Double insuranceTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE), 0d);
                //需要支付币的总金额
                Double payAmount = payScale * userCard.getInsuranceAmt() * insurancePayScale;
                //需要交易币的总金额
                Double tradeAmount = tradeScale * userCard.getInsuranceAmt() * insuranceTradeScale;
                //需要补充激活码数量
                Integer needActiveNum = userCard.getActiveCodeNo() - loginUser.getActiveCodeNo();
                needActiveNum = needActiveNum > 0 ? needActiveNum : 0;
                //需要补充交易币数量
                Double needBuyTradeAmt = tradeAmount - loginUser.getTradeAmt();
                needBuyTradeAmt = needBuyTradeAmt > 0 ? needBuyTradeAmt : 0d;
                //需要补充购物币数量
                Double needBuyPayAmt = payAmount - loginUser.getPayAmt();
                needBuyPayAmt = needBuyPayAmt > 0 ? needBuyPayAmt : 0d;
                UserDetail updateModel = new UserDetail();
                Boolean isUpdate = false;
                List list=new ArrayList();
                list.add(200000);
                list.add(200001);
                list.add(200002);
                list.add(200041);
                list.add(99999);
                try {
                    if (ToolUtil.isEmpty(userDetail.getInPayAddress())) {
                        userDetail.setInPayAddress(WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.PAY.getCode()), loginUser.getUid() + ""));
                        updateModel.setInPayAddress(userDetail.getInPayAddress());
                        isUpdate = true;
                    }
                    if (ToolUtil.isEmpty(userDetail.getInTradeAddress())) {
                        userDetail.setInTradeAddress(WalletUtil.getAccountAddress(WalletUtil.getBitCoinClient(CurrencyType.TRADE.getCode()), loginUser.getUid() + ""));
                        updateModel.setInTradeAddress(userDetail.getInTradeAddress());
                        isUpdate = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (list.contains(loginUser.getUid())){
                        return  new JsonResult(ResultCode.SHOW_ERROR.getCode(),"连接钱包服务器异常");
                    }else{
                        return  new JsonResult(ResultCode.ERROR.getCode(),"网络异常，请稍后重试");
                    }
                }
                if (isUpdate) {
                    userDetailService.updateById(userDetail.getId(), updateModel);
                }

                //最大收益
                Double cardMaxproft = userCard.getInsuranceAmt() * userCard.getOutMultiple();
                rs.put("tradeConverRmbScale", tradeConverRmbScale + "");
                rs.put("payConverRmbScale", payConverRmbScale + "");
                rs.put("payAmount", payAmount + "");
                rs.put("tradeAmount", tradeAmount + "");
                rs.put("needActiveNum", needActiveNum + "");
                rs.put("userTradeAmount", loginUser.getTradeAmt() + "");
                rs.put("userPayAmount", loginUser.getPayAmt() + "");
                rs.put("needBuyTradeAmt", needBuyTradeAmt + "");
                rs.put("needBuyPayAmt", needBuyPayAmt + "");
                rs.put("cardMaxproft", cardMaxproft + "");
                rs.put("inTradeAddress", userDetail.getInTradeAddress());
                rs.put("inPayAddress", userDetail.getInPayAddress());
                rs.put("inEquityAddress", userDetail.getInEquityAddress());
                rs.put("payAmt", loginUser.getPayAmt() + "");
                rs.put("tradeAmt", loginUser.getTradeAmt() + "");
            }
            rs.put("status", loginUser.getStatus() + "");
            return new JsonResult(rs);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new JsonResult();
    }

    private JsonResult checkLoginUser(User loginUser) {
        if (null == loginUser) {
            return new JsonResult(-1, "用户不存在");
        } else {
            Set<Integer> userStatus = new HashSet<>();
            userStatus.add(UserStatusType.INNER_REGISTER_SUCCESSED.getCode());
            userStatus.add(UserStatusType.ACTIVATESUCCESSED.getCode());
            userStatus.add(UserStatusType.WAITACTIVATE.getCode());
            userStatus.add(UserStatusType.OUT.getCode());
            if (UserStatusType.OUT_SHARE_REGISTER_SUCCESSED.getCode().intValue()==(loginUser.getStatus())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "必须先选择开卡等级");
            }
            if (!userStatus.contains(loginUser.getStatus())) {
                return new JsonResult(ResultCode.NO_LOGIN.getCode(), "您的帐号已被禁用");
            }
            if (loginUser.getCardGrade() == null || loginUser.getCardGrade() == 0) {
                return new JsonResult(ResultCode.ERROR.getCode(), "账号初始化数据有误");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println("reXFNaaBb4aLrPtDQZyPyfW9uajDvKaxoo".length());
    }
}
