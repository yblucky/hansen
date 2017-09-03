package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.CodeType;
import com.constant.UserStatusType;
import com.model.*;
import com.service.ActiveCodeService;
import com.service.CardGradeService;
import com.service.TransferCodeService;
import com.service.UserService;
import com.utils.codeutils.Md5Util;
import com.vo.CodeTransferVo;
import com.vo.CodeVo;
import com.utils.numberutils.UUIDUtil;
import com.utils.toolutils.ToolUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */
@Controller
@RequestMapping("/code")
public class ActiveCodeController {

    @Autowired
    private ActiveCodeService activeCodeService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransferCodeService transferCodeService;
    @Autowired
    private CardGradeService cardGradeService;

    /**
     * 生成激活码和注册码
     *
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/generateCode", method = RequestMethod.POST)
    public JsonResult generateCode(HttpServletRequest request, HttpServletResponse response, @RequestBody CodeVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        if (ToolUtil.isEmpty(vo.getCodeType())) {
            return new JsonResult(1, "生成码的类型不能为空");
        }

        //创建生成码的记录
        ActiveCode activeCode = new ActiveCode();
        activeCode.setCode(UUIDUtil.generateUUID());
        activeCode.setStatus(1);
        activeCode.setOwnerId(token.getId());
        if (vo.getCodeType().intValue() == CodeType.ACTIVATECODE.getCode().intValue()) {
            activeCode.setType(CodeType.ACTIVATECODE.getCode());
            activeCode.setRemark(CodeType.ACTIVATECODE.getMsg());
        } else {
            activeCode.setType(CodeType.REGISTERCODE.getCode());
            activeCode.setRemark(CodeType.REGISTERCODE.getMsg());
        }

        //生成码
        activeCodeService.createWithUUID(activeCode);

        return new JsonResult();
    }


    /**
     * 根据用户id查询所拥有的激活码和生成码
     *
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getExistCode", method = RequestMethod.POST)
    public JsonResult getExistCode(HttpServletRequest request, HttpServletResponse response, @RequestBody CodeVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);


        ActiveCode model = new ActiveCode();
        model.setStatus(1);
        model.setOwnerId(token.getId());
        model.setType(vo.getCodeType());
        List<ActiveCode> codeList = activeCodeService.readAll(model);

        return new JsonResult(codeList);
    }


    /**
     * 激活码转赠接口
     *
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/codetransfer", method = RequestMethod.POST)
    public JsonResult codeTransfer(HttpServletRequest request, HttpServletResponse response, @RequestBody CodeTransferVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User fromUser = userService.readById(token.getId());
        if (fromUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
        }
//        if (UserStatusType.ACTIVATESUCCESSED.getCode() != fromUser.getStatus()) {
//            return new JsonResult(ResultCode.ERROR.getCode(), "账号未激活");
//        }
        if (vo.getCodeType()==null || (vo.getCodeType()!=1  && vo.getCodeType()!=2)) {
            return new JsonResult(ResultCode.ERROR.getCode(), "转账类型错误");
        }
        if (vo.getCodeType()==1){
            if (fromUser.getActiveCodeNo() < vo.getTransferNo()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "激活码数量不足");
            }
        }else if (vo.getCodeType()==2){
            if (fromUser.getRegisterCodeNo() < vo.getTransferNo()) {
                return new JsonResult(ResultCode.ERROR.getCode(), "注册码数量不足");
            }
        }

        if (ToolUtil.isEmpty(vo.getToId()) && vo.getToUid() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "请选择转账目标用户");
        }

        if (ToolUtil.isEmpty(vo.getPayword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "支付密码不能为空");
        }
        if (ToolUtil.isEmpty(fromUser.getPassword())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "未设置支付密码");
        }
        if (!fromUser.getPassword().equals(Md5Util.MD5Encode(vo.getPayword(),fromUser.getSalt()))){
            return new JsonResult(ResultCode.ERROR.getCode(), "支付密码错误");
        }
        User toUser = null;
        if (ToolUtil.isEmpty(vo.getToId())) {
            User condition = new User();
            condition.setUid(vo.getToUid());
            toUser = userService.readOne(condition);
        } else {
            toUser = userService.readById(vo.getToId());
        }
        if (toUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "目标用户不存在");
        }
//        if (UserStatusType.ACTIVATESUCCESSED.getCode() != toUser.getStatus()) {
//            return new JsonResult(ResultCode.ERROR.getCode(), "目标账号未激活");
//        }
        activeCodeService.codeTransfer(fromUser.getId(), toUser.getId(), toUser.getUid(), vo.getTransferNo(),vo.getCodeType());
        return new JsonResult(ResultCode.SUCCESS.getCode(), "激活码转账成功");
    }

    /**
     * 使用激活码
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/useactivecode", method = RequestMethod.POST)
    public JsonResult useActiveCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
        }
        if (UserStatusType.OUT.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "账号不是出局状态，暂时不需要使用激活码");
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        if (cardGrade == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "无法获取用户开卡等级");
        }
        if (user.getActiveCodeNo() < cardGrade.getActiveCodeNo()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "冲洗激活需要" + cardGrade.getActiveCodeNo() + "个激活码，激活码数量不足");
        }
        activeCodeService.useActiveCode(user.getId(), cardGrade.getActiveCodeNo(), "");
        return new JsonResult(ResultCode.SUCCESS.getCode(), "激活账户成功");
    }


    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult inList(HttpServletRequest request, Page page,Integer type) {
        JsonResult result = null;
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10);
        }
        List<TransferCode> list = new ArrayList<>();
        PageResult<TransferCode> pageResult = new PageResult<>();
        BeanUtils.copyProperties(page,pageResult);
        Integer count = transferCodeService.readCountByUserId(user.getId());
        if (count != null && count > 0) {
            list = transferCodeService.readListByUserId(user.getId(),page);
            pageResult.setRows(list);
        }
        return new JsonResult(pageResult);
    }

    @ResponseBody
    @RequestMapping(value = "/intervalActice", method = RequestMethod.GET)
    public JsonResult intervalActice(HttpServletRequest request) {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登录用户不存在");
        }
        if (UserStatusType.OUT.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "不是出局状态，不可激活");
        }
        CardGrade cardGrade = cardGradeService.getUserCardGrade(user.getCardGrade());
        if (cardGrade==null){
            return new JsonResult(ResultCode.ERROR.getCode(), "用户开卡等级有误");
        }
        if (user.getActiveCodeNo()<cardGrade.getActiveCodeNo()){
            return new JsonResult(ResultCode.ERROR.getCode(), "激活不足,无法激活账户");
        }
        Boolean flag = userService.intervalActice(user.getId());
        if (flag){
            return new JsonResult(ResultCode.SUCCESS.getCode(),   "激活成功");
        }
        return new JsonResult(ResultCode.ERROR.getCode(), "激活失败");
    }
}
