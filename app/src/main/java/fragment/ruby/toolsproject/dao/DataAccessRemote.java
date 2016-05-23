/**
 *
 */
package fragment.ruby.toolsproject.dao;

import android.text.TextUtils;

import net.iaf.framework.Config;
import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.NoNetworkException;
import net.iaf.framework.exception.ServerException;
import net.iaf.framework.exception.TimeoutException;
import net.iaf.framework.util.Loger;
import net.iaf.framework.util.PhoneStateUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import fragment.ruby.toolsproject.entity.BaseJsonEntity;
import fragment.ruby.toolsproject.entity.Pair;

/**
 * @param <T>
 * @author zhaohaitao
 */
public class DataAccessRemote<T extends BaseJsonEntity<T>> extends DataAccess<T> {

  private int timeout;

  /**
   * Constructor for DataAccessRemote
   *
   * @param
   */
  public DataAccessRemote(BaseJsonEntity<T> jsonEntity) {
    super(jsonEntity);
    this.timeout = Config.TIMEOUT;
  }

  public DataAccessRemote(BaseJsonEntity<T> jsonEntity, int timeout) {
    super(jsonEntity);
    this.timeout = timeout;
  }

  @Override
  public T post(List<Pair> params, boolean cacheInvalidate)
      throws TimeoutException, NetworkException, DBException {
    buildUrl(params);
    String jsonStr = httpsPostJsonStrRequest(this.urlWhole, params);
    T data = this.jsonEntity.parseJson2Entity(jsonStr);
    return data;
  }

  @Override
  public T get(List<Pair> params, boolean cacheInvalidate)
      throws TimeoutException, NetworkException, DBException {
    buildUrl(params);
    String jsonStr = httpsGetJsonStrRequest(this.urlWhole, params);
    T data = this.jsonEntity.parseJson2Entity(jsonStr);
    return data;
  }

  // -------------   https Post request  ----------------
  protected String httpsPostJsonStrRequest(String strURL, final List<Pair> params)
      throws TimeoutException, NetworkException {
    Loger.d("strURL:" + strURL);

    if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
      throw new NoNetworkException();
    }

    StringBuilder json = new StringBuilder();
    HttpsURLConnection conn = null;

    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());

      URL url = new URL(strURL);
      BufferedReader br = null;
      conn = (HttpsURLConnection) url.openConnection();
      conn.setConnectTimeout(10000);
      conn.setReadTimeout(30000);
      conn.setDoOutput(true);
//      conn.setDoInput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Charset", "UTF-8");
      conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
      conn.setRequestProperty("Content-Type", "application/json");

      //add header
      JSONObject paramJson = new JSONObject();
      if (params != null && !params.isEmpty()) {
        for (Pair pair : params) {
          if (pair.value instanceof ArrayList) {
            ArrayList list = (ArrayList) (pair.value);
            JSONArray jsonItems = new JSONArray();
            Iterator<String> listIterator = list.iterator();
            while (listIterator.hasNext()) {
              jsonItems.put(listIterator.next().toString());
            }
            paramJson.put(pair.key, jsonItems);
          } else {
            paramJson.put(pair.key, pair.value);
          }
        }
      }
      Loger.i("url[post] send params--" + paramJson.toString());

      conn.connect();

      DataOutputStream out = new DataOutputStream(
          conn.getOutputStream());
      out.write((paramJson.toString()).getBytes("UTF-8"));
      out.flush();
      out.close();

      Loger.i("url[post] responseCode--" + conn.getResponseCode());
      InputStream is = conn.getInputStream();
      BufferedInputStream bis = new BufferedInputStream(is);

      boolean isGzip;

      // 判别gzip方法一
      // isGzip = "gzip".equals(conn.getContentEncoding());

      // 判别gzip方法二
      bis.mark(2);// 取前两个字节
      byte[] header = new byte[2];
      int result = bis.read(header);
      bis.reset();// reset输入流到开始位置
      // isGzip = (result!=-1 && getShort(header)==0x8b1f);
      isGzip = (result != -1 && getShort(header) == 0x1f8b);

      // 如果是gzip的压缩流 进行解压缩处理
      if (isGzip) {
        is = new GZIPInputStream(bis);
      } else {
        is = bis;
      }
      br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
      String temp;
      while ((temp = br.readLine()) != null) {
        json = json.append(temp);
      }
      br.close();
      is.close();
      //outs.close();
    } catch (SocketTimeoutException e) {
      throw new TimeoutException();
    } catch (IOException e) {
      throw new NetworkException();
    } catch (JSONException e) {
      e.printStackTrace();
    }catch (NoSuchAlgorithmException e) {
      throw new ServerException();
    }catch (KeyManagementException e) {
      throw new ServerException();
    } finally {
      if (conn != null)
        conn.disconnect();
    }

    String jsonStr = json.toString().replace("\\u000d", "");

    Loger.i("result jsonStr:" + jsonStr);

    return jsonStr;
  }

  // -------------   https Get request  ----------------
  protected String httpsGetJsonStrRequest(String strURL, final List<Pair> params)
      throws TimeoutException, NetworkException {
    Loger.d("strURL:" + strURL);
    if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
      throw new NoNetworkException();
    }

    String result = "";
    HttpsURLConnection conn = null;

    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());

      URL url = new URL(strURL);
      BufferedReader br = null;
      conn = (HttpsURLConnection) url.openConnection();
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(5000);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Charset", "UTF-8");
      conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
      conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
      conn.setRequestProperty("Content-Type", "application/json");

      //add header
      JSONObject paramJson = new JSONObject();
      if (params != null && !params.isEmpty()) {
//				for (Entry<String, String> header : headers.entrySet()){
//					conn.setRequestProperty(header.getKey(), header.getValue());
//				}
        for (Pair pair : params) {
          paramJson.put(pair.key, pair.value);
        }
      }
      Loger.i("url[get] send params--" + paramJson.toString());
      DataOutputStream out = new DataOutputStream(
          conn.getOutputStream());
      out.write((paramJson.toString()).getBytes("utf-8"));
      out.flush();
      out.close();
      // 获取响应的状态码 404 200 505 302
      Loger.i("url[get] responseCode--" + conn.getResponseCode());
      if (conn.getResponseCode() == 200) {
        // 获取响应的输入流对象
        InputStream is = conn.getInputStream();

        // 创建字节输出流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 定义读取的长度
        int len = 0;
        // 定义缓冲区
        byte buffer[] = new byte[1024];
        // 按照缓冲区的大小，循环读取
        while ((len = is.read(buffer)) != -1) {
          // 根据读取的长度写入到os对象中
          os.write(buffer, 0, len);
        }
        // 释放资源
        is.close();
        os.close();
        // 返回字符串
        result = new String(os.toByteArray());
      } else {
        throw new ServerException();
      }
    } catch (SocketTimeoutException e) {
      throw new TimeoutException();
    } catch (IOException e) {
      throw new NetworkException();
    }catch (JSONException e) {
      e.printStackTrace();
    }catch (NoSuchAlgorithmException e) {
      throw new ServerException();
    }catch (KeyManagementException e) {
      throw new ServerException();
    } finally {
      if (conn != null)
        conn.disconnect();
    }
    Loger.i("result jsonStr:" + result);
    return result;
  }

  // -------------   http Post request  ----------------
  protected String postJsonStrRequest(String strURL, final List<Pair> params)
      throws TimeoutException, NetworkException {
    Loger.d("strURL:" + strURL);

    if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
      throw new NoNetworkException();
    }

    //String urlstr = strURL.substring(0, strURL.indexOf("?"));
    //String param = strURL.substring(strURL.indexOf("?") + 1);
    StringBuilder json = new StringBuilder();
    HttpURLConnection conn = null;

    try {
      URL url = new URL(strURL);
      BufferedReader br = null;
      conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(this.timeout);
      conn.setReadTimeout(this.timeout);
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Charset", "UTF-8");
      conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
      conn.setRequestProperty("Content-Type", "application/json");
      //add header
      JSONObject paramJson = new JSONObject();
      if (params != null && !params.isEmpty()) {
//				for (Entry<String, String> header : headers.entrySet()){
//					conn.setRequestProperty(header.getKey(), header.getValue());
//				}
        for (Pair pair : params) {
//					if(pair.value instanceof String) {
//						paramJson.put(pair.key, URLEncoder.encode((String) pair.value,"utf-8"));
//					}else {
          if (pair.value instanceof ArrayList) {
            ArrayList list = (ArrayList) (pair.value);
            JSONArray jsonItems = new JSONArray();
            Iterator<String> listIterator = list.iterator();
            while (listIterator.hasNext()) {
              jsonItems.put(listIterator.next().toString());
            }
            paramJson.put(pair.key, jsonItems);
          } else {
            paramJson.put(pair.key, pair.value);
          }
//					}

        }
      }
      Loger.i("url[post] send params--" + paramJson.toString());

      DataOutputStream out = new DataOutputStream(
          conn.getOutputStream());
      out.write((paramJson.toString()).getBytes("UTF-8"));
      out.flush();
      out.close();
//			conn.connect();
      //OutputStream outs = conn.getOutputStream();
      //outs.write(param.getBytes());
      Loger.i("url[post] responseCode--" + conn.getResponseCode());
      InputStream is = conn.getInputStream();
      BufferedInputStream bis = new BufferedInputStream(is);

      boolean isGzip;

      // 判别gzip方法一
      // isGzip = "gzip".equals(conn.getContentEncoding());

      // 判别gzip方法二
      bis.mark(2);// 取前两个字节
      byte[] header = new byte[2];
      int result = bis.read(header);
      bis.reset();// reset输入流到开始位置
      // isGzip = (result!=-1 && getShort(header)==0x8b1f);
      isGzip = (result != -1 && getShort(header) == 0x1f8b);

      // 如果是gzip的压缩流 进行解压缩处理
      if (isGzip) {
        is = new GZIPInputStream(bis);
      } else {
        is = bis;
      }
      br = new BufferedReader(new InputStreamReader(is, "utf-8"));
      String temp;
      while ((temp = br.readLine()) != null) {
        json = json.append(temp);
      }
      br.close();
      is.close();
      //outs.close();
    } catch (SocketTimeoutException e) {
      throw new TimeoutException();
    } catch (IOException e) {
      throw new NetworkException();
    } catch (JSONException e) {
      e.printStackTrace();
    } finally {
      if (conn != null)
        conn.disconnect();
    }

    String jsonStr = json.toString().replace("\\u000d", "");

    Loger.i("result jsonStr:" + jsonStr);

    return jsonStr;
  }

  // -------------   http Get request  ----------------
  protected String getJsonStrRequest(String strURL, final List<Pair> params)
      throws TimeoutException, NetworkException {
    Loger.d("strURL:" + strURL);
    if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
      throw new NoNetworkException();
    }

    String result = "";

    URL url = null;
    HttpURLConnection conn = null;
    try {
      url = new URL(strURL);
      conn = (HttpURLConnection) url
          .openConnection();
      conn.setDoOutput(true);
      conn.setDoInput(true);
      conn.setRequestMethod("GET");// 设置请求的方式
      conn.setReadTimeout(5000);// 设置超时的时间
      conn.setConnectTimeout(5000);// 设置链接超时的时间
      // 设置请求的头
      conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");
      conn.setRequestProperty("Content-Type", "application/json");
      //add header
      JSONObject paramJson = new JSONObject();
      if (params != null && !params.isEmpty()) {
//				for (Entry<String, String> header : headers.entrySet()){
//					conn.setRequestProperty(header.getKey(), header.getValue());
//				}
        for (Pair pair : params) {
          paramJson.put(pair.key, pair.value);
        }
      }
      Loger.i("url[get] send params--" + paramJson.toString());
      DataOutputStream out = new DataOutputStream(
          conn.getOutputStream());
      out.write((paramJson.toString()).getBytes("utf-8"));
      out.flush();
      out.close();
      // 获取响应的状态码 404 200 505 302
      Loger.i("url[get] responseCode--" + conn.getResponseCode());
      if (conn.getResponseCode() == 200) {
        // 获取响应的输入流对象
        InputStream is = conn.getInputStream();

        // 创建字节输出流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // 定义读取的长度
        int len = 0;
        // 定义缓冲区
        byte buffer[] = new byte[1024];
        // 按照缓冲区的大小，循环读取
        while ((len = is.read(buffer)) != -1) {
          // 根据读取的长度写入到os对象中
          os.write(buffer, 0, len);
        }
        // 释放资源
        is.close();
        os.close();
        // 返回字符串
        result = new String(os.toByteArray());
      } else {
        throw new ServerException();
      }
    } catch (SocketTimeoutException e) {
      throw new TimeoutException();
    } catch (IOException e) {
      throw new NetworkException();
    }/* catch (JSONException e) {
      e.printStackTrace();
		} */ catch (JSONException e) {
      e.printStackTrace();
    } finally {
      if (conn != null)
        conn.disconnect();
    }
    Loger.i("result jsonStr:" + result);
    return result;
  }

  /**
   * 上传图片 (只上传一张图片)
   *
   * @param params  参数(不包括图片)
   * @param imgKey  图片key
   * @param imgPath 图片路径
   * @return
   * @throws NetworkException
   * @throws TimeoutException
   */
  public T uploadPic(final List<Pair> params, String imgKey, String imgPath)
      throws NetworkException, TimeoutException {

    //String ts = AppConfig.getTs();
    //paramsMap.put("actid", AppConfig.actId);
    //paramsMap.put("version", AppConfig.VERSION);
    //paramsMap.put("ts", ts);
    //paramsMap.put("vkey", AppConfig.getVKey(ts));
//		paramsMap.put("mobiletype", "2");
    String url = this.jsonEntity.getUrl() + getString4Params(params);
//		String url = "http://210.83.216.71:1944/Service/TrackingService.svc/App/SignForDispatch/"+ SP_AppStatus.getTokenId()+"/00297415/ggdgdh/2015-08-29/send";
    String jsonStr = getJsonStrUploadImg(
        url, imgKey, imgPath);

    T data = this.jsonEntity.parseJson2Entity(jsonStr);

    return data;
  }

  private String getJsonStrUploadImg(String actionUrl, String imgKey, String imgPath)
      throws NetworkException, TimeoutException {

    Loger.d("actionUrl:" + actionUrl);

    if (!PhoneStateUtil.hasInternet(BaseApplication.getContext())) {
      throw new NoNetworkException();
    }

    String BOUNDARY = java.util.UUID.randomUUID().toString();
    String PREFIX = "--", LINEND = "\r\n";
    String MULTIPART_FROM_DATA = "multipart/form-data";
    String CHARSET = "UTF-8";

    StringBuilder sb2 = new StringBuilder();
    HttpsURLConnection conn = null;

    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());

      URL url = new URL(actionUrl);
      conn = (HttpsURLConnection) url.openConnection();
      // 缓存的最长时间
      conn.setReadTimeout(60 * 1000);
      // 允许输入
      conn.setDoInput(true);
      // 允许输出
      conn.setDoOutput(true);
      // 不允许使用缓存
      conn.setUseCaches(false);
      conn.setRequestMethod("POST");
      conn.setRequestProperty("connection", "keep-alive");
      conn.setRequestProperty("Charset", "UTF-8");
      conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
          + ";boundary=" + BOUNDARY);

      conn.connect();
      DataOutputStream outStream = new DataOutputStream(
          conn.getOutputStream());
      // 发送文件数据
      if (!TextUtils.isEmpty(imgPath)) {
        //文件开头格式
//				outStream.writeBytes(PREFIX + BOUNDARY + LINEND);
//				outStream.writeBytes("Content-Disposition: form-data; name=\""+imgKey+"\""+ LINEND);
//				outStream.writeBytes(LINEND);
        //文件开头格式结束
        try {
          InputStream is = new FileInputStream(new File(imgPath));
          byte[] buffer = new byte[1024];
          int len = 0;
          while ((len = is.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
          }

          is.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
        outStream.write(LINEND.getBytes());
      }

      // 请求结束标志
      byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();

//			outStream.write(end_data);
      outStream.flush();
      outStream.close();
      // 得到响应码
      int res = conn.getResponseCode();
      String msg = conn.getResponseMessage();
      Loger.i("res:" + res);
      Loger.i("msg:" + msg);
      // System.out.println("getResponseCode-->" + res);
      // StringBuilder sb2 = new StringBuilder();
      if (res == HttpsURLConnection.HTTP_OK) {
        BufferedReader br = new BufferedReader(new InputStreamReader(
            conn.getInputStream(), "utf-8"));
        String tmp = "";
        while ((tmp = br.readLine()) != null) {
          sb2.append(tmp);
        }
      } else {
        outStream.close();
        throw new ServerException();
      }
      outStream.close();
    } catch (SocketTimeoutException e) {
      throw new TimeoutException();
    } catch (IOException e) {
      throw new NetworkException();
    } catch (NoSuchAlgorithmException e) {
      throw new ServerException();
    }catch (KeyManagementException e) {
      throw new ServerException();
    }finally {
      if (conn != null)
        conn.disconnect();
    }

    String jsonStr = sb2.toString();
    Loger.d("jsonStr:" + jsonStr);

    return jsonStr;
  }

  private int getShort(byte[] data) {
    return (int) ((data[0] << 8) | data[1] & 0xFF);
  }

  private class MyHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
      return true;
    }
  }

  private class MyTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
        throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
      return null;
    }
  }
}
