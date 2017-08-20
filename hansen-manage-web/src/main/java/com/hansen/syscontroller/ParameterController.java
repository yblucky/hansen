package com.hansen.syscontroller;

import com.common.utils.toolutils.LogUtils;
import com.hansen.annotation.SystemControllerLog;
import com.hansen.enums.RespCodeEnum;
import com.hansen.resp.Paging;
import com.hansen.resp.RespBody;
import com.hansen.vo.SysParameterVo;
import com.hansen.sysservice.ParameterService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 参数设置控制器
 */
@RestController
@RequestMapping("/parameter")
public class ParameterController {
	@Resource
	private  ParameterService parameterService;
	
	/**
	 * 查找列表数据
	 * @param paging 分页对象
	 * @return 响应信息
	 */
	@RequestMapping("/findAll")
	public RespBody findAll(Paging paging){
		RespBody respBody = new RespBody();
		try {
			//保存返回数据
			respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置查找所有数据成功", parameterService.findAll(paging));
			//保存分页对象
			paging.setTotalCount(parameterService.findCount());
			respBody.setPage(paging);
		} catch (Exception ex) {
			respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置查找所有数据失败");
			LogUtils.error("参数设置查找所有数据失败！",ex);
		}
		return respBody;
	}
	
	/**
	 * 新增参数设置
	 * @param parameterVo 新参数Vo
	 * @return 响应信息
	 */
	@RequestMapping("/add")
	@SystemControllerLog(description="新增参数设置")
	public RespBody add(@RequestBody SysParameterVo parameterVo){
		RespBody respBody = new RespBody();
		try {
			parameterService.add(parameterVo);
			respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置保存成功");
		} catch (Exception ex) {
			respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置保存失败");
			LogUtils.error("参数设置保存失败！",ex);
		}
		return respBody;
	}
	
	/**
	 * 修改参数设置
	 * @param parameterVo 参数Vo
	 * @return 响应信息
	 */
	@RequestMapping("/update")
	@SystemControllerLog(description="修改参数设置")
	public RespBody update(@RequestBody SysParameterVo parameterVo){
		RespBody respBody = new RespBody();
		try {
			parameterService.update(parameterVo);
			respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置保存成功");
		} catch (Exception ex) {
			respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置保存失败");
			LogUtils.error("参数设置保存失败！",ex);
		}
		return respBody;
	}
	
	/**
	 * 删除参数设置
	 * @param parameterVo 参数VO
	 * @return 响应信息
	 */
	@RequestMapping("/delete")
	@SystemControllerLog(description="删除参数设置")
	public RespBody delete(@RequestBody SysParameterVo parameterVo){
		RespBody respBody = new RespBody();
		try {
			parameterService.delete(parameterVo);
			respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置删除成功");
		} catch (Exception ex) {
			respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置删除失败");
			LogUtils.error("参数设置删除失败！",ex);
		}
		return respBody;
	}
}
