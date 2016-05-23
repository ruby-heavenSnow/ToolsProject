/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see 
 */

package net.iaf.framework.http;

import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.Loger;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * HttpPost工具方法
 * @author Bob
 *
 */
public class HttpPostUtil extends BaseHttp {

	/**
	 * 传入uri,参数集合，构造HttpUriRequest的方法
	 * @param uri
	 * @param hmParams
	 * @return
	 */
	private HttpUriRequest buildHttpUriRequest(String uri,
			HashMap<String, String> hmParams) {
        uri += "/Iris/b7dfe9134c7a717a3b6eaf37bdc1ef7b";
        Loger.i("url:" + uri);
        HttpPost post = new HttpPost(uri);
        //NameValuePair v = new BasicNameValuePair();
        //List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        //nameValuePair.add(new BasicNameValuePair("username", "Iris"));
        //nameValuePair.add(new BasicNameValuePair("pwd", "B7DFE9134C7A717A3B6EAF37BDC1EF7B"));
		//Iterator<Entry<String, String>> iter = hmParams.entrySet().iterator();
		//while (iter.hasNext()) {
		//	Entry<String, String> entry = iter.next();
		//	nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry
		//			.getValue()));
		//}
		//try {
		//	post.setEntity(new UrlEncodedFormEntity(nameValuePair, ENCODING));
		//} catch (UnsupportedEncodingException e) {
		//	e.printStackTrace();
		//	throw new IllegalArgumentException(e);
		//}
		return post;
	}

	/**
	 * url,json构造HttpUriRequest的方法
	 * @param url
	 * @param json
	 * @return
	 */
	private HttpUriRequest buildHttpUriRequest(String url, String json) {
		HttpPost post = new HttpPost(url);
		post.addHeader("Accept", "application/json");
		post.addHeader("Content-Type", "application/json;charset=utf-8");

		try {
			post.setEntity(new StringEntity(json, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(e);
		}
		return post;
	}

	/**
	 * 执行Request，HttpPost中参数默认编码方式为BaseHttp中的ENCODING
	 */
	public HttpResult executeRequest(String url,
			HashMap<String, String> hmParams) throws TimeoutException,
			NetworkException {
		return sendRequest(buildHttpUriRequest(url, hmParams));
	}

	/**
	 * 发起请求的方法
	 * @param url
	 * @param json
	 * @return
	 * @throws TimeoutException
	 * @throws NetworkException
	 */
	public HttpResult executeRequest(String url, String json)
			throws TimeoutException, NetworkException {
		return sendRequest(buildHttpUriRequest(url, json));
	}

	/**
	 * 发起请求的方法
	 * @param post
	 * @return
	 * @throws TimeoutException
	 * @throws NetworkException
	 */
	public HttpResult executeRequest(HttpPost post) throws TimeoutException,
			NetworkException {
		return sendRequest(post);
	}

	/**
	 * 构造DefaultHttpClient的方法
	 */
	@Override
	protected DefaultHttpClient buildClient(int connectionPoolTimeout,
			int connectionTimeout, int socketTimeout) {
		return HttpClientHelper.buildHttpClient(connectionPoolTimeout,
				connectionTimeout, socketTimeout);
	}

}
