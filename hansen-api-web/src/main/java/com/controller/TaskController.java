package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.UserStatusType;
import com.service.*;
import com.vo.TaskVo;
import com.model.User;
import com.model.UserTask;
import com.utils.toolutils.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private UserTaskService userTaskService;

    @Autowired
    private UserSignService userSignService;


    /**
     * 获取用户任务列表
     *
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getTaskInfo", method = RequestMethod.GET)
    public JsonResult getTask(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "账号未激活");
        }
        userTaskService.assignUserTask(user.getId());
        UserTask condition = new UserTask();
        condition.setUserId(user.getId());
        Integer count = userTaskService.readCount(condition);
        List<UserTask> taskList = null;
        if (count != null && count > 0) {
            taskList = userTaskService.readList(condition, page.getPageNo(), page.getPageSize(), count);
        } else {
            taskList = Collections.emptyList();
        }
        PageResult<UserTask> pageResult = new PageResult<>();
        pageResult.setPageNo(page.getPageNo());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalSize(count);
        pageResult.setRows(taskList);
        Integer compelteTaskCount = userTaskService.readCompeleteUserTaskCount(user.getId());
        Double signCount = userSignService.readSignCount(user.getId());
        pageResult.getExtend().put("compelteTaskCount",compelteTaskCount);
        pageResult.getExtend().put("signCount",signCount);
        return new JsonResult(pageResult);
    }


    /**
     * 用户点击任务
     *
     * @param request
     * @param response
     * @param taskvo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/dotask", method = RequestMethod.POST)
    public JsonResult doTask(HttpServletRequest request, HttpServletResponse response, @RequestBody TaskVo taskvo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.ERROR.getCode(), "账号未激活");
        }
        if (ToolUtil.isEmpty(taskvo.getTaskId())) {
            return new JsonResult(ResultCode.ERROR.getCode(), "任务id不能为空");
        }
        UserTask userTask = userTaskService.readById(taskvo.getUserTaskId());
        if (userTask == null) {
            return new JsonResult(ResultCode.ERROR.getCode(), "任务不存在");
        }
        userTaskService.doTask(user.getId(), userTask);
        return new JsonResult(userTask);
    }
}
