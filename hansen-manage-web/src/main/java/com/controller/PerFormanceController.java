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

import static com.sun.tools.doclint.Entity.nu;

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
    public RespBody getTask(HttpServletRequest request, HttpServletResponse response, Paging page, Integer uid, Integer status, String mobile) throws Exception {
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
        if (status==null){
            status=1;
        }

        if (status==1){
            if (uid != null) {
                condition.setUid(uid);
            }
            if (ToolUtil.isNotEmpty(mobile)) {
                User condionUser = new User();
                condionUser.setPhone(mobile);
                User u = userService.readOne(condionUser);
                if (u == null) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "没有记录");
                } else {
                    condition.setUid(u.getUid());
                }
            }
        }else if (status==2){
            User condionUser = new User();
            condionUser.setPhone(mobile);
            User u = userService.readOne(condionUser);
            if (u == null) {
                if (uid!=null){
                    condition.setParentUserId(u.getId());
                }
                u = userService.readOne(condionUser);
                if (u==null) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "没有记录");
                }else {
                    condition.setParentUserId(u.getId());
                }
            }else {
                condition.setParentUserId(u.getId());
            }
        }

        List<UserDepartment> departments = new ArrayList<>();

        Integer count = userDepartmentService.readCount(condition);
        if (count != null && count > 0) {
            departments = userDepartmentService.readList(condition, page.getPageNumber(), page.getPageSize(), count);
            for (UserDepartment department:departments){
                User u=userService.readById(department.getParentUserId());
                if (u!=null){
                    department.setParentUserId(u.getUid().toString());
                }
                department.setRemark(GradeType.fromCode(department.getGrade()).getMsg());

            }
        }
        page.setTotalCount(count);
        respBody.add(RespCodeEnum.SUCCESS.getCode(), "成功", page, departments);
        return respBody;
    }


}
