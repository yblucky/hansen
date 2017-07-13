/**
 * Copyright 2010 Aleksey Krivosheev (paradoxs.mail@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package hansen.utils;

import org.apache.commons.httpclient.Credentials;
import ru.paradoxs.bitcoin.http.HttpSession;

import java.net.URI;

/**
 * Manages the HTTP machinery for accessing the Bitcoin server.
 * 
 * PLEASE NOTE that it doesn't do https, only http!
 */
public class HttpSessionFactory {

	public static HttpSession httpSession = null;
	public static HttpSession getHttpSession(URI uri,Credentials credentials) {
		if (httpSession == null) {
			httpSession = new HttpSession(uri, credentials);
		}
		return httpSession;
	}
}
