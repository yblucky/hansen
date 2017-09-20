package com.controller;


import com.Token;
import com.base.TokenUtil;
import com.base.page.*;
import com.constant.RedisKey;
import com.constant.UserStatusType;
import com.model.*;
import com.redis.Strings;
import com.service.CardGradeService;
import com.service.FeedBackService;
import com.service.UserDetailService;
import com.service.UserService;
import com.sysservice.ManageUserService;
import com.utils.classutils.MyBeanUtils;
import com.utils.codeutils.Md5Util;
import com.utils.toolutils.ToolUtil;
import com.utils.toolutils.ValidateUtils;
import com.vo.InnerRegisterUserVo;
import com.vo.SysUserVo;
import com.vo.UserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.R;

/**
 * Created on 2017-08-21;
 */
@Controller
@RequestMapping("/muser")
public class UserController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private ManageUserService manageUserService;

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private CardGradeService cardGradeService;
    @Autowired
    private FeedBackService  feedBackService;

    @ResponseBody
    @RequestMapping(value = "/list")
    public RespBody list(HttpServletRequest request, UserVo vo, Paging page) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        User condition = new User();
        if (vo.getUid() != null) {
            condition.setUid(vo.getUid());
        }
        if (ToolUtil.isNotEmpty(vo.getPhone())) {
            condition.setPhone(vo.getPhone());
        }
        if (ToolUtil.isNotEmpty(vo.getNickName())) {
            condition.setNickName(vo.getNickName());
        }

        if (ToolUtil.isNotEmpty(vo.getGrade())) {
            condition.setGrade(vo.getGrade());
        }
        if (ToolUtil.isNotEmpty(vo.getGrade())) {
            condition.setGrade(vo.getGrade());
        }
        if (ToolUtil.isNotEmpty(vo.getCardGrade())) {
            condition.setCardGrade(vo.getCardGrade());
        }
        if (ToolUtil.isNotEmpty(vo.getStatus())) {
            condition.setStatus(vo.getStatus());
        }
        Integer count = userService.readCount(condition);
        List<User> users = userService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
        List<UserVo> list = new ArrayList<UserVo>();
        UserVo userVo = null;
        for (User po : users) {
            userVo = MyBeanUtils.copyProperties(po, UserVo.class);
            userVo.setGrade(po.getGrade());
            userVo.setCardGrade(po.getCardGrade());
            userVo.setStatus(po.getStatus());
            UserDetail userDetail = userDetailService.readById(po.getId());
            BeanUtils.copyProperties(userVo, userDetail);
            userVo.setPayWord("");
            userVo.setPassword("");
            userVo.setStatus(po.getStatus());
            list.add(userVo);
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, list);
        return respBody;
    }

    @ResponseBody
    @RequestMapping("/update")
    public RespBody update(HttpServletRequest request, @RequestBody UserVo model) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        String token = request.getHeader("token");
        //读取用户信息
        SysUserVo userVo = manageUserService.SysUserVo(token);
        User user = userService.readById(model.getId());
        if (userVo == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "登录用户不存在");
            return respBody;
        }
        if (user == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
            return respBody;
        }
        if (ToolUtil.isNotEmpty(model.getPhone())) {
            model.setPhone(model.getPhone());
        }
        if (ToolUtil.isNotEmpty(model.getNickName())) {
            model.setNickName(model.getNickName());
        }
        if (ToolUtil.isNotEmpty(model.getEmail())) {
            model.setEmail(model.getEmail());
        }
        if (model.getRemainTaskNo() != null) {
            model.setRemainTaskNo(model.getRemainTaskNo());
        }
        if (ToolUtil.isNotEmpty(model.getPhone())) {
            model.setPhone(model.getPhone());
        }
        if (ToolUtil.isNotEmpty(model.getPassword())) {
            if (!model.getPassword().equals(model.getConfirmPassword())) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "确认登录密码错误");
                return respBody;
            }
        }
        if (ToolUtil.isNotEmpty(model.getPayWord())) {
            if (!model.getPayWord().equals(model.getConfirmPayWord())) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "确认支付密码错误");
                return respBody;
            }
        }
        if (ToolUtil.isNotEmpty(model.getPassword())) {
            model.setPassword(Md5Util.MD5Encode(model.getPassword(), user.getSalt()));
        }
        if (ToolUtil.isNotEmpty(model.getPayWord())) {
            model.setPayWord(Md5Util.MD5Encode(model.getPayWord(), user.getSalt()));
        }
        UserDetail detail = new UserDetail();

        if (ToolUtil.isNotEmpty(model.getReceiver())) {
            detail.setReceiver(model.getReceiver());
        }
        if (ToolUtil.isNotEmpty(model.getUserName())) {
            detail.setUserName(model.getUserName());
        }
        if (ToolUtil.isNotEmpty(model.getReceiverPhone())) {
            if (!ValidateUtils.mobile(model.getReceiverPhone())){
                respBody.add(RespCodeEnum.ERROR.getCode(), "收货人手机号有误");
                return respBody;
            }
            detail.setReceiverPhone(model.getReceiverPhone());
        }
        if (ToolUtil.isNotEmpty(model.getReceiver())) {
            detail.setReceiver(model.getReceiver());
        }
        if (ToolUtil.isNotEmpty(model.getShopAddr())) {
            detail.setShopAddr(model.getShopAddr());
        }
        if (ToolUtil.isNotEmpty(model.getBankType())) {
            detail.setBankType(model.getBankType());
        }
        if (ToolUtil.isNotEmpty(model.getBankCardNo())) {
            if (!ValidateUtils.checkBankCard(model.getBankCardNo())){
                respBody.add(RespCodeEnum.ERROR.getCode(), "银行卡号不合法");
                return respBody;
            }
            detail.setBankCardNo(model.getBankCardNo());
        }
        userService.updateById(user.getId(), model);
        userDetailService.updateById(user.getId(), detail);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户已更新");
        return respBody;
    }

    @ResponseBody
    @RequestMapping("/delete")
    public RespBody delete(HttpServletRequest request, @RequestBody Map<String, Object> map) throws Exception {
        String userId = "";
        Integer status = null;
        if (map.containsKey("id")) {
            userId = (String) map.get("id");
        }
        if (map.containsKey("status")) {
            status = (Integer) map.get("status");
        }
        // 创建返回对象
        RespBody respBody = new RespBody();
        String token = request.getHeader("token");
        //读取用户信息
        SysUserVo userVo = manageUserService.SysUserVo(token);
        SysUser loginUser = manageUserService.readById(userVo.getId());
        if (loginUser == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "登录用户不存在");
            return respBody;
        }
        User appUser = userService.readById(userId);
        if (appUser == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "禁用账户不存在");
            return respBody;
        }
        if (status == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "状态不正确");
            return respBody;
        }
        if (UserStatusType.DISABLE.getCode() == status) {
            userService.updateUserStatusByUserId(userId, UserStatusType.DISABLE.getCode());
            String appUserToken = TokenUtil.generateToken(appUser.getId(), appUser.getNickName());
            Strings.setEx(RedisKey.TOKEN_API.getKey() + loginUser.getId(), RedisKey.TOKEN_API.getSeconds(), appUserToken);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户已被禁用");
        } else if (UserStatusType.DEL.getCode() == status) {
            if (UserStatusType.ACTIVATESUCCESSED.getCode().intValue() == appUser.getStatus().intValue() || UserStatusType.WAITACTIVATE.getCode().intValue() == appUser.getStatus().intValue()) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "用户激活或正在保单中，不允许删除");
                return respBody;
            }
            userService.updateUserStatusByUserId(userId,UserStatusType.DEL.getCode());
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户已删除");
        }else if (100 == status) {
            if (UserStatusType.DISABLE.getCode().intValue() !=  appUser.getStatus().intValue()) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "用户不是禁用状态,不能重新启用");
                return respBody;
            }
            userService.updateUserStatusByUserId(userId,UserStatusType.OUT.getCode());
            userService.updateRemainTaskNoByUserId(userId,-appUser.getRemainTaskNo());
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户已启用，需要重新购买消费码进行激活");
        }
        return respBody;
    }

    @ResponseBody
    @RequestMapping("/detail")
    public RespBody detail(HttpServletRequest request, String userId) {
        UserVo userVo = null;
        // 创建返回对象
        RespBody respBody = new RespBody();
        try {
            Token token = TokenUtil.getSessionUser(request);
            User user = userService.readById(token.getId());
            if (user == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
                return respBody;
            }
            userVo = new UserVo();
            BeanUtils.copyProperties(userVo, user);

            UserDetail userDetail = userDetailService.readById(userId);
            if (userDetail != null) {
                BeanUtils.copyProperties(userVo, userDetail);
            }
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功获取");
        } catch (Exception e) {
            e.printStackTrace();
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
        }
        return respBody;
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
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBody innerCreateUser(HttpServletRequest request, @RequestBody InnerRegisterUserVo vo) throws Exception {

        // 创建返回对象
        RespBody respBody = new RespBody();

        String token = request.getHeader("token");
        //读取用户信息
        SysUserVo userVo = manageUserService.SysUserVo(token);
        SysUser user = manageUserService.readById(userVo.getId());
        if (user == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户不存在");
            return respBody;
        }

        if (ToolUtil.isEmpty(vo.getPassword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "新建用户登录密码不能为空");
            return respBody;
        }
        if (ToolUtil.isEmpty(vo.getPayword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "新建用户支付密码不能为空");
            return respBody;
        }
        if (ToolUtil.isEmpty(vo.getConfirmPassword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "新建用户确认登录密码不能为空");
            return respBody;
        }
        if (ToolUtil.isEmpty(vo.getConfirmPassword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "新建用户确认支付密码不能为空");
            return respBody;
        }
        if (!vo.getConfirmPassword().equals(vo.getPassword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "新建用户确认两次登录密码不一致");
            return respBody;
        }
        if (!vo.getConfirmPayWord().equals(vo.getPassword())) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "新建用户确认两次支付密码不一致");
            return respBody;
        }
        if (vo.getCardGrade() == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "请选择开卡级别");
            return respBody;
        }
        if (vo.getFirstReferrer() == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "邀请人uid不能为空");
            return respBody;
        }
        User askCondition = new User();
        askCondition.setUid(vo.getUid());
        User askUser = userService.readOne(askCondition);
        if (askUser == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "邀请人不能存在");
            return respBody;
        }

        User regisUserContion = new User();
        regisUserContion.setLoginName(vo.getLoginName());
        User regisUser = userService.readOne(regisUserContion);
        if (regisUser != null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "此账号已被注册");
            return respBody;
        }
        CardGrade condition = new CardGrade();
        condition.setGrade(vo.getCardGrade());
        CardGrade cardGrade = cardGradeService.readOne(condition);
        if (cardGrade == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "开卡级别有误");
            return respBody;
        }


        if (askUser.getRegisterCodeNo() < cardGrade.getRegisterCodeNo()) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "邀请人注册码个数不足");
            return respBody;
        }
        if (askUser.getActiveCodeNo() < cardGrade.getActiveCodeNo()) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "邀请人激活码个数不足");
            return respBody;
        }


        User inviterUser = null;
        if (ToolUtil.isNotEmpty(vo.getContactUserId()) && vo.getContactUserId() != vo.getUid()) {
            User inviterCondition = new User();
            inviterCondition.setUid(vo.getContactUserId());
            inviterUser = userService.readOne(inviterCondition);
            if (inviterUser == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "接点人信息有误");
                return respBody;
            }
        } else {
            inviterUser = askUser;
        }
        User model = new User();
        BeanUtils.copyProperties(model, vo);
        model.setPayWord(vo.getPayword());
        userService.innerRegister(askUser, inviterUser, model, cardGrade);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "注册成功");
        return respBody;
    }

    @ResponseBody
    @RequestMapping(value = "/feedbacklist", method = RequestMethod.GET)
    public RespBody feedbackList(HttpServletRequest request, Paging page) throws Exception {
        RespBody respBody=new RespBody();
        FeedBack feedBack = new FeedBack();
        String token = request.getHeader("token");
        SysUserVo sysUserVo=manageUserService.SysUserVo(token);
        if (sysUserVo == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(),"登录失效");
        }
        int count = feedBackService.readCount(feedBack);
        List<FeedBack> list = new ArrayList<>();
        if(count > 0) {
            list = feedBackService.readList(feedBack, page.getPageNumber(), page.getPageSize(),count);
            for (FeedBack feedBack1:list){
                if (feedBack1!=null){
                    User u=userService.readById(feedBack1.getUserId());
                    if (u!=null){
                        feedBack1.setUserId(u.getUid().toString());
                    }else {
                        feedBack1.setUserId("");
                    }
                }
            }
        }
        respBody.add(RespCodeEnum.SUCCESS.getCode(),"成功",page,list);
        return respBody;
    }
}
