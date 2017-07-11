package com.manage.pay.wechatpay;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.manage.pay.vo.QueryOrderVo;
import com.manage.util.WechatUtil;
import com.manage.pay.vo.QueryResponseInfoVo;
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
public class GenerateOrder {
	private String _appid = WeChatPay.APP_ID;// 微信APPID
	private String _MchId = WeChatPay.MCH_ID;// 微信商户ID
	private String _AppKey = WeChatPay.APP_KEY; // 微信APPkey
	private String _NotifyUrl = WeChatPay.NOTIFY_URL;//
	private String _body = "斗拍商城充值";
	private static final Logger logger = LoggerFactory.getLogger(GenerateOrder.class);

	/**
	 * 预付单生成
	 *
	 * @param money
	 * @return
	 */
	public Map<String, String> generate(String money, String ip, String attach,String outTradeNo,String notifyUrl) {
//		String outTradeNo = getOutTradeNo();
		logger.debug("**********outTradeNo*********************");

		logger.debug(outTradeNo);

		logger.debug("**********info*********************");
		String info = buildOrderInfo(money, ip, outTradeNo, attach,notifyUrl);
		logger.debug(info);
		ResponseInfo resp = post(info);
		if (resp==null) {
			throw new RuntimeException("微信支付出错");
		}
		resp.setOut_trade_no(outTradeNo);
		Map<String, String> map = buildResponseInfoOut(resp);
		logger.debug("**********buildResponseInfoOut*********************");
		logger.debug(JSON.toJSONString(map));
		return map;
	}


    /**
     * 组装订单信息
     *
     * @return
     */
    public String buildOrderInfo(String money, String ip, String outTradeNo, String attach,String notifyUrl) {
        // 生成订单对象
        RequestInfo unifiedOrderRequest = new RequestInfo();
        unifiedOrderRequest.setAttach(attach);
        unifiedOrderRequest.setAppid(_appid);// 公众账号ID
        unifiedOrderRequest.setMch_id(_MchId);// 商户号
        unifiedOrderRequest.setNonce_str(UUID.randomUUID().toString().replace("-", ""));// 随机字符串

        unifiedOrderRequest.setBody(_body);
        // 商品描述
        unifiedOrderRequest.setOut_trade_no(outTradeNo);// 商户订单号
        unifiedOrderRequest.setTotal_fee(money); // 金额需要扩大100倍:1代表支付时是0.01
        unifiedOrderRequest.setSpbill_create_ip(ip);// 终端IP
        unifiedOrderRequest.setNotify_url(notifyUrl);// 通知地址
        unifiedOrderRequest.setTrade_type("APP");// JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付
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
        unifiedOrderRequest.setSign(createSign(packageParams));// 签名
        // 将订单对象转为xml格式
        XStream xStream = new XStream(new DomDriver("utf-8"));
        xStream.alias("xml", RequestInfo.class);// 根元素名需要是xml
        return xStream.toXML(unifiedOrderRequest).replace("__", "_");
    }



    /**
	 * 组装查询订单信息
	 *
	 * @return String
	 */
	public String buildQueryOrderInfo(String outTradeNo ) {
		// 生成订单对象
		RequestInfo unifiedOrderRequest = new RequestInfo();
		QueryOrderVo orderVo = new QueryOrderVo();
		orderVo.setAppid(_appid);// 公众账号ID
		orderVo.setMch_id(_MchId);// 商户号
		orderVo.setOut_trade_no(outTradeNo);
		orderVo.setNonce_str(UUID.randomUUID().toString().replace("-", ""));// 随机字符串
		// 商品描述
		unifiedOrderRequest.setOut_trade_no(outTradeNo);// 商户订单号
		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", _appid);
		packageParams.put("mch_id", _MchId);
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
		logger.error("查询微信支付结果："+sb.toString());
		xStream.alias("xml", QueryResponseInfoVo.class);
		QueryResponseInfoVo  responseInfoVo = (QueryResponseInfoVo) xStream.fromXML(sb.toString());
		return responseInfoVo;
	}

	/**
	 * 提交
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
	 * 微信公众号支付回调生成签名
	 * @return
	 */
	public String createH5SignWithObjectKV(SortedMap<Object, Object> packageParams) {
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
//		sb.append("key=" + _AppKey);
		sb.append("key=" + WechatUtil.YANBAO_KEY);
		String sign = DigestUtils.md5Hex(sb.toString()).toUpperCase();// MD5加密
		return sign;
	}



	/**
	 * 微信app支付回调校验生成签名
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
//		sb.append("key=" + _AppKey);
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
	public static void main(String[] args) {

		Double A=0.01d;
		String string=A*100+"";
		System.out.println(string);
		System.out.println(getOutTradeNo());


		SortedMap map = new TreeMap<String,String>();
		map.put("appid","wx1581a2802e11162d");
		map.put("mch_id","1425023102");
		map.put("nonce_str","3adad1ee55f149efbde23b88ec831a99");
		map.put("out_trade_no","20170401193127001037");
		GenerateOrder generateOrder = new GenerateOrder();
		String sign12=generateOrder.createSign(map);
		System.out.println("1111 "+sign12);
		String sign="F76F78E2F4271E4409F569C865587377";


	}
}
