package fragment.ruby.toolsproject.entity;

import com.google.gson.annotations.SerializedName;


public class TestEntity extends BaseJsonEntity<TestEntity> {

    private String mUrl;

    @SerializedName("TokenID")
    private String TokenID;

    @SerializedName("UserID")
    private String UserID;

    public TestEntity(String url) {
        mUrl = url;
    }

    public String getTokenID() {
        return TokenID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public int getCacheTime() {
        return 0;
    }
}
