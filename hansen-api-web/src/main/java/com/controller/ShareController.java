package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.ResultCode;
import com.constant.RedisKey;
import com.constant.UserStatusType;
import com.model.CardGrade;
import com.model.User;
import com.service.CardGradeService;
import com.service.UserService;
import com.utils.thirdutils.QRCodeUtil;
import com.utils.toolutils.RedisLock;
import com.utils.toolutils.ToolUtil;
import com.utils.toolutils.ValidateUtils;
import com.vo.CardGradeVo;
import com.vo.InnerRegisterUserVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.Date;

/**
 * Created   on 2017/9/12 0012.
 */
@Controller
@RequestMapping("/share")
public class ShareController {

    @Autowired
    private UserService userService;

    @Autowired
    private CardGradeService cardGradeService;
    @Value("${shareUrl}")
    private String shareUrl;

    /**
     * 生成邀请二维码
     */
    @ResponseBody
    @RequestMapping(value = "/sharecode", method = RequestMethod.GET)
    public JsonResult shareCode(HttpServletRequest request, String url) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (ToolUtil.isEmpty(url)) {
            url = shareUrl;
        }
        String content = url + "?type=2&shareUserId=" + loginUser.getId()+"&shareUid="+loginUser.getUid();
        String imgPath = request.getSession().getServletContext().getRealPath("/resources/") + "/css/img/logo.png";
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        QRCodeUtil.encode(content, imgPath, baos);
        String rstr = "data:image/jpg;base64," + Base64.encodeBase64String(baos.toByteArray());
        baos.close();
        return new JsonResult(rstr);
    }


    /**
     *
     * 分享注册
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/shareregister", method = RequestMethod.POST)
    public JsonResult innerCreateUser(HttpServletRequest request, @RequestBody InnerRegisterUserVo vo) throws Exception {
        if (ToolUtil.isEmpty(vo.getEmail())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "邮箱不能为空");
        }
        if (ToolUtil.isEmpty(vo.getPhone())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "手机号不能为空");
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
        if (vo.getFirstReferrer() == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "邀请人uid不能为空");
        }
        User regisUserContion = new User();
        regisUserContion.setLoginName(vo.getPhone());
        User regisUser = userService.readOne(regisUserContion);
        User inviterUser = userService.readUserByUid(vo.getFirstReferrer());
        if (inviterUser==null){
            return new JsonResult(ResultCode.ERROR.getCode(), "不合法的邀请码，查不到邀请人");
        }
        if (regisUser != null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "此账号已被注册");
        }
        User createUser = new User();
        BeanUtils.copyProperties(createUser, vo);
        createUser.setLoginName(vo.getPhone());
        createUser.setPayWord(vo.getPayword());
        userService.shareRegister(createUser, inviterUser);
        //返回用户uid和手机号
        User con = new User();
        con.setLoginName(vo.getLoginName());
        con.setPhone(vo.getPhone());
        User resultUser = userService.readOne(con);
        if (resultUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "注册成功,返回注册用户信息失败");
        }
        return new JsonResult(ResultCode.SUCCESS.getCode(), "注册成功", resultUser);
    }


    /**
     *
     * 分享注册首次登陆选择等级
     *
     * @param request
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/shareChooseCardeGrade", method = RequestMethod.POST)
    public JsonResult shareChooseCardeGrade(HttpServletRequest request, @RequestBody CardGradeVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = userService.readById(token.getId());
        if (loginUser == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "登陆用户不存在");
        }
        if (vo.getGrade()==null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "选择开卡等级不能为空");
        }
        if (ValidateUtils.Number(vo.getGrade().toString())){
            return new JsonResult(ResultCode.ERROR.getCode(), "卡等级必须是数字");
        }
        if (vo.getGrade()<=0 || vo.getGrade()>5){
            return new JsonResult(ResultCode.ERROR.getCode(), "卡等级必须在【1-5】范围内");
        }
        CardGrade cardGrade= cardGradeService.getUserCardGrade(vo.getGrade());
        if (cardGrade==null){
            return new JsonResult(ResultCode.ERROR.getCode(), "找不到开卡等级");
        }
        userService.updateUserCardGrade(loginUser,cardGrade);
        return new JsonResult(ResultCode.SUCCESS.getCode(), "选卡成功");
    }

}
