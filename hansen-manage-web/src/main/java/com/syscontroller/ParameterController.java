package com.syscontroller;

import com.annotation.SystemControllerLog;
import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.sysservice.ManageParameterService;
import com.vo.SysParameterVo;
import com.utils.toolutils.LogUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 参数设置控制器
 */
@Controller
@RequestMapping("/parameter")
public class ParameterController {
    @Resource
    private ManageParameterService manageParameterService;

    /**
     * 查找列表数据
     *
     * @param paging 分页对象
     * @return 响应信息
     */
    @RequestMapping(value = "/findAll",method = RequestMethod.GET)
    @ResponseBody
    public RespBody findAll(Paging paging) {
        RespBody respBody = new RespBody();
        try {
            //保存返回数据
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置查找所有数据成功", manageParameterService.findAll(paging));
            //保存分页对象
            paging.setTotalCount(manageParameterService.findCount());
            respBody.setPage(paging);
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置查找所有数据失败");
            LogUtils.error("参数设置查找所有数据失败！", ex);
        }
        return respBody;
    }

    /**
     * 新增参数设置
     *
     * @param parameterVo 新参数Vo
     * @return 响应信息
     */
    @RequestMapping(value = "/add",method = RequestMethod.POST)
    @SystemControllerLog(description = "新增参数设置")
    @ResponseBody
    public RespBody add(@RequestBody SysParameterVo parameterVo) {
        RespBody respBody = new RespBody();
        try {
            manageParameterService.add(parameterVo);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置保存成功");
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置保存失败");
            LogUtils.error("参数设置保存失败！", ex);
        }
        return respBody;
    }

    /**
     * 修改参数设置
     *
     * @param parameterVo 参数Vo
     * @return 响应信息
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    @SystemControllerLog(description = "修改参数设置")
    @ResponseBody
    public RespBody update(@RequestBody SysParameterVo parameterVo) {
        RespBody respBody = new RespBody();
        try {
            manageParameterService.update(parameterVo);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置保存成功");
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置保存失败");
            LogUtils.error("参数设置保存失败！", ex);
        }
        return respBody;
    }

    /**
     * 删除参数设置
     *
     * @param parameterVo 参数VO
     * @return 响应信息
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    @SystemControllerLog(description = "删除参数设置")
    @ResponseBody
    public RespBody delete(@RequestBody SysParameterVo parameterVo) {
        RespBody respBody = new RespBody();
        try {
            manageParameterService.delete(parameterVo);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "参数设置删除成功");
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "参数设置删除失败");
            LogUtils.error("参数设置删除失败！", ex);
        }
        return respBody;
    }
}
