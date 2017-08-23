package com.vo;

/**
 * 激活码转赠
 * Created by Administrator on 2018/08/17.
 */
public class CodeTransferVo {
    private Integer toUid;
    private String toId;
    private Integer transferNo;


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
}
