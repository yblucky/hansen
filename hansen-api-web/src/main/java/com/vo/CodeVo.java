package com.vo;

/**
 * 激活码和注册码vo类
 * Created by Administrator on 2017/7/19.
 */
public class CodeVo {

    /**
     * 需要生成码的类型(1:激活码 2 注册码)
     */
    private Integer codeType;

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }
}
