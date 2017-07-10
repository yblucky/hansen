package com.mall.service.impl;

import com.mall.constant.OrderStatus;
import com.mall.constant.OrderType;
import com.mall.constant.RecordType;
import com.mall.core.dao.CommonDao;
import com.mall.core.service.impl.CommonServiceImpl;
import com.mall.mapper.MallOrderMapper;
import com.mall.model.*;
import com.mall.service.*;
import com.mall.util.JPushUtil;
import com.mall.util.OrderNoUtil;
import com.mall.util.PoundageUtil;
import com.mall.util.UUIDUtil;
import com.mall.vo.CartVo;
import com.mall.vo.OrderPurchaseVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 
 * @author zhuzh
 * @date 2016年12月28日
 */
@Service
public class MallOrderServiceImpl extends CommonServiceImpl<MallOrder> implements MallOrderService {
	private static final Logger logger = LoggerFactory.getLogger(MallOrderServiceImpl.class);

	@Autowired
	private MallOrderMapper mallOrderMapper;
	@Autowired
	private MallGoodsService goodsService;
//	@Autowired
//	private MallOrderService orderService;
	@Autowired
	private MallOrderDetailService mallOrderDetailService;
	@Autowired
	private BaseUserService userService;
	@Autowired
	private MallRecordService recordService;
	@Override
	protected CommonDao<MallOrder> getDao() {
		return mallOrderMapper;
	}

	@Override
	protected Class<MallOrder> getModelClass() {
		return MallOrder.class;
	}


	@Override
	@Transactional
	public MallOrder createOrder(BaseUser user, List<MallGoods> goodsList, OrderPurchaseVo orderPurchaseVo) throws Exception {

		//定义商品订单变量
		MallOrder order = new MallOrder();
		order.setId(UUIDUtil.getUUID());
		order.setOrderNo(OrderNoUtil.get());
		order.setUserId(user.getId());
		order.setPayWay(orderPurchaseVo.getPayType());
		order.setStatus(OrderStatus.PENDING.getCode());//待处理(待付款)
		// 收货信息
		order.setUserName(orderPurchaseVo.getUserName());
		order.setPhone(orderPurchaseVo.getPhone());
		order.setAddr(orderPurchaseVo.getAddr());

		//如果商品订单列表的个数为1，代表一件商品，不作循环
		if(goodsList.size() == 1){
			//直接购买(1个商品)
			order.setGoodsId(goodsList.get(0).getId());
			order.setGoodsName(goodsList.get(0).getName());
			order.setDetail(goodsList.get(0).getDetail());
			order.setIcon(goodsList.get(0).getIcon());
			order.setPrice(goodsList.get(0).getPrice());
			order.setStoreId(goodsList.get(0).getStoreId());
			order.setStoreName(goodsList.get(0).getStoreName());
			order.setOrderType(OrderType.PURCHASE.getCode());
			order.setRemark(OrderType.PURCHASE.getMsg());
			order.setNum(orderPurchaseVo.getCartList().get(0).getNum());

		}else {
			//购物车购买(多个商品)
			MallGoods goods = null;
			MallOrderDetail goodsWinDetail = null;
			//定义一个变量保存总价格
			Double totalPrice = 0d;
			//定义一个变量保存数量
			Integer totalNum = 0;

			for(CartVo cartVo:orderPurchaseVo.getCartList()){
				//根据商品id查询商品信息
				goods = goodsService.readById(cartVo.getGoodsId());
				MallOrderDetail orderDetail = new MallOrderDetail();
				orderDetail.setNum(cartVo.getNum());
				orderDetail.setGoodsId(goods.getId());
				orderDetail.setGoodsName(goods.getName());
				orderDetail.setOrderId(order.getId());
				orderDetail.setIcon(goods.getIcon());
				orderDetail.setOrderNo(order.getOrderNo());
				orderDetail.setPrice(goods.getPrice());
				orderDetail.setStoreId(goods.getStoreId());
				orderDetail.setStoreName(goods.getStoreName());
				orderDetail.setStatus(0);
				orderDetail.setUserId(user.getId());
				orderDetail.setOriginalPrice(goods.getOriginalPrice());

				//计算价格
				totalPrice = PoundageUtil.getPoundage(totalPrice + (goods.getPrice() * cartVo.getNum()),1d);

				//计算数量
				totalNum = totalNum + cartVo.getNum();
				if (StringUtils.isEmpty(order.getIcon())){
					order.setIcon(goods.getIcon());
				}
			}

			order.setGoodsId(null);
			order.setGoodsName(null);
			order.setDetail(null);
			order.setPrice(totalPrice);
			order.setOrderType(OrderType.CARTPAY.getCode());
			order.setRemark(OrderType.CARTPAY.getMsg());
			order.setNum(totalNum);
		}

		//新增订单
		this.create(order);
		return order;
	}

	/**
	 * 直接购买订单回调业务
	 * @param user
	 * @param orderNo
	 * @return
	 * @throws Exception
	 */
	@Transactional
	@Override
	public Boolean purchaseCallBack(BaseUser user, String orderNo) throws Exception {
		return  false;
	}

	@Override
	public MallOrder getByOrderNo(String orderNo, String userId) throws Exception {

		if (StringUtils.isBlank(orderNo) || StringUtils.isBlank(userId)) {
			return null;
		}
		return mallOrderMapper.getByOrderNo(orderNo, userId);
	}

}
