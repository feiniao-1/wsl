package com.jx.common;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @author Administrator 数据库操作类
 */
public class DB {
	private static ComboPooledDataSource ds = null;

	public static void init() {
		if (ds != null) {
			return;
		}
		ds = new ComboPooledDataSource("jxapp");
	}

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		init();
		test();
	}

	public static QueryRunner getRunner() {
		return new QueryRunner(ds);
	}

	private static void test() throws SQLException {
		DB.init();
		// TODO Auto-generated method stub
		// QueryRunner qr = new QueryRunner(ds);
		// ResultSetHandler<List<Mapx<String, Object>>> handler = new
		// MapxListHandler();
		// List<Mapx<String, Object>> list=null;
		// list = qr.query("select id,name,age from test", handler);
		// // System.out.println(list);
		// for(Mapx<String, Object> map :list){
		// System.out.println(map.getInt("id")+"\t"+map.getString("name")+"\t"+map.getInt("age"));
		// }
		// for(int i=1001;i<2001;i++){
		// DB.getRunner().update("insert into test(id,name,age,createtime,companyid) values(?,?,?,?,?)",i,"名字"+i,(int)(Math.random()*99+1),new
		// java.util.Date().getTime()/1000,(int)(Math.random()*10+1));
		// }
		// List<Mapx<String, Object>> list =
		// DB.getRunner().query("select id,name,age from test", new
		// MapxListHandler());
		// System.out.println((Long)list.get(0).get("money"));
		// long l = (Long)list.get(0).get("money");
		// double d = (double)l;
		// BigDecimal b= (BigDecimal) list.get(0).get("money2");
		// System.out.println(b);
		// b= b.add(new BigDecimal("-1.245"));
		// System.out.println(list);

		for (int i = 1; i <= 1000; i++) {
			DB.getRunner()
					.update("insert into admin_product(id,name,createtime,img1,img2,img3,img4,img5,content,pricemin,pricemax,user,status,type1,type2,type3,usertype,del) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
							i,
							"商品" + i,
							new Date().getTime() / 1000,
							"http://101.200.174.101/upload/a0bf99d8-2d5e-4b89-b256-93f76fd1d6c7.png",
							"http://101.200.174.101/upload/c73b57fc-620a-4c4b-a1c0-d90e9d9de983.png",
							"http://101.200.174.101/upload/8593fd7e-e14e-410f-9629-f369bc7b0574.png",
							"http://101.200.174.101/upload/efd1bbec-9af9-42f8-9114-5a9d5b1d44d7.png",
							"http://101.200.174.101/upload/2cfcba92-ff2e-43e3-a280-a33c5796dca5.png",
							"内容" + i, new BigDecimal(Math.random() * 100),
							new BigDecimal(Math.random() * 100 + 100), 10063,
							"已上线", 10019, 10020, 10027, "经销商", 0);
		}
	}

}
