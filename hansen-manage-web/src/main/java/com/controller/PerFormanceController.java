package com.controller;

import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.constant.GradeType;
import com.model.SysUser;
import com.model.User;
import com.model.UserDepartment;
import com.service.UserDepartmentService;
import com.service.UserService;
import com.sysservice.ManageUserService;
import com.utils.toolutils.ToolUtil;
import com.vo.SysUserVo;
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
@RequestMapping("/performance")
public class PerFormanceController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PerFormanceController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserDepartmentService userDepartmentService;
    @Resource
    private ManageUserService manageUserService;//用户业务层


    /**
     * 获取列表
     *
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public RespBody getTask(HttpServletRequest request, HttpServletResponse response, Paging page, Integer uid, String mobile) throws Exception {
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
        List<UserDepartment> list = null;
        UserDepartment condition = new UserDepartment();
        if (uid != null) {
            condition.setUid(uid);
        }
        if (ToolUtil.isNotEmpty(mobile)) {
            User condion = new User();
            condion.setPhone(mobile);
            User u = userService.readOne(condion);
            if (u == null) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "没有记录");
            } else {
                condion.setUid(u.getUid());
            }
        }
        List<UserDepartment> departments = new ArrayList<>();

        Integer count = userDepartmentService.readCount(condition);
        if (count != null && count > 0) {
            departments = userDepartmentService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
            for (UserDepartment department:departments){
                department.setRemark(GradeType.fromCode(department.getGrade()).getMsg());
            }
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, departments);
        return respBody;
    }


}
