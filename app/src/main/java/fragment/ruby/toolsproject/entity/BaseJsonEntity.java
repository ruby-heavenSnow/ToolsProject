package fragment.ruby.toolsproject.entity;

import android.text.TextUtils;

import com.esteelauder.tms.util.GsonUtil;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import net.iaf.framework.exception.JsonServerException;
import net.iaf.framework.exception.ServerException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

public abstract class BaseJsonEntity<T> implements Serializable, Cloneable {

    public static final String CODE_SUCCESS = "1";
    private static final long serialVersionUID = 670125849422940907L;

    //--------------------------------------------------------------------
    @SerializedName("ErrorCode")
    private String ErrorCode;

    @SerializedName("Message")
    private String Message;

    @SerializedName("Status")
    private String Status;

    //--------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public T parseJson2Entity(String jsonStr) throws ServerException {
        //Loger.v(jsonStr);
        T jsonEntity;
        try {
            if (TextUtils.isEmpty(jsonStr)) {
                return null;
            }

            //jsonStr = jsonStr.replace("\"\"", "null");

            JSONObject jsonObject = new JSONObject(jsonStr);
            if (!jsonObject.getString("Status").equals(CODE_SUCCESS)) {
                throw new ServerException(jsonObject.getString("ErrorCode"), jsonObject.getString("Message"));
            }
        } catch (JSONException e) {
            throw new JsonServerException();
        }
        try {
            Class<T> entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            jsonEntity = GsonUtil.jsonToBean(jsonStr, entityClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            throw new JsonServerException();
        }
        return jsonEntity;
    }

    /**
     * 请求的url
     */
    public abstract String getUrl();

    /**
     * 缓存时间
     */
    public abstract int getCacheTime();

    public String getErrorCode() {
        return ErrorCode;
    }

    public String getMessage() {
        return Message;
    }

    public String getStatus() {
        return Status;
    }

}
