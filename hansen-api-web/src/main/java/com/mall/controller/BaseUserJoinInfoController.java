package com.mall.controller;

import com.mall.common.Token;
import com.mall.core.page.JsonResult;
import com.mall.model.BaseUserJoinInfo;
import com.mall.service.BaseUserJoinInfoService;
import com.mall.util.TokenUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/joinInfo")
public class BaseUserJoinInfoController {
	
	@Autowired
	private BaseUserJoinInfoService baseUserJoinInfoService;
    
	/**
     * 新增加盟用户
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult add(HttpServletRequest request,@RequestBody BaseUserJoinInfo joinInfo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);

        if(StringUtils.isEmpty(joinInfo.getUserName())){
            return new JsonResult(1,"加盟用户名称不能为空");
        }

        if(StringUtils.isEmpty(joinInfo.getPhone())){
            return new JsonResult(1,"加盟用户电话不能为空");
        }

        joinInfo.setStatus(1);

        baseUserJoinInfoService.create(joinInfo);

        return new JsonResult();
    }

}
