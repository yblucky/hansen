package com.mall.model;

import com.mall.common.BaseModel;


/**
 * 商铺表
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class MallStore extends BaseModel {

    private static final long serialVersionUID = 5898754554769429761L;
    /**
     * 商品分类ID
     */
    private String userId;
    /**
     * 商铺名称
     */
    private String storeName;
    /**
     * 默认图片
     */
    private String icon;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 县
     */
    private String county;
    /**
     * 详情地址
     */
    private String address;
    /**
     * 经营类别
     */
    private String businessScope;
    /**
     * 商铺介绍
     */
    private String detail;
    /**
     * 是否推荐
     */
    private Integer isRecommend;
    /**
     * '客服电话
     */
    private String servicePhone;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getServicePhone() {
        return servicePhone;
    }

    public void setServicePhone(String servicePhone) {
        this.servicePhone = servicePhone;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessScope() {
        return businessScope;
    }

    public void setBusinessScope(String businessScope) {
        this.businessScope = businessScope;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getIsRecommend() {
        return isRecommend;
    }

    public void setIsRecommend(Integer isRecommend) {
        this.isRecommend = isRecommend;
    }
}
