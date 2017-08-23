package com.hansen.syscontroller;

import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.hansen.service.RoleService;
import com.hansen.vo.SysRolePermissionVo;
import com.utils.toolutils.LogUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/role")
public class RoleController {
    @Resource
    private RoleService roleService;

    /**
     * 加载菜单集合
     *
     * @param roleId
     * @return
     */
    @RequestMapping("/findMenus")
    public RespBody findMenus(String roleId) {
        RespBody respBody = new RespBody();
        try {
            //保存返回数据
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "查找所有菜单数据成功", roleService.findMenus(roleId));
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "查找所有菜单数据失败");
            LogUtils.error("查找所菜单数据失败！", ex);
        }
        return respBody;
    }

    /**
     * 保存角色权限关系
     *
     * @return
     */
    @RequestMapping("/saveRolePerm")
    public RespBody saveRolePerm(@RequestBody SysRolePermissionVo rolePerm) {
        RespBody respBody = new RespBody();
        try {
            //调用业务层保存数据
            roleService.saveRolePerm(rolePerm);
            //返回客户端
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "保存角色权限成功");
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "保存角色权限失败");
            LogUtils.error("保存角色权限失败！", ex);
        }
        return respBody;
    }

}
