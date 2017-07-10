package com.mall.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @date 2017年2月4日
 */
public class OrderPurchaseVo implements Serializable {

    private static final long serialVersionUID = 8041418456665782640L;

    /**
     * 收件人姓名
     */
    private String userName;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 收货地址
     */
    private String addr;
    /**
     * 支付类型
     */
    private Integer payType;
    /**
     * 商品列表
     */
    private List<CartVo> cartList;


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public List<CartVo> getCartList() {
        return cartList;
    }

    public void setCartList(List<CartVo> cartList) {
        this.cartList = cartList;
    }
}
