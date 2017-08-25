package com.controller;

import com.base.page.JsonResult;
import com.model.FeedBack;
import com.service.FeedBackService;
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

@Controller
@RequestMapping("/index")
public class IndexController {

    @Autowired
    private FeedBackService  feedBackService;
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
        feedBack.setUserId(vo.getUserId());
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

}
