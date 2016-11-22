package com.jx.common;

import java.sql.SQLException;

import org.apache.commons.dbutils.handlers.MapHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator 生成唯一数据库key
 */
public class Ukey {
	public static String user = "user";
	public static String test = "test";
	public static String admin_user = "admin_user";
	public static String admin_fuwuzhan = "admin_fuwuzhan";
	public static String admin_product_type = "admin_product_type";
	public static String admin_product = "admin_product";
	public static String admin_product_price = "admin_product_price";
	public static String admin_order = "admin_order";
	public static String admin_order_product = "admin_order_product";
	public static String admin_jiaoyi_log = "admin_jiaoyi_log";
	public static String admin_xinxi = "admin_xinxi";
	public static String admin_zixun = "admin_zixun";
	public static String admin_code = "admin_code";
	private static HashMap<String, Object> lockMap = new HashMap<String, Object>();
	static {
		lockMap.put(user, new Object());
		lockMap.put(test, new Object());
		lockMap.put(admin_user, new Object());
		lockMap.put(admin_fuwuzhan, new Object());
		lockMap.put(admin_product_type, new Object());
		lockMap.put(admin_product, new Object());
		lockMap.put(admin_product_price, new Object());
		lockMap.put(admin_order, new Object());
		lockMap.put(admin_order_product, new Object());
		lockMap.put(admin_jiaoyi_log, new Object());
		lockMap.put(admin_xinxi, new Object());
		lockMap.put(admin_zixun, new Object());
		lockMap.put(admin_code, new Object());
	}

	public static void main(String[] args) throws SQLException {
		DB.init();
		System.out.println(getKey(Ukey.user));

	}

	public static int getKey(String name) throws SQLException {
		synchronized (lockMap.get(name)) {
			Map<String, Object> map = DB.getRunner().query(
					"select value from ids where name=?", new MapHandler(),
					name);
			if (map == null) {// 没有则添加
				DB.getRunner().update(
						"insert into ids(name,value) values(?,10000)", name);
				return 10000;
			}
			int value = (Integer) map.get("value");
			value += 1;
			DB.getRunner().update("update ids set value=? where name=?", value,
					name);
			return value;
		}
	}
}
