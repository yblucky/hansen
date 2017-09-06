package com.controller;

import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.model.SysUser;
import com.model.UserDetail;
import com.service.UserDetailService;
import com.sysservice.ManageUserService;
import com.vo.SysUserVo;
import com.vo.UserDetailVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/userDetail")
public class UserDetailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(UserSignController.class);
    @Autowired
    private UserDetailService userDetailService;
    @Resource
    private ManageUserService manageUserService;//用户业务层


    /**
     * 获取用户地址信息
     *
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespBody getTask(HttpServletRequest request, HttpServletResponse response, Paging page, UserDetailVo vo) throws Exception {
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
        if(vo.getUid()!=null || !"".equals(vo.getPhone())){
            page.setPageNumber(0);
        }
        List<UserDetailVo> list = new ArrayList<>();
        Integer count = userDetailService.readCount(new UserDetail());
        if (count != null && count > 0) {
            list = userDetailService.findAll(vo, page.getPageNumber(), page.getPageSize(), count);
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, list);
        return respBody;
    }


}
