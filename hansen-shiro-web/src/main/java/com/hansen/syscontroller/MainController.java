/* 
 * 文件名：MainController.java  
 * 版权：Copyright 2016-2017 炎宝网络科技  All Rights Reserved by
 * 修改人：邱深友  
 * 创建时间：2017年6月14日
 * 版本号：v1.0
*/
package com.hansen.syscontroller;

import com.common.utils.toolutils.LogUtils;
import com.hansen.enums.RespCodeEnum;
import com.hansen.resp.RespBody;
import com.hansen.sysservice.MainService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 主界面控制器
 */
@RestController
@RequestMapping(value = "/main")
public class MainController {
	@Resource
	private MainService mainService;
	
	/**
	 * 加载用户菜单
	 * @param roleId 权限编号
	 * @return 响应对象
	 */
	@RequestMapping("/findMenu")
	public RespBody findMenu(String roleId){
		RespBody respBody = new RespBody();
		try {
			respBody.add(RespCodeEnum.SUCCESS.getCode(), "加载菜单成功",mainService.findMenu(roleId));
		} catch (Exception ex) {
			respBody.add(RespCodeEnum.ERROR.getCode(), "加载菜单失败");
			LogUtils.error("加载菜单失败！",ex);
		}
		return respBody;
	}
}
