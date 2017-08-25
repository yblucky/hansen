package com.vo;

/**
 * 激活码/注册码转赠
 * Created by Administrator on 2018/08/17.
 */
public class CodeTransferVo {
    private Integer toUid;
    private String toId;
    private Integer transferNo;
    private Integer codeType;
    private String payword;


    public Integer getToUid() {
        return toUid;
    }

    public void setToUid(Integer toUid) {
        this.toUid = toUid;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public Integer getTransferNo() {
        return transferNo;
    }

    public void setTransferNo(Integer transferNo) {
        this.transferNo = transferNo;
    }

    public Integer getCodeType() {
        return codeType;
    }

    public void setCodeType(Integer codeType) {
        this.codeType = codeType;
    }

    public String getPayword() {
        return payword;
    }

    public void setPayword(String payword) {
        this.payword = payword;
    }
}
