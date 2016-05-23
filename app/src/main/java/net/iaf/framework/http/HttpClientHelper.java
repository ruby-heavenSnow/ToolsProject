/**
 * @Description: TODO
 * @author: {Zhou Haitao.  }
 * @version: 1.0
 * @see 
 */

package net.iaf.framework.http;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpVersion;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

/**
 * HttpClient工具方法
 * @author Bob
 *
 */
public class HttpClientHelper {

	/**
	 * 创建HttpClient
	 */
	public static DefaultHttpClient buildHttpClient(int connectionPoolTimeout,
			int connectionTimeout, int socketTimeout) {
		return buildHttpsClient(connectionPoolTimeout, connectionTimeout,
				socketTimeout);
	}

	/**
	 * 创建HttpsClient
	 */
	public static DefaultHttpClient buildHttpsClient(int connectionPoolTimeout,
			int connectionTimeout, int socketTimeout) {
		DefaultHttpClient httpsClient;
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
			// 设置是否可以重定向
			HttpClientParams.setRedirecting(params, true);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));
			ClientConnectionManager mgr = new ThreadSafeClientConnManager(
					params, registry);
			httpsClient = new DefaultHttpClient(mgr, params);
		} catch (Exception e) {
			httpsClient = new DefaultHttpClient();
		}
		ConnManagerParams.setTimeout(httpsClient.getParams(),
				connectionPoolTimeout);
		HttpConnectionParams.setConnectionTimeout(httpsClient.getParams(),
				connectionTimeout);
		HttpConnectionParams.setSoTimeout(httpsClient.getParams(),
				socketTimeout);
		return httpsClient;
	}

	/**
	 * 关闭HttpClient连接
	 * 
	 * @Title: closeHttpClient
	 * @param httpClient
	 */
	public static void closeClient(DefaultHttpClient httpClient) {
		if (httpClient != null && httpClient.getConnectionManager() != null) {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
	}
	
	/**
	 * SSLSocketFactory覆写类 
	 * @author Bob
	 *
	 */
	static class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);
			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);

		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

}
