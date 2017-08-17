package com.model;


import com.common.BaseModel;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
public class Parameter extends BaseModel {

    public static final long serialVersionUID = -76937973371276572L;

    public static final String EQUITY_RPCALLOWIP = "equityRpcallowip";
    public static final String EQUITY_RPCPASSWORD = "equityRpcPassword";
    public static final String EQUITY_DAEMON = "equityDaemon";
    public static final String EQUITY_RPCUSER = "equityRpcser";
    public static final String EQUITY_SERVER = "equityServer";
    public static final String EQUITY_RPCPORT = "equityRpcport";
    public static final String EQUITY_LISTEN = "equityListen";
    public static final String EQUITY_ADDNODE = "equityAddnode";
    public static final String JUMPRE_WARDLIMIT = "jumpRewardLimit";
    public static final String PAY_RPCALLOWIP = "payRpcallowip";
    public static final String PAY_RPCUSER = "payRpcuser";
    public static final String PAY_RPCPORT = "payRpcport";
    public static final String PAY_SERVER = "payServer";
    public static final String PAY_LISTEN = "payListen";
    public static final String PAY_ADDNODE = "payAddnode";
    public static final String PAY_RPCPASSWORD = "payRpcpassword";
    public static final String TRADE_ADDNODE = "tradeAddnode";
    public static final String PAY_DAEMON = "payDaemon";
    public static final String TRADE_RPCALLOWIP = "tradeRpcallowip";
    public static final String TRADE_RPCUSER = "tradeRpcuser";
    public static final String TRADE_RPCPORT = "tradeRpcport";
    public static final String TRADE_SERVER = "tradeServer";
    public static final String TRADE_LISTEN = "tradeListen";
    public static final String TRADE_RPCPASSWORD = "tradeRpcpassword";
    public static final Integer  WALLET_PAGE_SIZE = 20;


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
     * 奖励到账拆分周期
     **/
    public static final String REWARDINTERVAL = "rewardInterval";
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
    /**
     * 收益累计完成任务次数
     **/
    public static final String TASKINTERVAL = "taskInterval";



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
