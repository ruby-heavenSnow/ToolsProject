package fragment.ruby.toolsproject.dao;

import net.iaf.framework.exception.DBException;
import net.iaf.framework.exception.NetworkException;
import net.iaf.framework.exception.TimeoutException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import fragment.ruby.toolsproject.entity.BaseJsonEntity;
import fragment.ruby.toolsproject.entity.Pair;

public abstract class DataAccess<T extends BaseJsonEntity<T>> {
	
	//缓存时间，单位毫秒
	protected long cacheTime = 0;
	// 完整的请求url字符串（带参数，包含接口请求所需的所有参数）
	protected String urlWhole = "";
	//作为key的请求url字符串
	protected String urlCacheKey = "";

	protected BaseJsonEntity<T> jsonEntity;

	public abstract T post(List<Pair> params, boolean cacheInvalidate)
			throws TimeoutException, NetworkException, DBException;

    public abstract T get(List<Pair> params, boolean cacheInvalidate)
            throws TimeoutException, NetworkException, DBException;
	
	public DataAccess(BaseJsonEntity<T> jsonEntity){
		this.jsonEntity = jsonEntity;
		this.cacheTime = this.jsonEntity.getCacheTime() * 60000L;
	}
	
	protected void buildUrl(List<Pair> params) {
		this.urlWhole = this.jsonEntity.getUrl();// + getString4Params(params);
		this.urlCacheKey = this.jsonEntity.getUrl() + getString4CacheKey(params);
	}
	
	protected String getString4CacheKey(List<Pair> params){
        List<Pair> pairs = new ArrayList<>();
        pairs.addAll(params);
        for(Pair pair : pairs) {
            if ("tokenID".equalsIgnoreCase(pair.key)) {
                pairs.remove(pair);
                break;
            }
        }
        return pair2String(pairs);
	}

    protected String getString4Params(List<Pair> params) {
        return pair2String(params);
	}

	private String pair2String(List<Pair> pairs) {
        if (null == pairs || pairs.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for(Pair pair : pairs) {
            sb.append("/");
            if(pair.value instanceof String) {
                try {
                    sb.append(URLEncoder.encode((String) pair.value,"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }else {
                sb.append(pair.value);
            }
        }
        return sb.toString();
    }
}