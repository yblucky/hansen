package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.*;
import com.service.*;
import com.vo.*;
import com.utils.classutils.MyBeanUtils;
import com.model.*;
import com.redis.Strings;
import com.utils.DateUtils.DateUtils;
import com.utils.codeutils.Md5Util;
import com.utils.numberutils.CurrencyUtil;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(vo.getPayWord())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择输入支付密码");
        }
        if (loginUser.getPassword().equals(Md5Util.MD5Encode(vo.getPayWord(), loginUser.getSalt()))) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付密码错误");
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(vo.getGrade());
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "升卡级别有误");
        }
        if (loginUser.getRegisterCodeNo() < cardGrade.getRegisterCodeNo() || loginUser.getActiveCodeNo() < cardGrade.getActiveCodeNo()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户激活码或注册码不足，请先补充激活码或注册码!");
        }


        //生成保单时支付币必须有的比例数量
        double insurancePayScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCEPAYSCALE), 0d);
        //生成保单时交易币必须有的比例数量
        double insuranceTradeScale = ToolUtil.parseDouble(ParamUtil.getIstance().get(Parameter.INSURANCETRADESCALE), 0d);

        /**校验虚拟币**/
        if (loginUser.getPayAmt() < CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), insurancePayScale, 4)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付币不足，无法激活");
        }
        if (loginUser.getTradeAmt() < CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), insuranceTradeScale, 4)) {
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
        updateActiveUser.setTradeAmt(CurrencyUtil.subtract(loginUser.getTradeAmt(), cardGrade.getInsuranceAmt() * insuranceTradeScale, 4));
        updateActiveUser.setPayAmt(CurrencyUtil.subtract(loginUser.getPayAmt(), cardGrade.getInsuranceAmt() * insurancePayScale, 4));
        updateActiveUser.setStatus(UserStatusType.ACTIVATESUCCESSED.getCode());
        userService.updateById(updateActiveUser.getId(), updateActiveUser);

        UserDetail activeUserDetail = userDetailService.readById(loginUser.getId());
        //写入冻结
        UserDetail updateActiveUserDetailContion = new UserDetail();
        updateActiveUserDetailContion.setId(activeUserDetail.getId());
        updateActiveUserDetailContion.setForzenPayAmt(CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), insurancePayScale, 4));
        updateActiveUserDetailContion.setForzenTradeAmt(CurrencyUtil.multiply(cardGrade.getInsuranceAmt(), insuranceTradeScale, 4));
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
            int count = userGradeRecordService.readCount(model);
            List<UserGradeRecordVo> rslist = null;
            if (count > 0) {
                List<UserGradeRecord> list = userGradeRecordService.readList(model, page.getPageNo(), page.getPageSize(), count);
                UserGradeRecordVo po = null;
                rslist = new ArrayList<UserGradeRecordVo>();
                for(UserGradeRecord record : list){
                    po=MyBeanUtils.copyProperties(record,UserGradeRecordVo.class);
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
        List<UserDepartment> userDepartments = userDepartmentService.getDirectTeamList(parentUserId);
        if (userDepartments == null) {
            userDepartments = Collections.emptyList();
        }
        int count = userDepartments.size();
        PageResult<UserDepartment> pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, userDepartments);
        return new JsonResult(pageResult);
    }


    /**
     * 修改用户信息
     */
    @ResponseBody
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public JsonResult updateUserInfo(HttpServletRequest request, @RequestBody UserVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        User updateUser =new User();
        UserDetail upateDetail=new UserDetail();
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
        if (ToolUtil.isNotEmpty(vo.getOutEquityAddress())) {
            upateDetail.setOutEquityAddress(vo.getOutEquityAddress());
        }
        if (ToolUtil.isNotEmpty(vo.getOutPayAddress())) {
            upateDetail.setOutPayAddress(vo.getOutPayAddress());
        }
        if (ToolUtil.isNotEmpty(vo.getOutTradeAddress())) {
            upateDetail.setOutTradeAddress(vo.getOutTradeAddress());
     }

        // 更新用户信息
        userService.updateById(loginUser.getId(),updateUser);
        userDetailService.updateById(loginUser.getId(),upateDetail);
        return new JsonResult();
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
     * 修改用户信息
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
        if(StringUtils.isEmpty(vo.getPicCode())){
            return new JsonResult(ResultCode.ERROR.getCode(), "验证码不能为空");
        }
        String rsCode = Strings.get(RedisKey.PIC_CODE.getKey() +vo.getPicKey());
        if(!rsCode.equalsIgnoreCase(vo.getPicCode())){
            return new JsonResult(ResultCode.ERROR.getCode(), "验证码错误或者失效了");
        }
        User updateUser = new User();
        //修改登录密码
        if ("1".equals(vo.getPwdType())) {
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
            updateUser.setPassword(Md5Util.MD5Encode(vo.getConfirmPassWord(), loginUser.getSalt()));
            updateUser.setUpdateTime(new Date());
        } else if ("2".equals(vo.getPwdType())) {
            //修改支付密码
            if (StringUtils.isEmpty(vo.getOldPayWord())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "旧支付密码不能为空");
            }
            if (StringUtils.isEmpty(vo.getNewPayWord())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "新支付密码不能为空");
            }
            if (StringUtils.isEmpty(vo.getConfirmPayWord())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "确认新支付密码不能为空");
            }
            if (!loginUser.getPayWord().equals(Md5Util.MD5Encode(vo.getOldPayWord(), loginUser.getSalt()))) {
                return new JsonResult(ResultCode.ERROR.getCode(), "旧登录密码不正确");
            }
            if (!vo.getConfirmPayWord().trim().equals(vo.getNewPayWord().trim())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "确认新支付密码和新支付密码不一致");
            }
            updateUser.setPayWord(Md5Util.MD5Encode(vo.getConfirmPayWord(), loginUser.getSalt()));
            updateUser.setUpdateTime(new Date());
        }

        // 更新用户信息
        userService.updateById(loginUser.getId(), updateUser);
        return new JsonResult();
    }

    /**
     * 忘记密码
     */
    @ResponseBody
    @RequestMapping(value = "/forgetPwd", method = RequestMethod.POST)
    public JsonResult updatePwd(HttpServletRequest request, @RequestBody ForgetPwdVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (StringUtils.isEmpty(vo.getPhoneNumber())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "手机号不能为空");
        }
        if(StringUtils.isEmpty(vo.getPhoneCode())){
            return new JsonResult(ResultCode.ERROR.getCode(), "验证码不能为空");
        }
        if(StringUtils.isEmpty(vo.getNewPassWord())){
            return new JsonResult(ResultCode.ERROR.getCode(), "新密码不能为空");
        }
        if(!vo.getNewPassWord().equals(vo.getConfirmPassWord())){
            return new JsonResult(ResultCode.ERROR.getCode(), "两次密码输入不一致");
        }
        String rsCode = Strings.get(RedisKey.SMS_CODE.getKey() +vo.getPhoneNumber());
        if(org.springframework.util.StringUtils.isEmpty(rsCode) || !rsCode.equalsIgnoreCase(vo.getPhoneCode())){
            return new JsonResult(ResultCode.ERROR.getCode(), "验证码错误或者失效了");
        }

        User updateUser = new User();
        //修改登录密码
        updateUser.setPassword(Md5Util.MD5Encode(vo.getConfirmPassWord(), loginUser.getSalt()));
        updateUser.setUpdateTime(new Date());

        //修改成功，删除验证码
        Strings.del(RedisKey.SMS_CODE.getKey() +vo.getPhoneNumber());

        // 更新用户信息
        userService.updateById(loginUser.getId(), updateUser);
        return new JsonResult();
    }
}
