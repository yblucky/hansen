
package hansen.bitcoin.http;

import hansen.bitcoin.http.exceptions.HttpSessionException;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONTokener;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;

import java.io.IOException;
import java.net.URI;

/**
 * Manages the HTTP machinery for accessing the Bitcoin server.
 * <p>
 * PLEASE NOTE that it doesn't do https, only http!
 */
public class HttpSession {

    // TODO: There's an encoding bug lurking down here, but I can't find it right now
    private static final String ENCODING = "ISO-8859-1";
    private static final String JSON_CONTENT_TYPE = "application/json;charset=" + ENCODING;
    private static final String POST_CONTENT_TYPE = "text/plain";

    private HttpClient client = null;
    private URI uri = null;
    private Credentials credentials = null;

    public HttpSession(URI uri, Credentials credentials) {
        this.uri = uri;
        this.credentials = credentials;
    }

    public JSONObject sendAndReceive(JSONObject message) {
        PostMethod method = new PostMethod(uri.toString());

        try {
            method.setRequestHeader("Content-Type", POST_CONTENT_TYPE);

            RequestEntity requestEntity = new StringRequestEntity(message.toString(), JSON_CONTENT_TYPE, ENCODING);
            method.setRequestEntity(requestEntity);

            getHttpClient().executeMethod(method);
            int statusCode = method.getStatusCode();
            System.out.println(statusCode);
            System.out.println(statusCode);
            System.out.println(statusCode);
            if (statusCode != HttpStatus.SC_OK) {
                throw new HttpSessionException("HTTP Status - " + HttpStatus.getStatusText(statusCode) + " (" + statusCode + ")");
            }

            JSONTokener tokener = new JSONTokener(method.getResponseBodyAsString());
            Object rawResponseMessage = tokener.nextValue();
            JSONObject response = (JSONObject) rawResponseMessage;

            if (response == null) {
                throw new HttpSessionException("Invalid response type");
            }

            return response;
        } catch (HttpException e) {
            throw new HttpSessionException(e);
        } catch (IOException e) {
            throw new HttpSessionException(e);
        } catch (JSONException e) {
            throw new HttpSessionException(e);
        } finally {
            method.releaseConnection();
        }
    }

    private HttpClient getHttpClient() {
        if (client == null) {
            client = new HttpClient();
            client.getParams().setContentCharset(ENCODING);
            client.getParams().setAuthenticationPreemptive(true);
            client.getState().setCredentials(AuthScope.ANY, credentials);
            client.setParams(new HttpClientParams());

//            client.getState().setCredentials(new AuthScope(ResourceUtil.getRpcallowip("br"), Integer.valueOf(ResourceUtil.getRpcport("br")), AuthScope.ANY_REALM), credentials);
        }

        return client;
    }
}
