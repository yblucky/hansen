package com.mall.controller;

import com.mall.constant.StatusType;
import com.mall.core.page.JsonResult;
import com.mall.core.page.Page;
import com.mall.core.page.PageResult;
import com.mall.model.MallGoods;
import com.mall.model.MallSort;
import com.mall.service.MallGoodsService;
import com.mall.service.MallSortService;
import com.mall.vo.GoodsVo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/goods")
public class GoodsController extends BaseController<MallGoods>{

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private MallGoodsService goodsService;
    @Autowired
    private MallSortService goodsSortService;

    /**
     * 商品分类
     */
    @ResponseBody
    @RequestMapping(value = "/sortlist", method = RequestMethod.GET)
    public JsonResult goodsSorts(HttpServletRequest request, MallSort model) throws Exception {
        Integer totalSize = goodsSortService.readCount(model);
        List<MallSort> list = goodsSortService.readList(model, 1, 100, totalSize);
        PageResult<MallSort> page = new PageResult<>(1,100,totalSize,list);
        return new JsonResult(page);
    }

    /**
     * 商品详情
     */
    @ResponseBody
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public JsonResult goodsDetail(HttpServletRequest request, String goodsId) throws Exception {

        if(StringUtils.isEmpty(goodsId)){
            return new JsonResult(3,"商品id不能为空");
        }

        MallGoods goods = goodsService.readById(goodsId);
        if (goods == null) {
            return new JsonResult(3,"商品不存在");
        }

        GoodsVo goodsVo = new GoodsVo();
        BeanUtils.copyProperties(goodsVo, goods);

        return new JsonResult(goodsVo);
    }


    @Override
    public JsonResult index(HttpServletRequest request, MallGoods model, Page page) {
        return null;
    }

    /**
     * 添加商品
     */
    @ResponseBody
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @Override
    public JsonResult add(HttpServletRequest request, MallGoods model) throws Exception{

        if (model == null) {
            return new JsonResult(1, "请填写商品资料");
        }
        if (model.getIcon() == null) {
            return new JsonResult(2, "请上传商品图片");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(model.getName())) {
            return new JsonResult(3, "请填写商品名称");
        }
        if (model.getPrice() == null || model.getPrice() <= 0) {
            return new JsonResult(4, "请填写商品价格");
        }

        if (model.getStock() == null || model.getStock() < 1) {
            return new JsonResult(5, "商品库存不能小于1件");
        }
        if (org.apache.commons.lang3.StringUtils.isBlank(model.getSortId())) {
            return new JsonResult(6, "请选择商品类别");
        }

        MallGoods goods = new MallGoods();
        BeanUtils.copyProperties(goods, model);
        goods.setId(null);
        if (model.getOriginalPrice() == null || model.getOriginalPrice() <= 0) {
            goods.setOriginalPrice(goods.getPrice());
        }

        try {
            goodsService.create(goods);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new JsonResult();
    }

    /**
     * 编辑商品(包括上下架、修改库存、删除)
     */
    @ResponseBody
    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @Override
    public JsonResult edit(HttpServletRequest request, MallGoods model) throws Exception{

        if (model == null) {
            return new JsonResult(1, "请填写商品资料");
        }
        MallGoods goods = goodsService.readById(model.getId());
        if (null == goods) {
            return new JsonResult(2, "商品不存在已或已删除");
        }

        if (goods.getStatus() == StatusType.TRUE.getCode()) {
            return new JsonResult(3, "商品售卖中，禁止修改");
        }

        if (model.getPrice() != null && model.getPrice() <= 0) {
            return new JsonResult(5, "请填写商品价格");
        }

        if (model.getStock() != null && model.getStock() < 1) {
            return new JsonResult(6, "商品库存不能小于1件");
        }
        if("".equalsIgnoreCase(model.getIcon())){
            model.setIcon(null);
        }

        MallGoods editGoods = new MallGoods();
        BeanUtils.copyProperties(editGoods,model);

        goodsService.updateById(goods.getId(), editGoods);
        return new JsonResult();
    }

    /**
     * 商品列表
     */
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @Override
    public JsonResult list(HttpServletRequest request, MallGoods model, Page page) throws Exception {
        int count = goodsService.readCount(model);
        List<MallGoods> goodsList = goodsService.readList(model, page.getPageNo(), page.getPageSize(), count);
        List<GoodsVo> voList = new ArrayList<GoodsVo>();
        for(MallGoods goods : goodsList){
            GoodsVo vo = new GoodsVo();
            BeanUtils.copyProperties(vo,goods);
            voList.add(vo);
        }
        PageResult<GoodsVo> result = new PageResult<GoodsVo>(page.getPageNo(), page.getPageSize(), count, voList);
        return new JsonResult(result);
    }



    /**
     * 批量删除
     */
    @ResponseBody
    @RequestMapping(value = "/batchDel", method = RequestMethod.POST)
    public JsonResult batchDel(HttpServletRequest request, @RequestParam(value = "ids[]")String[] ids) throws Exception{

        if (ids == null || ids.length <=0) {
            return new JsonResult(1, "请选择要删除的选项");
        }
        List<String> list = new ArrayList<String>();
        for(String id : ids){
            list.add(id);
        }
        goodsService.batchDel(list);
        return new JsonResult();
    }
}
