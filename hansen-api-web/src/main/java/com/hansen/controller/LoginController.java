package com.hansen.controller;

import com.base.page.JsonResult;
import com.common.Token;
import com.common.base.TokenUtil;
import com.common.constant.*;
import com.common.utils.codeutils.Md5Util;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.service.*;
import com.hansen.vo.*;
import com.model.*;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

import static com.common.utils.WalletUtil.getBitCoinClient;

@Controller
@RequestMapping("/login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
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
     * 账号密码登录
     *
     * 包含激活账号流程
     *
     */
    @ResponseBody
    @RequestMapping(value = "/login/loginIn", method = RequestMethod.POST)
    public JsonResult loginByUserName(HttpServletRequest request, @RequestBody LoginUserVo vo) throws Exception {
        if (ToolUtil.isEmpty(vo.getLoginName())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录名称不能为空");
        }
        if (ToolUtil.isEmpty(vo.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录密码不能为空");
        }
        User condition = new User();
        condition.setLoginName(vo.getLoginName());
        User loginUser = userService.readOne(condition);
        if (null == loginUser) {
            return new JsonResult(-1, "用户不存在");
        } else {
            if (loginUser.getStatus() != UserStatusType.ACTIVATESUCCESSED.getCode() && loginUser.getStatus()!=UserStatusType.INNER_REGISTER_SUCCESSED.getCode()) {
                    return new JsonResult(ResultCode.ERROR.getCode(), "您的帐号已被禁用");
            }
            String password = loginUser.getPassword();
            if (org.springframework.util.StringUtils.isEmpty(password)) {
                return new JsonResult(ResultCode.ERROR.getCode(), "没有设置登录密码");
            }
            if (!password.equals(Md5Util.MD5Encode(vo.getPassword(), loginUser.getSalt()))) {
                return new JsonResult(ResultCode.ERROR.getCode(), "用户名或密码错误");
            }
        }
        if (loginUser.getStatus()==UserStatusType.INNER_REGISTER_SUCCESSED.getCode()){
            CardGrade cardGradeCondition = new CardGrade();
            cardGradeCondition.setGrade(vo.getCardGrade());
            CardGrade cardGrade = cardGradeService.readOne(cardGradeCondition);
            if (cardGrade == null) {
                return new JsonResult(ResultCode.ERROR.getCode(), "开卡级别有误");
            }
            //如果用户状态是内部注册成功，已经代为扣除激活码的状态，则走此流程，此流程走完，满足条件的情况下，用户账号即被激活成功
            userService.innerActicveUser(loginUser,cardGrade);
        }
        User updateUser = new User();
        updateUser.setLoginTime(new Date());
        userService.updateById(loginUser.getId(), updateUser);
        // 登录
        String token = TokenUtil.generateToken(loginUser.getId(), loginUser.getNickName());
        Strings.setEx(RedisKey.TOKEN_API.getKey() + loginUser.getId(), RedisKey.TOKEN_API.getSeconds(), token);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user login[%s]", TokenUtil.getTokenObject(token)));
        }
        UserVo u = new UserVo();
        BeanUtils.copyProperties(u, loginUser);
        u.setToken(token);
        return new JsonResult(u);
    }

    /**
     * 根据token获取用户信息
     * @param request
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/userinfo", method = RequestMethod.GET)
    public JsonResult userInfo(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (token == null) {
            return new JsonResult(1, "用户登录失效");
        }
        User user = userService.readById(token.getId());
        if (null == user) {
            return new JsonResult(2, "无法找到用户信息");
        }
        if (user.getStatus() != UserStatusType.ACTIVATESUCCESSED.getCode() && user.getStatus()!=UserStatusType.INNER_REGISTER_SUCCESSED.getCode()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "您的帐号已被禁用");
        }
        // Redis获取Token
        String redisToken = Strings.get(RedisKey.TOKEN_API.getKey() + token.getId());
        if (logger.isInfoEnabled()) {
            logger.info(String.format("user again login[%s]", TokenUtil.getTokenObject(redisToken)));
        }
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(vo, user);
        vo.setToken(redisToken);
        return new JsonResult(vo);
    }
    /**
     * 忘记登录密码
     */
    @ResponseBody
    @RequestMapping(value = "/forget/loginpass", method = RequestMethod.POST)
    public JsonResult forgetLoginPassword(HttpServletRequest request, @RequestBody LoginPasswordVo vo) throws Exception {
       if (vo.getValidType()==null){
           return new JsonResult(ResultCode.ERROR.getCode(),"校验类型不合法");
       }
       if (vo.getValidType()==2 && ToolUtil.isEmpty(vo.getNewLoginPass())){
           //兼容分两次校验，第一次只校验手机号和验证码，通过后才输入新密码和确认新密码 此时 validType=2
           if (StringUtils.isBlank(vo.getPhone())) {
               return new JsonResult(ResultCode.ERROR.getCode(), "请输入手机号");
           }
           if (StringUtils.isBlank(vo.getSmsCode())) {
               return new JsonResult(ResultCode.ERROR.getCode(), "请输入短信验证码");
           }
           return  new JsonResult(ResultCode.SUCCESS.getCode(),"验证码校验通过");
       }
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(2, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User user = userService.readOne(condition);
        if (user == null) {
            return new JsonResult(-1, "该手机号不存在");
        }
        if (StringUtils.isBlank(vo.getNewLoginPass())) {
            return new JsonResult(1, "请输入新密码");
        }
        if (StringUtils.isBlank(vo.getNewLoginPassConfirm())) {
            return new JsonResult(3, "请输入确认新密码");
        }
        if (!vo.getNewLoginPass().equals(vo.getNewLoginPassConfirm())) {
            return new JsonResult(7, "两次输入密码不一致");
        }
        // 更新用户登录密码
        User model = new User();
        model.setPassword(Md5Util.MD5Encode(vo.getNewLoginPass(), user.getSalt()));
        userService.updateById(user.getId(), model);
        return new JsonResult();
    }

    /**
     * 忘记支付密码
     */
    @ResponseBody
    @RequestMapping(value = "/forget/paypass", method = RequestMethod.POST)
    public JsonResult forgetPayPassword(HttpServletRequest request, @RequestBody PayPasswordVo vo) throws Exception {
        if (vo.getValidType()==null){
            return new JsonResult(ResultCode.ERROR.getCode(),"校验类型不合法");
        }
        if (vo.getValidType()==2 && ToolUtil.isEmpty(vo.getNewPayPass())){
            //兼容分两次校验，第一次只校验手机号和验证码，通过后才输入新支付密码和确认支付新密码 此时 validType=2
            if (StringUtils.isBlank(vo.getPhone())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "请输入手机号");
            }
            if (StringUtils.isBlank(vo.getSmsCode())) {
                return new JsonResult(ResultCode.ERROR.getCode(), "请输入短信验证码");
            }
            return  new JsonResult(ResultCode.SUCCESS.getCode(),"验证码校验通过");
        }
        if (StringUtils.isBlank(vo.getPhone())) {
            return new JsonResult(0, "请输入手机号");
        }
        if (StringUtils.isBlank(vo.getSmsCode())) {
            return new JsonResult(1, "请输入短信验证码");
        }
        String smsCode2 = Strings.get(RedisKey.SMS_CODE.getKey() + vo.getPhone());
        if (!vo.getSmsCode().equalsIgnoreCase(smsCode2)) {
            return new JsonResult(2, "短信验证码不正确或已失效");
        }
        User condition = new User();
        condition.setPhone(vo.getPhone());
        User user = userService.readOne(condition);
        if (user == null) {
            return new JsonResult(-1, "该手机号不存在");
        }
        if (StringUtils.isBlank(vo.getNewPayPass())) {
            return new JsonResult(1, "请输入新密码");
        }
        if (StringUtils.isBlank(vo.getNewPayPassConfirm())) {
            return new JsonResult(3, "请输入确认新密码");
        }
        if (!vo.getNewPayPass().equals(vo.getNewPayPassConfirm())) {
            return new JsonResult(7, "两次输入密码不一致");
        }
        // 更新用户支付密码
        User model = new User();
        model.setPayWord(Md5Util.MD5Encode(vo.getNewPayPassConfirm(), user.getSalt()));
        userService.updateById(user.getId(), model);
        return new JsonResult();
    }


    
}