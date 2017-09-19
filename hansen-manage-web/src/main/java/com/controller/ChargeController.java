package com.controller;

import com.base.page.Page;
import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.constant.CodeType;
import com.constant.Constant;
import com.constant.WalletOrderStatus;
import com.constant.WalletOrderType;
import com.model.SysUser;
import com.model.TransferCode;
import com.model.User;
import com.model.WalletOrder;
import com.service.TransferCodeService;
import com.service.UserService;
import com.service.WalletOrderService;
import com.sysservice.ManageUserService;
import com.utils.codeutils.CryptUtils;
import com.utils.toolutils.OrderNoUtil;
import com.utils.toolutils.ToolUtil;
import com.vo.BackReChargeVo;
import com.vo.SysUserVo;
import com.vo.UserVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/charge")
public class ChargeController {
    private Logger logger = LoggerFactory.getLogger(ChargeController.class);
    @Resource
    private UserService userService;
    @Autowired
    private WalletOrderService walletOrderService;
    @Autowired
    private TransferCodeService transferCodeService;
    @Autowired
    private ManageUserService manageUserService;


    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespBody list(HttpServletRequest request, UserVo vo, Paging page) throws Exception {
        // 创建返回对象
        RespBody respBody = new RespBody();
        User user = null;
        List<WalletOrder> list = new ArrayList<WalletOrder>();
        String userId = "";
        if (ToolUtil.isNotEmpty(vo.getUid()) && vo.getUid() != 0) {
            User condition = new User();
            condition.setUid(vo.getUid());
            user = userService.readOne(condition);
        }
        if (page.getPageNumber() == 0) {
            page.setPageNumber(1);
        }
        if (page.getPageSize() == 0) {
            page.setPageSize(10);
        }
        Page page1 = new Page();
        page1.setPageNo(page.getPageNumber());
        page1.setPageSize(page.getPageSize());
        if (user != null) {
            userId = user.getId();
        }
        List<Integer> orderTypeList = new ArrayList<>();
        orderTypeList.add(WalletOrderType.TRADE_COIN_BACK_CHARGE.getCode());
        orderTypeList.add(WalletOrderType.PAY_COIN_BACK_CHARGE.getCode());
        orderTypeList.add(WalletOrderType.EQUITY_COIN_BACK_CHARGE.getCode());

        Integer c = walletOrderService.readOrderCount(userId, orderTypeList);
        if (c != null && c > 0) {
            list = walletOrderService.readOrderList(userId, orderTypeList, page1);
            for (WalletOrder order : list) {
                User u = userService.readById(order.getReceviceUserId());
                order.setRemark(u.getNickName());
                order.setReceviceUserId(u.getUid() + "");
            }
        } else {
            c = 0;
        }
        page.setTotalCount(c);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, list);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, list);
        return respBody;
    }

    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public RespBody add(HttpServletRequest request, @RequestBody BackReChargeVo vo) throws Exception {
        RespBody respBody = new RespBody();
        String token = request.getHeader("token");
        SysUserVo userVo = manageUserService.SysUserVo(token);

        if (userVo == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "登录用户不存在");
            return respBody;
        }
        SysUser muser = manageUserService.readById(userVo.getId());
        if (vo.getUid() == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "uid不能为空");
            return respBody;
        }
        if (ToolUtil.isEmpty(vo.getSupperPass())){
            respBody.add(RespCodeEnum.ERROR.getCode(), "请先输入超级密码");
            return respBody;
        }
        if (ToolUtil.isEmpty(userVo.getRemark())){
            respBody.add(RespCodeEnum.ERROR.getCode(), "没有充值权限，或者没有设置过超级密码，无法完成充值");
            return respBody;
        }
        String supass = CryptUtils.hmacSHA1Encrypt(vo.getSupperPass(), muser.getSalt());
        if (!userVo.getRemark().equals(supass)){
            respBody.add(RespCodeEnum.ERROR.getCode(), "超级密码不正确，无法完成充值");
            return respBody;
        }
        User condition = new User();
        condition.setUid(vo.getUid());
        User chargeTargerUser = userService.readOne(condition);
        if (chargeTargerUser == null) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "充值用户不存在");
            return respBody;
        }

        walletOrderService.chargeService(vo, chargeTargerUser);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功");
        return respBody;
    }

}
