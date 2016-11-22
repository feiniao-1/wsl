package com.jx.action;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;

import com.jx.common.DB;
import com.jx.common.G;
import com.jx.common.Mapx;
import com.jx.common.MapxListHandler;

public class IndexAction extends BaseAction {
	private static String[] colNames = { "ID", "姓名", "年龄", "日期", "公司名" };
	private static String sqlPre = "select a.id,a.name,a.age,a.createtime,b.name as companyname from test a,company b where 1=1 and a.companyid=b.id";
	private static String sqlPreCount = "select count(1) as count from test a,company b where 1=1 and a.companyid=b.id";

	@Override
	public void go() throws Exception {
		HashMap<String, String> param = G.getParamMap(req);
		param.put("page", param.get("page") == null ? "1" : param.get("page"));
		String sql = G.sqlForQuery(param);
		System.out.println("sql:" + sql);
		String sqlAll = sqlPre + sql + G.sqlLimit(param);
		System.out.println("sqlAll:" + sqlAll);
		String sqlCount = sqlPreCount + sql;
		System.out.println("sqlCount:" + sqlCount);
		QueryRunner qr = DB.getRunner();
		List<Mapx<String, Object>> listAll = qr.query(sqlAll,
				new MapxListHandler());
		System.out.println("listAll:" + listAll);
		List<Mapx<String, Object>> listCount = qr.query(sqlCount,
				new MapxListHandler());
		System.out.println("listCount:" + listCount);
		Map root = new HashMap();
		root.put("listAll", listAll);
		root.put("total", listCount.get(0).getInt("count"));
		System.out.println(listCount.get(0).getInt("count"));
		root.put("colNames", colNames);
		root.put("paramMap", param);
		root.put("uri", uri);
		// 以上是通用操作
		root.put("name", "tom");
		this.render("list.ftl", root);

	}

}
