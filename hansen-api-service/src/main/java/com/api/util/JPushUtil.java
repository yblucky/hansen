package com.api.util;

import cn.jiguang.common.ClientConfig;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;
import org.apache.commons.lang3.StringUtils;

/**
 * 极光推送
 * @author zcj
 * @date 2017年2月27日
 */
public class JPushUtil {
    /*暂时写死，后期有重构再调整*/
    private static String  MASTER_SECRET= "a143399be3c99ee3e7afbd77";
    private static String APP_KEY= "87a89551ec616197a8c5d8ff";
    /**
     * 根据每个用户的极光推送Id进行精准推送
     * @param registrationId 机关推送Id
     * @param content 推送的消息内容
     * */
    public static boolean pushPayloadByid(String registrationId,String content)  {
        if(StringUtils.isBlank(registrationId)){
            return false;
        }
        PushResult result = null;
        try {
            JPushClient jpushClient = new JPushClient(MASTER_SECRET, APP_KEY, null, ClientConfig.getInstance());
            PushPayload payload  =PushPayload.newBuilder()
                    .setPlatform(Platform.all())
                    .setAudience(Audience.registrationId(registrationId))
                    .setNotification(Notification.alert(content))
                    .setOptions(Options.newBuilder().setApnsProduction(true).build())
                    .build();

            result = jpushClient.sendPush(payload);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.isResultOK();
    }
    
    
    public static void main(String[] args) {
    	try {
			JPushUtil.pushPayloadByid("13065ffa4e3a1241c56", "擦后的活雷锋撒哈拉发货撒快递费哈萨克的后方可士大夫很快就撒的谎到返回");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
