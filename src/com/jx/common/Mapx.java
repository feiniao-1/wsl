package com.jx.common;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Administrator 从数据库里取出的数据，一行数据映射成一个Mapx。
 *         Mapx跟HashMap的差别是增加了一些工具函数，其他没差别。 本工程用的数据库操作库是DButils
 *         本工程没用具体的pojo的类。而是都用的Mapx，从而使代码量很少，结构非常简洁，高效。
 */
public class Mapx<K, V> extends HashMap<K, V> {

	public Mapx() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Mapx(Map<? extends K, ? extends V> m) {
		super(m);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Integer getInt(String name) {
		Object o = this.get(name);
		if (o == null) {
			return null;
		}
		return Integer.parseInt(o.toString());
	}

	public BigDecimal getMoney(String name) {
		BigDecimal money = (BigDecimal) this.get(name);
		if (money == null) {
			money = new BigDecimal(0.00f);
		}
		return money;
	}

	public String getMoneyView(String name) {
		Object o = this.get(name);
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	public Float getFloat(String name) {
		Object o = this.get(name);
		if (o == null) {
			return null;
		}
		return Float.parseFloat(o.toString());
	}

	public String getString(String name) {
		Object o = this.get(name);
		if (o == null) {
			return null;
		}
		return o.toString();
	}

	public Date getDate(String name) {
		Object o = this.get(name);
		if (o == null) {
			return null;
		}
		long l = Integer.parseInt(o.toString()) * 1000;
		return new Date(l);
	}

	/**
	 * 返回在页面上显示的样子
	 */
	public String getIntView(String name) {
		Object o = this.get(name);
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	/**
	 * 返回在页面上显示的样子
	 */
	public String getFloatView(String name) {
		Object o = this.get(name);
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	/**
	 * 返回在页面上显示的样子
	 */
	public String getStringView(String name) {
		Object o = this.get(name);
		if (o == null) {
			return "";
		}
		return o.toString();
	}

	/**
	 * 返回在页面上显示的样子
	 */
	public String getDateView(String name) {
		Object o = this.get(name);
		if (o == null) {
			return "";
		}
		long l = Integer.parseInt(o.toString()) * 1000L;
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(l));
	}

	/**
	 * 带小时分钟
	 */
	public String getDateView2(String name) {
		Object o = this.get(name);
		if (o == null) {
			return "";
		}
		long l = Integer.parseInt(o.toString()) * 1000L;
		return new SimpleDateFormat("yyyy-MM-dd hh:mm").format(new Date(l));
	}

	public static void main(String[] args) {
		long test = 1428549661057L;

	}

}
