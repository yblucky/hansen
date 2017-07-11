package com.api.util;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.api.pay.h5.GenerateH5Order;
import com.api.pay.h5.ResponseInfo;
import com.api.pay.wechatpay.GenerateOrder;
import com.api.pay.wechatpay.WeChatPay;
import com.api.pay.vo.QueryResponseInfoVo;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zzwei on 2017/4/21 0021.
 */
public class WechatUtil {

    /**
     * 炎宝公众号
     * */
    public static final String YANBAO_APPID = "wx32266be91b9e0a8f";
    public static final String YANBAO_KEY = "4KEFf84f7d7HJ35689a6535d5de3a9FC";

    public static final String YANBAO_SECRET = "d9b7e01273e3cf705e4c4bc66bd27514";

    public static String _appid = WeChatPay.APP_ID;// 微信APPID
    public static String _MchId = WeChatPay.MCH_ID;// 微信商户ID(斗拍)
    public static String _YANBAO_MCH_ID = WeChatPay.YANBAO_MCH_ID;// 微信商户ID（炎宝）
    public static String _AppKey = WeChatPay.APP_KEY; // 微信APPkey
    public static String _NOTIFY_URL_H5 = WeChatPay.NOTIFY_URL_H5;//H5微信支付回调地址
    public static String BODY = "斗拍商城支付";
    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);
    /**
     * 统一下单
     *
     * @param orderInfo
     * @return
     */
    public ResponseInfo post(String orderInfo) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
            StringBuffer sb=getRequestXml(url,orderInfo);
            XStream xStream = new XStream(new DomDriver("UTF-8"));// 说明3(见文末)
            // 将请求返回的内容通过xStream转换为UnifiedOrderRespose对象
            System.out.println(sb.toString());
            xStream.alias("xml", ResponseInfo.class);
            ResponseInfo unifiedOrderRespose = (ResponseInfo) xStream.fromXML(sb.toString());
            // 根据微信文档return_code 和result_code都为SUCCESS的时候才会返回code_url
            if (null != unifiedOrderRespose && "SUCCESS".equals(unifiedOrderRespose.getReturn_code()) && "SUCCESS".equals(unifiedOrderRespose.getResult_code())) {
                logger.error("****************xml********************");
                logger.error(JSON.toJSONString(unifiedOrderRespose));
                logger.error("*************************************");
                return unifiedOrderRespose;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public StringBuffer getRequestXml(String url,String orderInfo) throws IOException {
        System.out.println(orderInfo);
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        // 加入数据
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Charset", "UTF-8");
        BufferedOutputStream buffOutStr = new BufferedOutputStream(conn.getOutputStream());
        buffOutStr.write(orderInfo.toString().getBytes("UTF-8"));
        buffOutStr.flush();
        buffOutStr.close();
        // 获取输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = null;
        StringBuffer sb = new StringBuffer();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return  sb;
    }



    /**
     * 提交
     *
     * @param resp
     * @return
     */
    public Map<String, String> buildResponseInfoOut(ResponseInfo resp) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        parameters.put("appid", resp.getAppid());
        parameters.put("partnerid", resp.getMch_id());
        parameters.put("prepayid", resp.getPrepay_id());
        parameters.put("noncestr", resp.getNonce_str());
        parameters.put("timestamp", ts);
        parameters.put("package", "Sign=WXPay");
        String _sign = createSign(parameters); // 这个需要后台返回
        parameters.put("sign", _sign);
        return parameters;
    }

    /**
     * 生成签名
     * @return
     */
    public String createSign(SortedMap<String, String> packageParams) {
        // 根据规则创建可排序的map集合
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();// 字典序
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            // 为空不参与签名、参数名区分大小写
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        // 第二步拼接key，key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
        sb.append("key=" + _AppKey);
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();// MD5加密
        return sign;
    }


    /**
     * 生成签名
     * @return
     */
    public String createSignWithObjectKV(SortedMap<Object, Object> packageParams) {
        // 根据规则创建可排序的map集合
        StringBuffer sb = new StringBuffer();
        Set es = packageParams.entrySet();// 字典序
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            // 为空不参与签名、参数名区分大小写
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        // 第二步拼接key，key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
        sb.append("key=" + _AppKey);
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();// MD5加密
        return sign;
    }


    /*
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    private static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.replace("-", "").substring(0, 19);
        return key;
    }

    /**微信app支付对账**/
    public static Boolean  isAppPaySucess(String orderNo) throws Exception {
        GenerateOrder generateOrder = new GenerateOrder();
        try {
            StringBuffer sb= generateOrder.getRequestXml("https://api.mch.weixin.qq.com/pay/orderquery",generateOrder.buildQueryOrderInfo(orderNo));
            QueryResponseInfoVo responseInfo= generateOrder.getQueryOrderResponseInfo(sb);
            if (responseInfo==null){
                return false;
            }
            if (responseInfo.getTrade_state().equals("SUCCESS")  && "SUCCESS".equals(responseInfo.getResult_code())){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**微信公众号支付对账H5**/
    public static Boolean  isH5PaySucess(String orderNo) throws Exception {
        GenerateH5Order generateH5Order = new GenerateH5Order();
        try {
            StringBuffer sb= generateH5Order.getRequestXml("https://api.mch.weixin.qq.com/pay/orderquery",generateH5Order.buildQueryOrderInfo(orderNo));
            QueryResponseInfoVo responseInfo= generateH5Order.getQueryOrderResponseInfo(sb);
            if (responseInfo==null){
                return false;
            }
            if (responseInfo.getTrade_state().equals("SUCCESS")  && "SUCCESS".equals(responseInfo.getResult_code())){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return  false;
        }
        return false;
    }


    /**支付宝充值支付对账**/
    /**支付宝app支付对账**/
    public static Boolean  isAppScanAliPaySucess(String tradeNo)  {
        try {
            String url ="http://120.24.234.115:8090/pay/payAlipaySDK/queryAliPayState?tradeNo="+tradeNo;
            JSONObject jsonObject = HttpUtil.doGetRequest(url);
            System.out.println(jsonObject);
            if (jsonObject==null){
                return false;
            }
            if (jsonObject.get("code").equals("0000")){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static void main(String[] args) {

        try {
            System.out.println(isAppPaySucess("201704271116520350121"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
