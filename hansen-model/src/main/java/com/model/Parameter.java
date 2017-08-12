package com.model;


import com.common.BaseModel;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
public class Parameter extends BaseModel {

    public static final long serialVersionUID = -76937973371276572L;

    public static final String EQUITYRPCALLOWIP = "equityRpcallowip";
    public static final String EQUITYRPCPASSWORD = "equityRpcPassword";
    public static final String EQUITYDAEMON = "equityDaemon";
    public static final String EQUITYRPCUSER = "equityRpcser";
    public static final String EQUITYSERVER = "equityServer";
    public static final String EQUITYRPCPORT = "equityRpcport";
    public static final String EQUITYLISTEN = "equityListen";
    public static final String EQUITYADDNODE = "equityAddnode";
    public static final String JUMPREWARDLIMIT = "jumpRewardLimit";
    public static final String PAYRPCALLOWIP = "payRpcallowip";
    public static final String PAYRPCUSER = "payRpcuser";
    public static final String PAYRPCPORT = "payRpcport";
    public static final String PAYSERVER = "payServer";
    public static final String PAYLISTEN = "payListen";
    public static final String PAYADDNODE = "payAddnode";
    public static final String PAYRPCPASSWORD = "payRpcpassword";
    public static final String TRADEADDNODE = "tradeAddnode";
    public static final String PAYDAEMON = "payDaemon";
    public static final String TRADERPCALLOWIP = "tradeRpcallowip";
    public static final String TRADERPCUSER = "tradeRpcuser";
    public static final String TRADERPCPORT = "tradeRpcport";
    public static final String TRADESERVER = "tradeServer";
    public static final String TRADELISTEN = "tradeListen";
    public static final String TRADERPCPASSWORD = "tradeRpcpassword";


    /**
     * 激活码价格
     **/
    public static final String ACTIVECODEPRICE = "activeCodePrice";
    /**
     * 注册码价格
     **/
    public static final String REGISTERCODEPRICE = "registerCodePrice";
    /**
     * 生成保单时支付币必须有的比例数量
     **/
    public static final String INSURANCEPAYSCALE = "insurancePayScale";
    /**
     * 生成保单时交易币必须有的比例数量
     **/
    public static final String INSURANCETRADESCALE = "insuranceTradeScale";
    /**
     * 直推奖一代6%
     **/
    public static final String PUSHFIRSTREFERRERSCALE = "pushFirstReferrerScale";
    /**
     * 直推奖二代4%
     **/
    public static final String PUSHSECONDREFERRERSCALE = "pushSecondReferrerScale";
    /**
     * 管理奖一代2%
     **/
    public static final String MANAGEFIRSTREFERRERSCALE = "manageFirstReferrerScale";
    /**
     * 管理奖二代1%
     **/
    public static final String MANAGESECONDREFERRERSCALE = "manageSecondReferrerScale";
    /**
     * 星级奖最大分销比例限制
     **/
    public static final String MAXGRADESCALE = "maxGradeScale";


    /**
     * 人民币兑换支付币汇率
     **/
    public static final String RMBCONVERTPAYSCALE = "rmbConvertPayScale";
    /**
     * 人民币兑换交易币汇率
     **/
    public static final String RMBCONVERTTRADESCALE = "rmbConvertTradeScale";
    /**
     * 人民币兑换股权币汇率
     **/
    public static final String RMBCONVERTEQUITYSCALE = "rmbConvertEquityScale";

    /**
     * 收益间隔时间
     **/
    public static final String INTERVAL = "interval";
    /**
     * 收益转化成支付币数量比例
     **/
    public static final String REWARDCONVERTPAYSCALE = "rewardConvertPayScale";
    /**
     * 收益转化成交易币数量比例
     **/
    public static final String REWARDCONVERTTRADESCALE = "rewardConvertTradeScale";
    /**
     * 收益转化成股权币数量比例
     **/
    public static final String REWARDCONVERTEQUITYSCALE = "rewardConvertEquityScale";
    /**
     * 原点升级（1开  2关）
     **/
    public static final String UPGRADEORIGINSWITCH = "upGradeOriginSwitch";
    /**
     * 覆盖升级（1开  2关）
     **/
    public static final String UPGRADECOVERSWITCH = "upGradeCoverSwitch";
    /**
     * 直推奖烧伤（1开  2关）
     **/
    public static final String BURNSPUSH = "burnsPush";
    /**
     * 级差奖烧伤（1开  2关）
     **/
    public static final String BURNSLEVEL = "burnsLevel";
    /**
     * 管理奖烧伤（1开  2关）
     **/
    public static final String BURNSMANAGE = "burnsManage";


    private String name;
    private String value;
    private String kind;
    private String title;
    private String remark;
    private String groupType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroupType() {
        return groupType;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }
}
