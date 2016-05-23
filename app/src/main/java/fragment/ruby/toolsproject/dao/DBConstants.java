package fragment.ruby.toolsproject.dao;

import net.iaf.framework.util.Version;

import java.util.ArrayList;
import java.util.List;

public class DBConstants {

	public static final String DATABASE_NAME = "test.db";
	public static final int DATABASE_VERSION = Version.getVersionCode();

	public static List<String> DB_CREATE_SQL;
	
	static {
		DB_CREATE_SQL = new ArrayList<String>();
		
		/**
		 * 缓存内容表
		 */
		DB_CREATE_SQL.add("DROP TABLE IF EXISTS tb_cache;");
		DB_CREATE_SQL.add("CREATE TABLE IF NOT EXISTS tb_cache ( " +
				"_id INTEGER PRIMARY KEY autoincrement, " +
				"key TEXT NOT NULL," +
				"create_time INTEGER NOT NULL," +
				"value BLOB NOT NULL" +
				");");

        /**
         * 接口调用缓存表
         */
        DB_CREATE_SQL.add("DROP TABLE IF EXISTS tb_api;");
        DB_CREATE_SQL.add("CREATE TABLE IF NOT EXISTS tb_api ( " +
                "_id INTEGER PRIMARY KEY autoincrement, " + // 自增长id
				"user_id TEXT NOT NULL," +
                //"type INTEGER NOT NULL," + // 接口类型（1：转运更新   2：签到   3：签收   4：退货）
                "value BLOB NOT NULL" + // 接口对应的实体
                ");");
	}
}
