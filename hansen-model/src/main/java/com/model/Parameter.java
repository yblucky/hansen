package com.model;


import com.common.BaseModel;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
public class Parameter extends BaseModel {

    public static final long serialVersionUID = -76937973371276572L;

    public static final String EQUITYRPCPASSWORD = "equityRpcPassword";
    public static final String EQUITYDAEMON = "equityDaemon";
    public static final String EQUITYRPCUSER = "equityRpcuser";
    public static final String EQUITYSERVER = "equityServer";
    public static final String EQUITYLISTEN = "equityListen";
    public static final String EQUITYADDNODE = "equityAddnode";
    public static final String JUMPREWARDLIMIT = "jumpRewardLimit";
    public static final String PAYRPCALLOWIP = "payRpcallowip";
    public static final String PAYRPCUSER = "payRpcuser";
    public static final String PAYSERVER = "payServer";
    public static final String PAYLISTEN = "payListen";
    public static final String PAYADDNODE = "payAddnode";
    public static final String PAYRPCPASSWORD = "payRpcpassword";
    public static final String TRADEADDNODE = "tradeAddnode";
    public static final String TRADEDAEMON = "V";
    public static final String PAYDAEMON = "PayDaemon";
    public static final String TRADERPCALLOWIP = "tradeRpcallowip";
    public static final String TRADERPCUSER = "tradeRpcuser";
    public static final String TRADESERVER = "tradeServer";
    public static final String TRADELISTEN = "tradeListen";
    public static final String TRADERPCPASSWORD = "tradeRpcpassword";
    public static final String ACTIVECODEPRICE = "activeCodePrice";
    public static final String REGISTERCODEPRICE = "registerCodePrice";
    public static final String INSURANCEPAYSCALE = "insurancePayScale";
    public static final String INSURANCETRADESCALE = "insuranceTradeScale";
    public static final String PUSHFIRSTREFERRERSCALE = "pushFirstReferrerScale";
    public static final String PUSHSECONDREFERRERSCALE = "pushSecondReferrerScale";
    public static final String MANAGEFIRSTREFERRERSCALE = "manageFirstReferrerScale";
    public static final String MANAGESECONDREFERRERSCALE = "manageSecondReferrerScale";
    public static final String MAXGRADESCALE = "maxGradeScale";
    public static final String RMBCONVERTPAYSCALE = "rmbConvertPayScale";
    public static final String RMBCONVERTTRADESCALE = "rmbConvertTradeScale";
    public static final String RMBCONVERTEQUITYSCALE = "rmbConvertEquityScale";
    public static final String THIRDREFERRERSCALE = "thirdReferrerScale";
    public static final String INTERVAL = "interval";
    public static final String REWARDCONVERTPAYSCALE = "rewardConvertPayScale";
    public static final String REWARDCONVERTTRADESCALE = "rewardConvertTradeScale";
    public static final String REWARDCONVERTEQUITYSCALE = "rewardConvertEquityScale";
    public static final String UPGRADEORIGINSWITCH = "upGradeOriginSwitch";
    public static final String UPGRADECOVERSWITCH = "upGradeCoverSwitch";
    public static final String BURNSPUSH = "burnsPush";
    public static final String BURNSLEVEL = "burnsLevel";
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
