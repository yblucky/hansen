package com.vo;

import com.model.TradeOrder;

/**
 * 用户任务
 * Created  on 2018/08/17.
 */
public class TradeOrderVo extends TradeOrder {

    /**
     * 用户姓名
     */
    private String nickName;
    /**
     * 等级的中文名
     */
    private String cardGradeName;
    /**
     * 状态的中文名
     */
    private String statusName;

    /**
     * 奖励类型
     */
    protected String rewardType;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCardGradeName() {
        return cardGradeName;
    }

    public void setCardGradeName(String cardGradeName) {
        this.cardGradeName = cardGradeName;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }
}
