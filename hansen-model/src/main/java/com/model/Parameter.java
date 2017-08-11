package com.model;


import com.common.BaseModel;

/**
 * @author jay.zheng
 * @date 2017/6/28
 */
public class Parameter extends BaseModel {

    public static final long serialVersionUID = -76937973371276572L;

    public static final String equityRpcPassword = "equityRpcPassword";
    public static final String equityDaemon = "equityDaemon";
    public static final String equityRpcuser = "equityRpcuser";
    public static final String equityServer = "equityServer";
    public static final String equityListen = "equityListen";
    public static final String equityAddnode = "equityAddnode";
    public static final String jumpRewardLimit = "jumpRewardLimit";
    public static final String payRpcallowip = "payRpcallowip";
    public static final String payRpcuser = "payRpcuser";
    public static final String payServer = "payServer";
    public static final String payListen = "payListen";
    public static final String payAddnode = "payAddnode";
    public static final String payRpcpassword = "payRpcpassword";
    public static final String tradeAddnode = "tradeAddnode";
    public static final String tradeDaemon = "V";
    public static final String PayDaemon = "PayDaemon";
    public static final String tradeRpcallowip = "tradeRpcallowip";
    public static final String tradeRpcuser = "tradeRpcuser";
    public static final String tradeServer = "tradeServer";
    public static final String tradeListen = "tradeListen";
    public static final String tradeRpcpassword = "tradeRpcpassword";
    public static final String activeCodePrice = "activeCodePrice";
    public static final String registerCodePrice = "registerCodePrice";
    public static final String insurancePayScale = "insurancePayScale";
    public static final String insuranceTradeScale = "insuranceTradeScale";
    public static final String pushFirstReferrerScale = "pushFirstReferrerScale";
    public static final String pushSecondReferrerScale = "pushSecondReferrerScale";
    public static final String manageFirstReferrerScale = "manageFirstReferrerScale";
    public static final String manageSecondReferrerScale = "manageSecondReferrerScale";
    public static final String maxGradeScale = "maxGradeScale";
    public static final String rmbConvertPayScale = "rmbConvertPayScale";
    public static final String rmbConvertTradeScale = "rmbConvertTradeScale";
    public static final String rmbConvertEquityScale = "rmbConvertEquityScale";
    public static final String thirdReferrerScale = "thirdReferrerScale";
    public static final String interval = "interval";
    public static final String rewardConvertPayScale = "rewardConvertPayScale";
    public static final String rewardConvertTradeScale = "rewardConvertTradeScale";
    public static final String rewardConvertEquityScale = "rewardConvertEquityScale";
    public static final String upGradeOriginSwitch = "upGradeOriginSwitch";
    public static final String upGradeCoverSwitch = "upGradeCoverSwitch";
    public static final String burnsPush = "burnsPush";
    public static final String burnsLevel = "burnsLevel";
    public static final String burnsManage = "burnsManage";


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
