package fragment.ruby.toolsproject.db;

import android.content.Context;
import android.text.TextUtils;

import net.iaf.framework.app.BaseApplication;
import net.iaf.framework.util.SharePrefsHelper;

/**
 * 应用级别的状态，持久化到 SharedPreferences
 */
public class SP_AppStatus
{
	/** 保存session的SharedPreference的文件名*/
	public static final String SESSION_FILE = "SP_AppStatus";
	private static SharePrefsHelper sessionSp = null;
	
	////////////////////////////////////以下为需要保存的数据////////////////////////////////////////
	/** 用户id */
	private static final String KEY_USER_ID = "userid";

	/**
	 * 获取用户id
	 * @return 用户id
	 */
	public static String getUserId() {
		return getString(KEY_USER_ID, "");
	}

	/**
	 * 设置用户id
	 * @param userid 用户id
	 */
	public static void setUserId(String userid) {
		setString(KEY_USER_ID, userid);
	}


	//////////////////////////////////以下为基础方法//////////////////////////////////////////////
	static {
		init();
	}
	
	private static void init(){
		if(sessionSp == null){
			sessionSp = new SharePrefsHelper(BaseApplication.getContext(), SESSION_FILE, Context.MODE_MULTI_PROCESS);
		}
	}
	
	public static String getString(String key, String def)
	{
		init();
		return sessionSp.getString(key, def);
	}
	
	public static void setString(String key, String value)
	{
		init();
		if(TextUtils.isEmpty(value)){
			value = "";
		}
		sessionSp.setString(key, value);
	}
	
	public static long getLong(String key, long def)
	{
		init();
		return sessionSp.getLong(key, def);
	}
	
	public static void setLong(String key, long def)
	{
		init();
		sessionSp.setLong(key, def);
	}
	
	public static boolean getBoolean(String key, boolean def)
	{
		init();
		return sessionSp.getBoolean(key, def);
	}
	
	public static void setBoolean(String key, boolean value)
	{
		init();
		sessionSp.setBoolean(key, value);
	}
	
	public static int getInteger(String key, int def) 
	{
		init();
		return sessionSp.getInt(key, def);
	}
	
	public static void setInteger(String key, int value)
	{
		init();
		sessionSp.setInt(key, value);
	}
	
	public static float getFloat(String key, float def) {
		init();
		return sessionSp.getFloat(key, def);
	}
	
	public static void setFloat(String key, float value) {
		init();
		sessionSp.setFloat(key, value);
	}
}
