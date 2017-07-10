package com.mall.controller;

import com.mall.core.page.JsonResult;
import com.mall.model.BaseCarousel;
import com.mall.service.BaseCarouselService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private BaseCarouselService carouselService;
    /**
     * 幻灯片广告
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public JsonResult getCarouselList(HttpServletRequest request) throws Exception {
        List<BaseCarousel> list = carouselService.readList(new BaseCarousel(), 1, 100, 100);
        return new JsonResult(list);
    }


}
