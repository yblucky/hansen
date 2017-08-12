package com.hansen.controller;

import com.base.page.JsonResult;
import com.common.Token;
import com.common.base.TokenUtil;
import com.common.constant.CodeType;
import com.common.utils.numberutils.UUIDUtil;
import com.common.utils.toolutils.ToolUtil;
import com.hansen.service.ActiveCodeService;
import com.hansen.vo.CodeVo;
import com.model.ActiveCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by Administrator on 2017/8/6.
 */
@Controller
@RequestMapping("/activecode")
public class ActiveCodeController {

    @Autowired
    private ActiveCodeService activeCodeService;

    /**
     * 生成激活码和注册码
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/generateCode", method = RequestMethod.POST)
    public JsonResult generateCode(HttpServletRequest request, HttpServletResponse response, @RequestBody CodeVo vo) throws Exception {

        Token token = TokenUtil.getSessionUser(request);

        if(ToolUtil.isEmpty(vo.getCodeType())){
            return new JsonResult(1,"生成码的类型不能为空");
        }

        //创建生成码的记录
        ActiveCode activeCode = new ActiveCode();
        activeCode.setCode(UUIDUtil.generateUUID());
        activeCode.setStatus(1);
        activeCode.setOwnerId(token.getId());
        if(vo.getCodeType().intValue() == CodeType.ACTIVATECODE.getCode().intValue()){
            activeCode.setType(CodeType.ACTIVATECODE.getCode());
            activeCode.setRemark(CodeType.ACTIVATECODE.getMsg());
        }else{
            activeCode.setType(CodeType.REGISTERCODE.getCode());
            activeCode.setRemark(CodeType.REGISTERCODE.getMsg());
        }

        //生成码
        activeCodeService.createWithUUID(activeCode);

        return new JsonResult();
    }


    /**
     * 根据用户id查询所拥有的激活码和生成码
     * @param request
     * @param response
     * @param vo
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/getExistCode", method = RequestMethod.POST)
    public JsonResult getExistCode(HttpServletRequest request, HttpServletResponse response, @RequestBody CodeVo vo) throws Exception {
        Token token = TokenUtil.getSessionUser(request);


        ActiveCode model = new ActiveCode();
        model.setStatus(1);
        model.setOwnerId(token.getId());
        model.setType(vo.getCodeType());
        List<ActiveCode> codeList =  activeCodeService.readAll(model);

        return new JsonResult(codeList);
    }


}
