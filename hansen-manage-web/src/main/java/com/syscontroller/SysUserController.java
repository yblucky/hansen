package com.syscontroller;

import com.base.page.Paging;
import com.base.page.RespBody;
import com.base.page.RespCodeEnum;
import com.model.SysUser;
import com.service.RedisService;
import com.sysservice.ManageUserService;
import com.utils.toolutils.ToolUtil;
import com.vo.UpdatePwVo;
import com.vo.SysUserVo;
import com.utils.ConfUtils;
import com.utils.codeutils.CryptUtils;
import com.utils.toolutils.LogUtils;
import jdk.management.resource.ResourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户控制器
 */

@Controller
@RequestMapping(value = "/user")
public class SysUserController {
    @Resource
    private ManageUserService manageUserService;//用户业务层
    @Resource
    private HttpServletRequest request;
    @Resource
    private ConfUtils confUtils;
    @Autowired
    private RedisService redisService;

    @RequestMapping(value = "/findUser", method = RequestMethod.GET)
    @ResponseBody
    public RespBody findUser() {
        RespBody respBody = new RespBody();
        try {
            String token = request.getHeader("token");
            //读取用户信息
            SysUserVo userVo = manageUserService.SysUserVo(token);
            //用户是否存在
            if (userVo != null) {
                userVo.setPassword("");
                userVo.setSalt("");
                //存在
                respBody.add(RespCodeEnum.SUCCESS.getCode(), "获取用户信息成功", userVo);
            } else {
                //不存在
                respBody.add(RespCodeEnum.ERROR.getCode(), "获取用户信息失败");
            }
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "获取用户信息异常");
            LogUtils.error("获取用户信息异常！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    @ResponseBody
    public RespBody findAll(Paging paging) {
        RespBody respBody = new RespBody();
        try {
            //保存返回数据
            List<SysUserVo> list = manageUserService.findAll(paging);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "查找所有用户信息数据成功", list);
            //保存分页对象
            paging.setTotalCount(manageUserService.findCount());
            respBody.setPage(paging);
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "查找所有用户信息数据失败");
            LogUtils.error("查找所有用户信息数据失败！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/findRoles", method = RequestMethod.GET)
    @ResponseBody
    public RespBody findRoles() {
        RespBody respBody = new RespBody();
        try {
            //保存返回数据
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "查找所有角色数据成功", manageUserService.findRoles());
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "查找所有角色数据失败");
            LogUtils.error("查找所有角色数据失败！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public RespBody add(@RequestBody SysUserVo userVo) {
        RespBody respBody = new RespBody();
        try {
            //判断用户是否存在
            SysUserVo findUser = manageUserService.findByLoginName(userVo.getLoginName());
            if (findUser == null) {
                manageUserService.add(userVo);
                respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户信息保存成功");
            } else {
                respBody.add(RespCodeEnum.ERROR.getCode(), "登录名已经存在");
            }

        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户信息保存失败");
            LogUtils.error("用户信息保存失败！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public RespBody update(@RequestBody SysUserVo userVo) {
        RespBody respBody = new RespBody();
        try {
            manageUserService.update(userVo);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户信息修改成功");
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户信息修改失败");
            LogUtils.error("用户信息修改失败！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseBody
    public RespBody delete(@RequestBody SysUserVo userVo) {
        RespBody respBody = new RespBody();
        try {
            if (ToolUtil.isEmpty(userVo)) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "获取用户信息失败，无法删除");
                return respBody;
            }
            manageUserService.delete(userVo);
            respBody.add(RespCodeEnum.SUCCESS.getCode(), "用户信息删除成功");
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "用户信息删除失败");
            LogUtils.error("用户信息删除失败！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/updatePw", method = RequestMethod.POST)
    @ResponseBody
    public RespBody updatePw(@RequestBody UpdatePwVo updatePwVo) {
        RespBody respBody = new RespBody();
        try {
            if (!updatePwVo.getNewPw().equals(updatePwVo.getOkPw())) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "新密码和确认码不一致");
                return respBody;
            }
            String token = request.getHeader("token");
            //读取用户信息
            SysUserVo userVo = manageUserService.SysUserVo(token);
            // 对输入密码进行加密
            String oldPw = CryptUtils.hmacSHA1Encrypt(updatePwVo.getOldPw(), userVo.getSalt());
            if (userVo.getPassword().equals(oldPw)) {
                //对新密码进行加密
                String newPw = CryptUtils.hmacSHA1Encrypt(updatePwVo.getNewPw(), userVo.getSalt());
                //旧密码正确，调用业务层执行密码更新
                manageUserService.updatePw(newPw, userVo.getId());
                respBody.add(RespCodeEnum.SUCCESS.getCode(), "修改密码成功");
                //更新redis数据
                userVo.setPassword(newPw);
                redisService.putObj(token, userVo, confUtils.getSessionTimeout());
            } else {
                //旧密码输入有误
                respBody.add(RespCodeEnum.ERROR.getCode(), "旧密码输入不正确");
            }
        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "修改密码失败");
            LogUtils.error("修改密码失败！", ex);
        }
        return respBody;
    }

    @RequestMapping(value = "/updatesupperpass", method = RequestMethod.POST)
    @ResponseBody
    public RespBody updateSupperPass(@RequestBody UpdatePwVo updatePwVo) {
        RespBody respBody = new RespBody();
        try {
            if (!updatePwVo.getNewPw().equals(updatePwVo.getOkPw())) {
                respBody.add(RespCodeEnum.ERROR.getCode(), "新密码和确认码不一致");
                return respBody;
            }
            String token = request.getHeader("token");
            //读取用户信息
            SysUserVo userVo = manageUserService.SysUserVo(token);
            SysUser sysUser = manageUserService.readById(userVo.getId());
            // 对输入密码进行加密
            if (ToolUtil.isNotEmpty(userVo.getRemark())) {
                if (ToolUtil.isEmpty(updatePwVo.getOldPw())) {
                    respBody.add(RespCodeEnum.ERROR.getCode(), "请先输入旧密码");
                }
                String oldPw = CryptUtils.hmacSHA1Encrypt(updatePwVo.getOldPw(), userVo.getSalt());
                if (userVo.getPassword().equals(oldPw)) {
                    //对新密码进行加密
                    String newPw = CryptUtils.hmacSHA1Encrypt(updatePwVo.getNewPw(), sysUser.getSalt());
                    //旧密码正确，调用业务层执行密码更新
                    manageUserService.updateSupperPassPw(newPw, userVo.getId());
                    respBody.add(RespCodeEnum.SUCCESS.getCode(), "修改密码成功");
                    //更新redis数据
                    userVo.setPassword(newPw);
                    redisService.putObj(token, userVo, confUtils.getSessionTimeout());
                } else {
                    //旧密码输入有误
                    respBody.add(RespCodeEnum.ERROR.getCode(), "旧密码输入不正确");
                }
            } else {
                //对新密码进行加密
                String newPw = CryptUtils.hmacSHA1Encrypt(updatePwVo.getNewPw(), userVo.getSalt());
                //旧密码正确，调用业务层执行密码更新
                manageUserService.updateSupperPassPw(newPw, userVo.getId());
                respBody.add(RespCodeEnum.SUCCESS.getCode(), "修改密码成功");
                //更新redis数据
                userVo.setPassword(newPw);
                redisService.putObj(token, userVo, confUtils.getSessionTimeout());
            }

        } catch (Exception ex) {
            respBody.add(RespCodeEnum.ERROR.getCode(), "重置超级密码失败");
            LogUtils.error("重置超级密码失败！", ex);
        }
        return respBody;
    }

    public static void main(String[] args) {
        System.out.println(CryptUtils.hmacSHA1Encrypt("123456", "QWERTYUIOPLKJHGHDFFHSDDG"));
    }
}
