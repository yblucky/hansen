package com.utils.sms;

import com.taobao.api.ApiException;
import com.utils.httputils.HttpUtil;
import net.sf.json.JSONObject;

/**
 * 阿凡达短信接口
 */
public class SmsUtil {

    private static final String APPID = "0c6b5e648c8d44778805121f531aca6e";

    private static final String SMS_URL = "http://v1.avatardata.cn/Sms/Send?key=APPID&mobile=PHONE&templateId=TEMPLATEID&param=PARAM";


    /**
     * 发送短信验证码，手机号码多个用“,”隔开.
     */
    public static Boolean sendSmsCode(String phone, SmsTemplate smsTemplate, String param) throws ApiException {
        String url = SMS_URL.replace("APPID", APPID).replace("TEMPLATEID", smsTemplate.getCode()).replace("PHONE", phone).replace("PARAM", param);
       JSONObject  resObj = HttpUtil.doGetRequest(url);
        if (resObj!=null){
            if (resObj.containsKey("success") && resObj.getBoolean("success")){
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws ApiException {
        sendSmsCode("18826214582", SmsTemplate.COMMON, "123456");
    }

}