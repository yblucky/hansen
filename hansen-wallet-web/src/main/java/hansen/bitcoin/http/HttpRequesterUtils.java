package hansen.bitcoin.http;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Vector;


/**
 * 请求外部链接获取数据
 * @author  zzwei
 */
public class HttpRequesterUtils {

    /** The default content encoding. */
    private String defaultContentEncoding;
    private static final String ENCODING = "ISO-8859-1";
    
    private HttpClient httpClient;

    /**
     * Instantiates a new http requester utils.
     */
    public HttpRequesterUtils() {
        this.defaultContentEncoding = Charset.defaultCharset().name();
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public HttpRespons sendGet(String urlString) throws IOException {
        return this.send(urlString, "GET", null, null);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public HttpRespons sendGet(String urlString, Map<String, String> params)
            throws IOException {
        return this.send(urlString, "GET", params, null);
    }

    /**
     * 发送GET请求
     * @param urlString URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public HttpRespons sendGet(String urlString, Map<String, String> params,
            Map<String, String> propertys) throws IOException {
        return this.send(urlString, "GET", params, propertys);
    }

    /**
     * 发送POST请求
     * @param urlString URL地址
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public HttpRespons sendPost(String urlString) throws IOException {
        return this.send(urlString, "POST", null, null);
    }

    /**
     * 发送POST请求
     * @param urlString URL地址
     * @param params 参数集合
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public HttpRespons sendPost(String urlString, Map<String, String> params)
            throws IOException {
        return this.send(urlString, "POST", params, null);
    }

    /**
     * 发送POST请求
     * @param urlString URL地址
     * @param params 参数集合
     * @param propertys 请求属性
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public HttpRespons sendPost(String urlString, Map<String, String> params,
            Map<String, String> propertys) throws IOException {
        return this.send(urlString, "POST", params, propertys);
    }

    /**
     * 发送HTTP请求
     * @param urlString the url string
     * @param method the method
     * @param parameters the parameters
     * @param propertys the propertys
     * @return 响映对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private HttpRespons send(String urlString, String method,
            Map<String, String> parameters, Map<String, String> propertys)
            throws IOException {
        HttpURLConnection urlConnection = null;

        if (method.equalsIgnoreCase("GET") && parameters != null) {
            StringBuffer param = new StringBuffer();
            int i = 0;
            for (String key : parameters.keySet()) {
                if (i == 0)
                    param.append("?");
                else
                    param.append("&");
                param.append(key).append("=").append(parameters.get(key));
                i++;
            }
            urlString += param;
        }
        URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod(method);
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setUseCaches(false);

        if (propertys != null)
            for (String key : propertys.keySet()) {
                urlConnection.addRequestProperty(key, propertys.get(key));
            }

        if (method.equalsIgnoreCase("POST") && parameters != null) {
            StringBuffer param = new StringBuffer();
            for (String key : parameters.keySet()) {
                param.append("&");
                param.append(key).append("=").append(parameters.get(key));
            }
            urlConnection.getOutputStream().write(param.toString().getBytes());
            urlConnection.getOutputStream().flush();
            urlConnection.getOutputStream().close();
        }

        return this.makeContent(urlString, urlConnection);
    }

    /**
     * 得到响应对象
     * @param urlString the url string
     * @param urlConnection the url connection
     * @return 响应对象
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private HttpRespons makeContent(String urlString,
            HttpURLConnection urlConnection) throws IOException {
        HttpRespons httpResponser = new HttpRespons();
        try {
            InputStream in = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(in));
            httpResponser.contentCollection = new Vector<String>();
            StringBuffer temp = new StringBuffer();
            String line = bufferedReader.readLine();
            while (line != null) {
                httpResponser.contentCollection.add(line);
                temp.append(line).append("\r\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

            String ecod = urlConnection.getContentEncoding();
            if (ecod == null)
                ecod = this.defaultContentEncoding;

            httpResponser.urlString = urlString;

            httpResponser.defaultPort = urlConnection.getURL().getDefaultPort();
            httpResponser.file = urlConnection.getURL().getFile();
            httpResponser.host = urlConnection.getURL().getHost();
            httpResponser.path = urlConnection.getURL().getPath();
            httpResponser.port = urlConnection.getURL().getPort();
            httpResponser.protocol = urlConnection.getURL().getProtocol();
            httpResponser.query = urlConnection.getURL().getQuery();
            httpResponser.ref = urlConnection.getURL().getRef();
            httpResponser.userInfo = urlConnection.getURL().getUserInfo();

            httpResponser.content = new String(temp.toString().getBytes(), ecod);
            httpResponser.contentEncoding = ecod;
            httpResponser.code = urlConnection.getResponseCode();
            httpResponser.message = urlConnection.getResponseMessage();
            httpResponser.contentType = urlConnection.getContentType();
            httpResponser.method = urlConnection.getRequestMethod();
            httpResponser.connectTimeout = urlConnection.getConnectTimeout();
            httpResponser.readTimeout = urlConnection.getReadTimeout();
            return httpResponser;
        } catch (IOException e) {
            throw e;
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
        }
    }

    /**
     * 默认的响应字符集
     */
    public String getDefaultContentEncoding() {
        return this.defaultContentEncoding;
    }

    /**
     * 设置默认的响应字符集
     */
    public void setDefaultContentEncoding(String defaultContentEncoding) {
        this.defaultContentEncoding = defaultContentEncoding;
    }
    
    private HttpClient getHttpClient() {
        if (httpClient == null) {
        	httpClient = new HttpClient();
        	httpClient.getParams().setContentCharset(ENCODING);
        	httpClient.getParams().setAuthenticationPreemptive(true);
        	httpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials());
        	httpClient.setParams(new HttpClientParams());
//            client.getState().setCredentials(new AuthScope(ResourceUtil.getRpcallowip("br"), Integer.valueOf(ResourceUtil.getRpcport("br")), AuthScope.ANY_REALM), credentials);
        }
        return httpClient;
    }
    
    public static void main(String[] args) {
		HttpRequesterUtils requesterUtils = new HttpRequesterUtils();
		try {
			System.out.println(requesterUtils.sendGet("http://www.haoyun2008.com/app/fortune105.apk").getContent());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
