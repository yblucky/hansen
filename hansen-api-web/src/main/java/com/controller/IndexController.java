package com.controller;

import com.Token;
import com.base.TokenUtil;
import com.base.page.JsonResult;
import com.base.page.Page;
import com.base.page.PageResult;
import com.base.page.ResultCode;
import com.model.FeedBack;
import com.model.User;
import com.service.FeedBackService;
import com.service.UserService;
import com.utils.toolutils.ToolUtil;
import com.vo.FeedBackVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private FeedBackService  feedBackService;
    @Autowired
    private UserService userService;

    /**
     *  意见反馈
     */
    @ResponseBody
    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public JsonResult setting(HttpServletRequest request, @RequestBody FeedBackVo vo) throws Exception {

        if (ToolUtil.isEmpty(vo.getTitle()) || ToolUtil.isEmpty(vo.getType()) || ToolUtil.isEmpty(vo.getDetail())){
            return  new JsonResult(-1,"反馈信息不能为空");
        }
        FeedBack feedBack=new FeedBack();
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = null;
        if(token != null) {
            loginUser=userService.readById(token.getId());
        }
        if (loginUser != null) {
            feedBack.setUserId(loginUser.getId());
        }
        feedBack.setType(vo.getType());
        feedBack.setDetail(vo.getDetail());
        feedBack.setTitle(vo.getTitle());
        feedBack.setPhone(vo.getPhone());
        if ((vo.getIcons() != null) && vo.getIcons().size()>0){
            feedBack.setIcons(StringUtils.join(vo.getIcons(),","));
        }
        feedBackService.create(feedBack);
        return new JsonResult();
    }

    /**
     * 意见消息
     */
    @ResponseBody
    @RequestMapping(value = "/feedbacklist", method = RequestMethod.GET)
    public JsonResult settinglist(HttpServletRequest request, Page page) throws Exception {
        FeedBack feedBack = new FeedBack();
        Token token = TokenUtil.getSessionUser(request);
        User loginUser = null;
        if (token != null) {
            loginUser = userService.readById(token.getId());
        }
        if (loginUser != null) {
            feedBack.setUserId(loginUser.getId());
        }
        int count = feedBackService.readCount(feedBack);
        List<FeedBack> list = null;
        if(count > 0) {
            list = feedBackService.readList(feedBack, page.getPageNo(), page.getPageSize(),count);
        }
        PageResult pageResult = new PageResult(page.getPageNo(), page.getPageSize(), count, list);
        return new JsonResult(pageResult);
    }

}
