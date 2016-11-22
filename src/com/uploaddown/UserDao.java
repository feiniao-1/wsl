package com.uploaddown;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.uploaddown.*;
import com.jx.common.*;
public class UserDao {

	/**
	 * 1. 上传数据的保存
	 */
	public void save(User user) {
		try {
			String sql = "INSERT INTO t_user(userName,fileName,fullName) VALUES(?,?,?);";
			DB.getRunner()//
					.update(sql,//
							user.getUserName(),//
							user.getFileName(),//
							user.getFullName());
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 2. 查看全部信息
	 * @return 
	 */
	public static List<User> getAll(){
		try {
			String sql = "select * from t_user";
			return DB.getRunner().query(sql,
					new BeanListHandler<User>(User.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 3. 主键查询
	 * @return 
	 */
	public static User findById(int id){
		try {
			String sql = "select * from t_user where id=?";
			return DB.getRunner().query(sql,new BeanHandler<User>(User.class),id);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
