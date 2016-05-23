package fragment.ruby.toolsproject.controller;

import net.iaf.framework.controller.BaseController;
import net.iaf.framework.exception.IException;
import net.iaf.framework.util.EncryptUtil;
import java.util.ArrayList;
import fragment.ruby.toolsproject.dao.DataAccessRemote;
import fragment.ruby.toolsproject.entity.TestEntity;
import fragment.ruby.toolsproject.entity.Pair;


public class TestController extends BaseController {

    /**
     * @param callback view回调
     * @param account  用户帐号名
     * @param password 用户密码
     */
    public void testFunction(UpdateViewAsyncCallback<TestEntity> callback, String account, String password) {
        doAsyncTask("login", callback, new DoAsyncTaskCallback<String, TestEntity>() {
            @Override
            public TestEntity doAsyncTask(String... params) throws IException {
                DataAccessRemote<TestEntity> dataAccess = new DataAccessRemote<>(new TestEntity("https://ddddddd"));
                ArrayList<Pair> param = new ArrayList<>();
                param.add(new Pair("UserName", params[0]));
                param.add(new Pair("Md5", EncryptUtil.MD5(params[1]))); //"B7DFE9134C7A717A3B6EAF37BDC1EF7B"));
                return dataAccess.post(param, true);
            }
        }, account, password);
    }
}
