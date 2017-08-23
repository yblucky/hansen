package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.constant.UserStatusType;
import com.service.TaskService;
import com.service.UserService;
import com.model.Task;
import com.model.User;
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
public class TaskController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TaskService taskService;


    /**
     * 获取任务列表
     *
     * @param request
     * @param response
     * @param page
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JsonResult getTask(HttpServletRequest request, HttpServletResponse response, Page page) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return fail("用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return new JsonResult(ResultCode.MANGE_ERROR.getCode(), "账号未激活");
        }
        if (page.getPageNo() == null) {
            page.setPageNo(1);
        }
        if (page.getPageSize() == null) {
            page.setPageSize(10);
        }
        List<Task> taskList = null;
        Task condition = new Task();
        Integer count = taskService.readCount(condition);
        if (count != null && count > 0) {
            taskList = taskService.readList(condition, page.getPageNo(), page.getPageSize(), count);
        } else {
            taskList = Collections.emptyList();
        }
        PageResult<Task> pageResult = new PageResult<>();
        pageResult.setPageNo(page.getPageNo());
        pageResult.setPageSize(page.getPageSize());
        pageResult.setTotalSize(count);
        pageResult.setRows(taskList);
        return success(ResultCode.MANGE_SUCCESS,pageResult);
    }


    /**
     * 新增任务
     *
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/add")
    public JsonResult addTask(HttpServletRequest request, HttpServletResponse response, @RequestBody Task vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return fail("用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return fail("账号未激活");
        }
        if (ToolUtil.isEmpty(vo.getTitle())) {
            return fail("任务标题不能为空");
        }
        if (ToolUtil.isEmpty(vo.getLink())) {
            return fail("任务链接不能为空");
        }
        taskService.create(vo);
        return success(ResultCode.MANGE_SUCCESS,"新增成功");
    }

    /**
     * 修改任务
     *
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/update")
    public JsonResult doTask(HttpServletRequest request, HttpServletResponse response, @RequestBody Task vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
        User user = userService.readById(token.getId());
        if (user == null) {
            return fail("用户不存在");
        }
        if (UserStatusType.ACTIVATESUCCESSED.getCode() != user.getStatus()) {
            return fail("账号未激活");
        }
        if (ToolUtil.isEmpty(vo.getId())) {
            return fail("任务id不能为空");
        }
        if (ToolUtil.isEmpty(vo.getTitle())) {
            return fail("任务标题不能为空");
        }
        if (ToolUtil.isEmpty(vo.getLink())) {
            return fail("任务链接不能为空");
        }
        taskService.updateById(vo.getId(), vo);
        return success(ResultCode.MANGE_SUCCESS," 更新成功");
    }
}
