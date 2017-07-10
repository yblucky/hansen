package com.mall.pay.mini;

import com.alibaba.fastjson.JSON;
import com.mall.pay.h5.RequestInfo;
import com.mall.pay.vo.QueryOrderVo;
import com.mall.pay.vo.QueryResponseInfoVo;
import com.mall.util.WechatUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
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
 * 微信支付
 */
public class GenerateMiniOrder {

    private static final Logger logger = LoggerFactory.getLogger(GenerateMiniOrder.class);
    public static String APP_ID = "wxc129df9d8e6ff47f";// 小程序APPID
    public static String MCH_ID = "1471793102";// 微信商户ID(小程序)
    public static String KEY = "BIlntxgR8di4Fbs6sUyUHBsxrNbj2Y6F";// 秘钥key(小程序)
    public static String NOTIFY_URL_H5 = "https://119.29.216.138:8090";//小程序微信支付回调地址
    public static String BODY = "英树美妆";

    /**
     * 预付单生成
     *
     * @param money
     * @return
     */
    public Map<String, String> generate(String money, String ip, String attach, String outTradeNo, String openid, String notyfyUrl) {
        logger.debug("**********outTradeNo*********************");
        logger.debug(outTradeNo);
        logger.debug("**********money*********************");
        logger.debug(money);
        System.out.println("******100000 money ********** " + money);
        com.mall.pay.h5.RequestInfo requestInfo = buildOrderRequestInfo(money, ip, outTradeNo, attach, openid, notyfyUrl);
        String info = buildOrderRequestInfoXml(requestInfo);
        logger.debug(info);
        com.mall.pay.h5.ResponseInfo resp = post(info);
        if (resp == null) {
            throw new RuntimeException("微信支付出错");
        }
        resp.setOut_trade_no(outTradeNo);
        Map<String, String> map = buildResponseInfoOut(resp);
        logger.debug("**********buildResponseInfoOut*********************");
        logger.debug(JSON.toJSONString(map));
        return map;
    }

    /**
     * 组装统一下单对象
     *
     * @return
     */
    public com.mall.pay.h5.RequestInfo buildOrderRequestInfo(String money, String ip, String outTradeNo, String attach, String openid, String notyfyUrl) {
        // 生成订单对象
        com.mall.pay.h5.RequestInfo unifiedOrderRequest = new com.mall.pay.h5.RequestInfo();
        unifiedOrderRequest.setAttach(attach);
        unifiedOrderRequest.setAppid(APP_ID);// 小程序APPID
        unifiedOrderRequest.setMch_id(MCH_ID);// 商户号
        unifiedOrderRequest.setNonce_str(UUID.randomUUID().toString().replace("-", ""));// 随机字符串
        //公众号H5支付增加参数openid
        unifiedOrderRequest.setOpenid(openid);
        unifiedOrderRequest.setBody(BODY);
        // 商品描述
        unifiedOrderRequest.setOut_trade_no(outTradeNo);// 商户订单号
        unifiedOrderRequest.setTotal_fee(money); // 金额需要扩大100倍:1代表支付时是0.01
        unifiedOrderRequest.setSpbill_create_ip(ip);// 终端IP
        unifiedOrderRequest.setNotify_url(notyfyUrl);// 通知地址
        unifiedOrderRequest.setTrade_type("JSAPI");// JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", unifiedOrderRequest.getAppid());
        packageParams.put("body", unifiedOrderRequest.getBody());
        packageParams.put("mch_id", unifiedOrderRequest.getMch_id());
        packageParams.put("nonce_str", unifiedOrderRequest.getNonce_str());
        packageParams.put("notify_url", unifiedOrderRequest.getNotify_url());
        packageParams.put("out_trade_no", unifiedOrderRequest.getOut_trade_no());
        packageParams.put("attach", unifiedOrderRequest.getAttach());
        packageParams.put("spbill_create_ip", unifiedOrderRequest.getSpbill_create_ip());
        packageParams.put("trade_type", unifiedOrderRequest.getTrade_type());
        packageParams.put("total_fee", unifiedOrderRequest.getTotal_fee());
        packageParams.put("openid", unifiedOrderRequest.getOpenid());
        unifiedOrderRequest.setSign(createSign(packageParams));// 签名
        return unifiedOrderRequest;
    }

    /**
     * 组装统一下单xml
     *
     * @return
     */
    public String buildOrderRequestInfoXml(com.mall.pay.h5.RequestInfo requestInfo) {
        // 将订单对象转为xml格式
        XStream xStream = new XStream(new DomDriver("utf-8"));
        xStream.alias("xml", com.mall.pay.h5.RequestInfo.class);// 根元素名需要是xml
        return xStream.toXML(requestInfo).replace("__", "_");
    }

    /**
     * 组装查询订单信息
     *
     * @return String
     */
    public String buildQueryOrderInfo(String outTradeNo) {
        // 生成订单对象
        com.mall.pay.h5.RequestInfo unifiedOrderRequest = new com.mall.pay.h5.RequestInfo();
        QueryOrderVo orderVo = new QueryOrderVo();
        orderVo.setAppid(WechatUtil.YANBAO_APPID);// 公众账号ID
        orderVo.setMch_id(WechatUtil._YANBAO_MCH_ID);// 商户号
        orderVo.setOut_trade_no(outTradeNo);
        orderVo.setNonce_str(UUID.randomUUID().toString().replace("-", ""));// 随机字符串
        // 商品描述
        unifiedOrderRequest.setOut_trade_no(outTradeNo);// 商户订单号
        SortedMap<String, String> packageParams = new TreeMap<String, String>();
        packageParams.put("appid", WechatUtil.YANBAO_APPID);
        packageParams.put("mch_id", WechatUtil._YANBAO_MCH_ID);
        packageParams.put("nonce_str", orderVo.getNonce_str());
        packageParams.put("out_trade_no", orderVo.getOut_trade_no());
        orderVo.setSign(createSign(packageParams));// 签名
        // 将订单对象转为xml格式
        XStream xStream = new XStream(new DomDriver("utf-8"));
        xStream.alias("xml", QueryOrderVo.class);// 根元素名需要是xml
        return xStream.toXML(orderVo).replace("__", "_");
    }

    /**
     * 解析查询订单结果
     *
     * @return String
     */
    public static QueryResponseInfoVo getQueryOrderResponseInfo(StringBuffer sb) {
        XStream xStream = new XStream(new DomDriver("UTF-8"));// 说明3(见文末)
        // 将请求返回的内容通过xStream转换为QueryResponseInfoVo对象
        logger.error("查询微信支付结果：" + sb.toString());
        xStream.alias("xml", QueryResponseInfoVo.class);
        QueryResponseInfoVo responseInfoVo = (QueryResponseInfoVo) xStream.fromXML(sb.toString());
        return responseInfoVo;
    }

    /**
     * 统一下单post
     *
     * @param orderInfo
     * @return
     */
    public com.mall.pay.h5.ResponseInfo post(String orderInfo) {
        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        try {
            StringBuffer sb = getRequestXml(url, orderInfo);
            XStream xStream = new XStream(new DomDriver("UTF-8"));// 说明3(见文末)
            // 将请求返回的内容通过xStream转换为UnifiedOrderRespose对象
            System.out.println(sb.toString());
            xStream.alias("xml", com.mall.pay.h5.ResponseInfo.class);
            com.mall.pay.h5.ResponseInfo unifiedOrderRespose = (com.mall.pay.h5.ResponseInfo) xStream.fromXML(sb.toString());
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

    public StringBuffer getRequestXml(String url, String orderInfo) throws IOException {
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
        return sb;
    }


    /**
     * 提交
     *
     * @param resp
     * @return
     */
    public Map<String, String> buildResponseInfoOut(com.mall.pay.h5.ResponseInfo resp) {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        Long tsLong = System.currentTimeMillis() / 1000;
        String ts = tsLong.toString();
        parameters.put("appId", resp.getAppid());
        parameters.put("nonceStr", resp.getNonce_str());
        parameters.put("timeStamp", ts);
        parameters.put("signType", "MD5");
        parameters.put("package", "prepay_id=" + resp.getPrepay_id());
        String _sign = createSign(parameters); // 这个需要后台返回
        parameters.put("paySign", _sign);
        parameters.put("prepayid", resp.getPrepay_id());
        return parameters;
    }

    /**
     * 生成签名
     *
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
        sb.append("key=" + KEY);
        String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();// MD5加密
        return sign;
    }


    /**
     * 生成签名
     *
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
        sb.append("key=" + WechatUtil._AppKey);
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

    public static void main(String[] args) {

        Double A = 0.01d;
        String string = A * 100 + "";
        System.out.println(string);
        System.out.println(getOutTradeNo());


        SortedMap map = new TreeMap<String, String>();
        map.put("appid", "wx1581a2802e11162d");
        map.put("mch_id", "1425023102");
        map.put("nonce_str", "3adad1ee55f149efbde23b88ec831a99");
        map.put("out_trade_no", "20170401193127001037");
        com.mall.pay.h5.RequestInfo requestInfo = new RequestInfo();
        requestInfo.setOpenid("test");
        requestInfo.setOut_trade_no("tttttt");

        GenerateMiniOrder generateOrder = new GenerateMiniOrder();
        String yopenId="ohf4L0S7acAEsWDO-spvl4tXfmLQ";
        String meId="ouH4bs_JG0XvtFfvLQZUDnWHPCl0";

        Map map1 = generateOrder.generate("5500", "113.76.115.37", "test", "test112345678", yopenId, "notyfyUrl");
        System.out.println(JSON.toJSON(map1));


    }
}
