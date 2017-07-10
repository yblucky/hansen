package com.mall.pay.wechatpay;

/**
 * 
 * @author zhuzh
 * @date 2016年12月15日
 */
public interface WeChatPay {

	/** 微信APPID */
	public static String APP_ID = "wx1581a2802e11162d";

	/** 斗拍微信商户ID */
	public static String MCH_ID = "1425023102";

    /** 炎宝微信商户ID */
    public static String YANBAO_MCH_ID = "1337799001";

	/** 微信APPkey */
	public static String APP_KEY = "ERGHBHF1581a2802e11162dYanbaodou";

	public static String NOTIFY_URL = "https://www.6pyun.com/api/wallet/recharge/wxCallback";

//	public static String NOTIFY_URL = "http://121.199.25.90:8091/api/wallet/recharge/wxCallback";

	public static String NOTIFY_URL_H5 = "http://m.yanbaocoin.cn/m/wallet/share/wxCallback";
	//http://120.25.120.154:8090/api/wallet/recharge/wxCallback
	//https://www.6pyun.com/api/wallet/recharge/wxCallback
	//http://192.168.2.222:8090/api/wallet/recharge/wxCallback
	//http://121.199.25.90:8091/api/wallet/recharge/wxCallback

}
