package hansen.utils;


import java.util.Vector;

/**
 * HTTP响应对象
 * @author zzwei
 */
public class HttpRespons {

    /** The url string. */
    public String urlString;

    /** The default port. */
    public int defaultPort;

    /** The file. */
    public String file;

    /** The host. */
    public String host;

    /** The path. */
    public String path;

    /** The port. */
    public int port;

    /** The protocol. */
    public String protocol;

    /** The query. */
    public String query;

    /** The ref. */
    public String ref;

    /** The user info. */
    public String userInfo;

    /** The content encoding. */
    public String contentEncoding;

    /** The content. */
    public String content;

    /** The content type. */
    public String contentType;

    /** The code. */
    public int code;

    /** The message. */
    public String message;

    /** The method. */
    public String method;

    /** The connect timeout. */
    public int connectTimeout;

    /** The read timeout. */
    public int readTimeout;

    /** The content collection. */
    public Vector<String> contentCollection;

    public String getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Vector<String> getContentCollection() {
        return contentCollection;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public String getMethod() {
        return method;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public String getUrlString() {
        return urlString;
    }

    public int getDefaultPort() {
        return defaultPort;
    }

    public String getFile() {
        return file;
    }

    public String getHost() {
        return host;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getQuery() {
        return query;
    }

    public String getRef() {
        return ref;
    }

    public String getUserInfo() {
        return userInfo;
    }

}
