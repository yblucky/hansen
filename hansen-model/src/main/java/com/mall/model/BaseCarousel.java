package com.mall.model;

import com.mall.common.BaseModel;

/**
 * 广告类
 *
 * @author zhuzh
 * @date 2016年12月7日
 */
public class BaseCarousel extends BaseModel {

    private static final long serialVersionUID = -7693797776371276572L;
    /**
     * 广告标题
     */
    private String title;
    /**
     * 广告图片
     */
    private String carouselImg;
    /**
     * 广告链接
     */
    private String href;
    /**
     * 排序号
     */
    private Integer rank;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCarouselImg() {
        return carouselImg;
    }

    public void setCarouselImg(String carouselImg) {
        this.carouselImg = carouselImg;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }
}
