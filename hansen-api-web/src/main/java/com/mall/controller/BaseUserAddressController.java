package com.mall.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mall.common.Token;
import com.mall.core.page.JsonResult;
import com.mall.model.BaseUserAddress;
import com.mall.service.BaseUserAddressService;
import com.mall.util.TokenUtil;

@Controller
@RequestMapping("/address")
public class BaseUserAddressController {
	
	@Autowired
	private BaseUserAddressService baseUserAddressService;
	
	
	/**
     * 地址列表
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult addressList(HttpServletRequest request) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
    	
//    	Token token = (Token) TokenUtil.getTokenObject("eyJuaWNrTmFtZSI6IuadsCIsImlkIjoiRjJDRURGMjIxMEMyNDNDNEE5M0YyODZEMjE2NTY5RUEiLCJ0aW1lIjoxNDk0NzUwNjAwMDcyfQ**");
        
        List<BaseUserAddress> addresssList = baseUserAddressService.getAddressListByUserId(token.getId());

        return new JsonResult(addresssList);
        
    }
    
	/**
     * 新增地址列表
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public JsonResult add(HttpServletRequest request,@RequestBody BaseUserAddress address) throws Exception {
        Token token = TokenUtil.getSessionUser(request);
    	
//    	Token token = (Token) TokenUtil.getTokenObject("eyJuaWNrTmFtZSI6IuadsCIsImlkIjoiRjJDRURGMjIxMEMyNDNDNEE5M0YyODZEMjE2NTY5RUEiLCJ0aW1lIjoxNDk0NzUwNjAwMDcyfQ**");
        		
    	String addr = address.getAddr();
    	String province = address.getProvince();
    	String county = address.getCounty();
    	String city = address.getCity();

    	if(StringUtils.isEmpty(addr)){
    		return new JsonResult(3,"详细地址不能为空");
    	}
/*    	if(StringUtils.isEmpty(province)){
    		return new JsonResult(3,"省份不能为空");
    	}
    	if(StringUtils.isEmpty(county)){
    		return new JsonResult(4,"市不能为空");
    	}
    	if(StringUtils.isEmpty(city)){
    		return new JsonResult(5,"县不能为空");
    	}
		
		if(addr.indexOf(city) < 0){
			 addr  = city + addr;
		}
		if(addr.indexOf(county) < 0){
			 addr = county + addr;
		}
		if(addr.indexOf(province) < 0){
			addr = province + addr;
		}*/
		
		address.setUserId(token.getId());
		
		baseUserAddressService.create(address);

        return new JsonResult();
    }

}
