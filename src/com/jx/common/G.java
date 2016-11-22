package com.jx.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;



/**
 * @author Administrator 全局工具函数类。G是Global的缩写。这个类里都是一些全局公用的方法。
 */
public class G {
	public static int pageSize = 10;
	/**
	 * 下拉框数据
	 */
	public static Map<String, String[]> selectMap = new HashMap<String, String[]>() {
		{
			put("selectUserType", new String[] { "高级管理员", "客服", "服务站", "网站运维",
					"财务", "普通用户", "生产企业", "经销商", "消费企业" });// 用户类型
			put("selectUserShenHe", new String[] { "待审核", "审核通过", "审核未通过" });// 用户审核状态
			put("selectUserStatus", new String[] { "有效", "无效" });// 用户能用状态
			put("selectProductStatus", new String[] { "已上线", "未上线" });// 商品状态状态

			put("selectXinXiType", new String[] { "二手机械", "招聘市场", "新机械" });// 信息分类
			put("selectZiXunStatus", new String[] { "未处理", "已处理" });// 咨询状态

		}
	};
	/**
	 * cookie失效时间，单位秒
	 */
	public static int maxTime = 3600 * 24;

	/**
	 * @param key
	 *            主键
	 * @param value
	 *            值
	 * @param maxAge
	 *            有效时间
	 * @param response
	 *            响应
	 * @return void
	 */
	public static void setCookie(String key, String value, int maxAge,
			HttpServletResponse response) {
		Cookie namecookie = new Cookie(key, value);
		namecookie.setMaxAge(maxAge);
		response.addCookie(namecookie);

	}

	/**
	 * @param key
	 *            主键
	 * @param value
	 *            值
	 * @param maxAge
	 *            有效时间
	 * @return void
	 */
	public static void setCookie(String key, String value,
			HttpServletResponse response) {
		Cookie namecookie = new Cookie(key, value);
		// namecookie.setMaxAge(60 * 60 * 24 * 365);
		namecookie.setMaxAge(60 * 60 * 24);
		response.addCookie(namecookie);
	}

	/**
	 * @param request
	 *            请求
	 * @param key
	 *            主键
	 * @return String 读取key 对应的值
	 */
	public static String getCookie(String key, HttpServletRequest request) {
		String value = null;
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {

			for (int i = 0; i < cookies.length; i++) {
				Cookie c = cookies[i];
				if (c.getName().equalsIgnoreCase(key)) {
					value = c.getValue();
					return value;
				}
			}
		}
		return value;
	}

	/**
	 * @param req
	 * @return 获得请求的参数map
	 * @throws UnsupportedEncodingException
	 */
	public static HashMap<String, String> getParamMap(HttpServletRequest req)
			throws UnsupportedEncodingException {
		// if(req.getRequestURI().indexOf("front_reg.jsp")>-1){//临时，未正式交付，无法创建新用户
		// return null;
		// }
		HashMap<String, String> result = new HashMap<String, String>();
		Map map = req.getParameterMap();
		for (Object entry : map.entrySet().toArray()) {
			Map.Entry entry2 = (Map.Entry) entry;
			String[] vv = (String[]) entry2.getValue();
			String v = "";
			if (vv != null && vv.length > 0) {
				v = new String(vv[0].getBytes("ISO-8859-1"), "utf-8");
			}
			result.put(entry2.getKey().toString(), v.trim());
		}
		result.remove("1");
		System.out.println("param:" + result);
		return result;
	}

	public static int toInt(Object o, int i) {
		try {
			return Integer.parseInt(o.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return i;
		}
	}

	/**
	 * 根据sqlPre和param直接生成完整sql q_i_a_e_name q_查询前缀 i整数s字符串d日期 a表a
	 * e等于l小于等于g大于等于li模糊查询like name字段名字
	 * 
	 * @throws ParseException
	 */
	public static String sqlForQuery(HashMap<String, String> param)
			throws ParseException {
		HashMap<String, String> paramOld = param;
		param = new HashMap<String, String>(param);
		String s = "";
		for (Iterator iterator = param.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry<String, String> type = (Map.Entry<String, String>) iterator
					.next();
			if (type.getKey().startsWith("q_") && type.getValue() != null
					&& !type.getValue().trim().equals("")) {
				String[] ss = type.getKey().split("_");
				if (ss[1].equals("d")) {
					try {
						new SimpleDateFormat("yyyy-MM-dd").parse(type
								.getValue());
					} catch (Exception e) {
						paramOld.remove(type.getKey());
						continue;
					}
				}
				if (ss[1].equals("i")) {
					try {
						Integer.parseInt(type.getValue());
					} catch (Exception e) {
						paramOld.remove(type.getKey());
						continue;
					}
				}
				s += " and ";
				s += " " + ss[2] + "." + ss[4] + " ";
				if (ss[3].equals("e")) {
					s += " = ";
				}
				if (ss[3].equals("l")) {
					s += " <= ";
				}
				if (ss[3].equals("g")) {
					s += " >= ";
				}
				if (ss[3].equals("li")) {
					s += " like ";
				}
				if (ss[1].equals("i")) {
					s += Integer.parseInt(type.getValue());
				}
				if (ss[1].equals("s")) {
					if (ss[3].equals("li")) {
						s += "'%" + type.getValue() + "%'";
					} else {
						s += "'" + type.getValue() + "'";
					}
				}
				if (ss[1].equals("d")) {
					if (ss[3].equals("l")) {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						s += sdf.parse(type.getValue()).getTime() / 1000 + 3600 * 24;
					} else {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyy-MM-dd");
						s += sdf.parse(type.getValue()).getTime() / 1000;
					}
				}
			}
		}
		// String page = param.get("page");
		// if(page==null||page.trim().equals("")){
		// page="1";
		// }
		// int p = Integer.parseInt(page);
		// s+=" limit "+(p-1)*pageSize+","+pageSize;
		return s;
	}

	/**
	 * 添加sql语句limit部分
	 * 
	 * @param param
	 * @return
	 * @throws ParseException
	 */
	public static String sqlLimit(HashMap<String, String> param)
			throws ParseException {
		String page = param.get("page");
		if (page == null || page.trim().equals("")) {
			page = "1";
		}
		int p = Integer.parseInt(page);
		if (p < 1) {
			p = 1;
		}
		int pageSize_ = pageSize;
		if (param.get("pageSize") != null) {
			pageSize_ = Integer.parseInt(param.get("pageSize"));
		}
		String result = " limit " + (p - 1) * pageSize_ + "," + pageSize_;
		return result;
	}

	/**
	 * 没有数据库查询条件
	 * 
	 * @param param
	 * @param ss
	 * @return
	 */
	public static String toUriForQuery(Map<String, String> param) {
		param = new HashMap<String, String>(param);
		param.remove("page");
		String s = "";
		for (Iterator iterator = param.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry<String, String> type = (Map.Entry<String, String>) iterator
					.next();
			if (type.getKey().startsWith("q_")) {
				continue;
			}
			if (s.equals("")) {
				s = type.getKey() + "=" + type.getValue();
			} else {
				s += "&" + type.getKey() + "=" + type.getValue();
			}
		}
		return s;
	}

	/**
	 * 组成uri
	 * 
	 * @param param
	 * @return
	 */
	public static String toUri(Map<String, String> param, String... ss) {
		param = new HashMap<String, String>(param);
		int count = ss.length / 2;
		for (int i = 0; i < count; i++) {
			String key = ss[i * 2];
			String value = ss[i * 2 + 1];
			if (value == null) {
				param.remove(key);
			} else {
				param.put(key, value);
			}

		}
		String s = "";
		for (Iterator iterator = param.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry<String, String> type = (Map.Entry<String, String>) iterator
					.next();
			if (s.equals("")) {
				s = type.getKey() + "=" + type.getValue();
			} else {
				s += "&" + type.getKey() + "=" + type.getValue();
			}
		}
		return s;
	}

	/**
	 * @param param
	 * @param ss
	 * @return
	 */
	public static String toDateStr(Object time) {
		if (time == null) {
			return null;
		}
		return new SimpleDateFormat("yyyy-MM-dd").format(new Date(Integer
				.parseInt(time.toString()) * 1000L));
	}

	public static List<String[]> pageList(String uri,
			HashMap<String, String> param, int total) {
		// int pageSize = 2;
		int page = Integer.parseInt(param.get("page"));
		int pageSizex = pageSize;
		if (param.get("pageSize") != null) {
			pageSizex = Integer.parseInt(param.get("pageSize"));
		}
		int pageCount = total % pageSizex == 0 ? total / pageSizex
				: ((int) (total / pageSizex) + 1);
		if (total == 0) {
			pageCount = 1;
		}
		List<String[]> list = new ArrayList<String[]>();
		uri = uri + "?1=1&" + G.toUri(param, "page", null);
		int control1 = 3;// 两端最多几个
		int control2 = 2;// 中间page两侧最多几个
		for (int i = 1; i <= pageCount; i++) {
			String[] tmp = new String[] { "" + i, uri + "&page=" + i };
			if (i == page) {
				tmp[0] = "[" + i + "]";
			}
			if (i > control1 && i < page - control2) {
				tmp = new String[] { "&hellip;", "" };
				i = page - control2 - 1;
			}
			if (i > page + control2 && i < pageCount - control1) {
				tmp = new String[] { "&hellip;", "" };
				i = pageCount - control1;
			}
			list.add(tmp);
		}
		System.out.println(JSON.toJSONString(list));
		return list;
	}

	public static String fromUrl(HttpServletRequest req)
			throws UnsupportedEncodingException {
		String str = req.getRequestURI();
		if (req.getQueryString() != null) {
			str += "?" + req.getQueryString();
		}
		str = URLEncoder.encode(str, "utf-8");
		return str;
	}

	public static HashMap navData(HttpServletRequest req) {
		String uri = req.getRequestURI();

		return null;
	}

	/**
	 * 生成select中option数据
	 * 
	 * @throws SQLException
	 */
	public static List<Mapx<String, Object>> optionList(String tableName)
			throws SQLException {
		String sql = "select id,name from " + tableName + " order by id";
		List<Mapx<String, Object>> listAll = DB.getRunner().query(sql,
				new MapxListHandler());
		return listAll;
	}
	/**
     * 正则表达式：验证手机号
     */
    //public static final String REGEX_MOBILE ="^((13[0-9])|(14[5,7,9])|(15[^4,\\D])|(17[0,1,3,5-8])|(18[0-9]))\\d{8}$";
	public static final String REGEX_MOBILE ="^1[3|4|5|8][0-9]\\d{8}$";
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile(String mobile) {
        return Pattern.matches(REGEX_MOBILE, mobile);
    } 
    /**
     * 正则表达式：邮箱
     */
    public static final String REGEX_MOBILE1 = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
    /**
     * 校验手机号
     * 
     * @param mobile
     * @return 校验通过返回true，否则返回false
     */
    public static boolean isMobile1(String mail) {
        return Pattern.matches(REGEX_MOBILE1, mail);
    } 
	/**
	 * 检查字段合法性.如果校验正确则返回正确的值，如果校验错误，则返回null。
	 */
	public static Object commonCheckx(List<String> errors, String value,
			String... rules) throws SQLException {
		List list = errors;
		Object result = null;
		if (rules[1].equals("must")) {
			if (value == null || value.trim().equals("")) {
				list.add(rules[0] + "不能为空");
				return null;
			}
		}
		if (rules[1].equals("phone")) {
			if (value == null || value.trim().equals("")) {
				list.add("手机号码有误");
				return null;
			}else{ //手机号匹配验证有问题
				System.out.println("手机号"+value+"=="+isMobile(value));
				if(!isMobile(value)){  //by hyc  2016-10-10
					list.add("手机号码有误");
				return null;
				}
			}
		}
		if (rules[1].equals("mail")) {
			if (value == null || value.trim().equals("")) {
				list.add("请输入有效的E_mail");
				return null;
			}else{ //E_mail匹配验证有问题
				System.out.println("邮箱"+value+"=="+isMobile1(value));
				if(!isMobile1(value)){
					list.add("请输入有效的E_mail");
				return null;
				}
			}
		}
		if (rules[1].equals("nomust")) {
			if (value == null || value.trim().equals("")) {
				return null;
			}
		}
		if (rules[2].equals("int")) {
			try {
				result = Integer.parseInt(value);
			} catch (Exception e) {
				list.add(rules[0] + "必须为数字");
				return null;
			}
		}
		if (rules[2].equals("float")) {
			try {
				result = Float.parseFloat(value);
			} catch (Exception e) {
				list.add(rules[0] + "必须为小数");
				return null;
			}
		}
		if (rules[2].equals("date")) {
			try {
				result = new SimpleDateFormat("yyyy-MM-dd").parse(value);
			} catch (Exception e) {
				list.add(rules[0] + "必须为日期格式");
				return null;
			}
		}
		if (rules[2].equals("string")) {
			result = value;
		}
		if (rules[2].equals("money")) {
			try {
				result = new BigDecimal(value);
			} catch (Exception e) {
				list.add(rules[0] + "必须为数字格式");
				return null;
			}
		}

		for (int i = 3; i < rules.length; i++) {
			String s = rules[i];
			String[] sss = s.split(",");
			if (sss[0].equals("between")) {
				if (rules[2].equals("int")) {
					int left = Integer.parseInt(sss[1]);
					int right = Integer.parseInt(sss[2]);
					if ((int) result < left || (int) result > right) {
						list.add(rules[0] + "必须在" + left + "~" + right + "之间");
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float left = Float.parseFloat(sss[1]);
					float right = Float.parseFloat(sss[2]);
					if ((float) result < left || (float) result > right) {
						list.add(rules[0] + "必须在" + left + "~" + right + "之间");
						return null;
					}
				}
				if (rules[2].equals("string")) {
					int left = Integer.parseInt(sss[1]);
					int right = Integer.parseInt(sss[2]);
					if (((String) result).length() < left
							|| ((String) result).length() > right) {
						list.add(rules[0] + "长度必须在" + left + "~" + right + "之间");
						return null;
					}
				}
				if (rules[2].equals("money")) {
					BigDecimal tmp = (BigDecimal) result;
					float tmpf = tmp.floatValue();
					float left = Float.parseFloat(sss[1]);
					float right = Float.parseFloat(sss[2]);
					if (tmpf < left || tmpf > right) {
						list.add(rules[0] + "必须在" + left + "~" + right + "之间");
						return null;
					}
				}
			}
			if (sss[0].equals("lte")) {
				if (rules[2].equals("int")) {
					int ii = Integer.parseInt(sss[1]);
					if ((int) result > ii) {
						list.add(rules[0] + "必须小于等于" + ii);
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float ii = Float.parseFloat(sss[1]);
					if ((float) result > ii) {
						list.add(rules[0] + "必须小于等于" + ii);
						return null;
					}
				}
				if (rules[2].equals("money")) {
					BigDecimal tmp = (BigDecimal) result;
					float tmpf = tmp.floatValue();
					float ii = Float.parseFloat(sss[1]);
					if (tmpf > ii) {
						list.add(rules[0] + "必须小于等于" + ii);
						return null;
					}
				}
			}
			if (sss[0].equals("lt")) {
				if (rules[2].equals("int")) {
					int ii = Integer.parseInt(sss[1]);
					if ((int) result >= ii) {
						list.add(rules[0] + "必须小于" + ii);
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float ii = Float.parseFloat(sss[1]);
					if ((float) result >= ii) {
						list.add(rules[0] + "必须小于" + ii);
						return null;
					}
				}
				if (rules[2].equals("money")) {
					BigDecimal tmp = (BigDecimal) result;
					float tmpf = tmp.floatValue();
					float ii = Float.parseFloat(sss[1]);
					if (tmpf >= ii) {
						list.add(rules[0] + "必须小于" + ii);
						return null;
					}
				}
			}
			if (sss[0].equals("gte")) {
				if (rules[2].equals("int")) {
					int ii = Integer.parseInt(sss[1]);
					if ((int) result < ii) {
						list.add(rules[0] + "必须大于等于" + ii);
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float ii = Float.parseFloat(sss[1]);
					if ((float) result < ii) {
						list.add(rules[0] + "必须大于等于" + ii);
						return null;
					}
				}
				if (rules[2].equals("money")) {
					BigDecimal tmp = (BigDecimal) result;
					float tmpf = tmp.floatValue();
					float ii = Float.parseFloat(sss[1]);
					if (tmpf < ii) {
						list.add(rules[0] + "必须大于等于" + ii);
						return null;
					}
				}
			}
			if (sss[0].equals("gt")) {
				if (rules[2].equals("int")) {
					int ii = Integer.parseInt(sss[1]);
					if ((int) result <= ii) {
						list.add(rules[0] + "必须大于" + ii);
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float ii = Float.parseFloat(sss[1]);
					if ((float) result <= ii) {
						list.add(rules[0] + "必须大于" + ii);
						return null;
					}
				}
				if (rules[2].equals("money")) {
					BigDecimal tmp = (BigDecimal) result;
					float tmpf = tmp.floatValue();
					float ii = Float.parseFloat(sss[1]);
					if (tmpf <= ii) {
						list.add(rules[0] + "必须大于" + ii);
						return null;
					}
				}
			}
			if (sss[0].equals("e")) {
				if (rules[2].equals("int")) {
					int ii = Integer.parseInt(sss[1]);
					if ((int) result != ii) {
						list.add(rules[0] + "必须等于" + ii);
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float ii = Float.parseFloat(sss[1]);
					if ((float) result != ii) {
						list.add(rules[0] + "必须等于" + ii);
						return null;
					}
				}
			}
			if (sss[0].equals("ne")) {
				if (rules[2].equals("int")) {
					int ii = Integer.parseInt(sss[1]);
					if ((int) result == ii) {
						list.add(rules[0] + "不能等于" + ii);
						return null;
					}
				}
				if (rules[2].equals("float")) {
					float ii = Float.parseFloat(sss[1]);
					if ((float) result == ii) {
						list.add(rules[0] + "不能等于" + ii);
						return null;
					}
				}
			}
			if (sss[0].equals("unique")) {// unique,tablename,colname,id
				if (rules[2].equals("int") || rules[2].equals("float")
						|| rules[2].equals("string")) {
					String tableName = sss[1];
					String colName = sss[2];
					int id = Integer.parseInt(sss[3]);
					String str = result + "";
					if (rules[2].equals("string")) {
						str = "'" + str + "'";
					}
					String sql = "select count(1) as count from " + tableName
							+ " where id != " + id + " and " + colName + " = "
							+ str + " and (del is NULL or del <>1)";
					List<Mapx<String, Object>> listAll = DB.getRunner().query(
							sql, new MapxListHandler());
					int count = listAll.get(0).getInt("count");
					if (count > 0) {
						list.add(rules[0] + "已经存在，请填写其他值");
						return null;
					}
				}
			}
			if (sss[0].equals("exist")) {// exist,tablename,colname
				if (rules[2].equals("int") || rules[2].equals("float")
						|| rules[2].equals("string")) {
					String tableName = sss[1];
					String colName = sss[2];
					String str = result + "";
					if (rules[2].equals("string")) {
						str = "'" + str + "'";
					}
					String sql = "select count(1) as count from " + tableName
							+ " where " + colName + " = " + str;
					List<Mapx<String, Object>> listAll = DB.getRunner().query(
							sql, new MapxListHandler());
					int count = listAll.get(0).getInt("count");
					if (count == 0) {
						list.add(rules[0] + "不存在，请重新填写");
						return null;
					}
				}
			}
		}
		return result;
	}

	// /**
	// * 通用的参数检查功能，返回错误信息。
	// * @throws SQLException
	// */
	// public static List<String> commonCheck(Map<String,String[]> tableMap,
	// Map<String,String[]> ruleMap, HashMap<String,String>
	// param,HashMap<String,Object> resultMap) throws SQLException{
	// List list= new ArrayList<String>();
	// for (Iterator iterator = ruleMap.entrySet().iterator();
	// iterator.hasNext();) {
	// Map.Entry<String, String[]> object = (Map.Entry<String, String[]>)
	// iterator.next();
	// String key = object.getKey();
	// // if(key.equals("a_password")){
	// // System.out.println();
	// // }
	// String[] rules = object.getValue();
	// String value = param.get(key);
	// if(rules[1].equals("must")){
	// if(value==null||value.trim().equals("")){
	// list.add(rules[0]+"不能为空");
	// // return list;
	// continue;
	// }
	// }
	// if(rules[1].equals("nomust")){
	// if(value==null||value.trim().equals("")){
	// continue;
	// }
	// }
	// if(rules[2].equals("int")){
	// try {
	// resultMap.put(key, Integer.parseInt(value));
	// } catch (Exception e) {
	// list.add(rules[0]+"必须为数字");
	// continue;
	// }
	// }
	// if(rules[2].equals("float")){
	// try {
	// resultMap.put(key, Float.parseFloat(value));
	// } catch (Exception e) {
	// list.add(rules[0]+"必须为小数");
	// continue;
	// }
	// }
	// if(rules[2].equals("date")){
	// try {
	// resultMap.put(key, new SimpleDateFormat("yyyy-MM-dd").parse(value));
	// } catch (Exception e) {
	// list.add(rules[0]+"必须为日期格式");
	// continue;
	// }
	// }
	// if(rules[2].equals("string")){
	// resultMap.put(key,value);
	// }
	// if(rules[2].equals("money")){
	// try {
	// resultMap.put(key, new BigDecimal(value));
	// } catch (Exception e) {
	// list.add(rules[0]+"必须为数字格式");
	// continue;
	// }
	// }
	// String[] ss = rules[3].split(",");
	// for (int i = 0; i < ss.length; i++) {
	// String s = ss[i];
	// String[] sss = s.split("_");
	// if(sss[0].equals("between")){
	// if(rules[2].equals("int")){
	// int left = Integer.parseInt(sss[1]);
	// int right = Integer.parseInt(sss[2]);
	// if((int)resultMap.get(key) < left || (int)resultMap.get(key) > right){
	// list.add(rules[0]+"必须在"+left+"~"+right+"之间");
	// }
	// }
	// if(rules[2].equals("float")){
	// float left = Float.parseFloat(sss[1]);
	// float right = Float.parseFloat(sss[2]);
	// if((float)resultMap.get(key) < left || (float)resultMap.get(key) >
	// right){
	// list.add(rules[0]+"必须在"+left+"~"+right+"之间");
	// }
	// }
	// if(rules[2].equals("string")){
	// int left = Integer.parseInt(sss[1]);
	// int right = Integer.parseInt(sss[2]);
	// if(((String)resultMap.get(key)).length() < left ||
	// ((String)resultMap.get(key)).length() > right){
	// list.add(rules[0]+"长度必须在"+left+"~"+right+"之间");
	// }
	// }
	// }
	// if(sss[0].equals("lte")){
	// if(rules[2].equals("int")){
	// int ii = Integer.parseInt(sss[1]);
	// if((int)resultMap.get(key) > ii ){
	// list.add(rules[0]+"必须小于等于"+ii);
	// }
	// }
	// if(rules[2].equals("float")){
	// float ii = Float.parseFloat(sss[1]);
	// if((float)resultMap.get(key) > ii){
	// list.add(rules[0]+"必须小于等于"+ii);
	// }
	// }
	// }
	// if(sss[0].equals("lt")){
	// if(rules[2].equals("int")){
	// int ii = Integer.parseInt(sss[1]);
	// if((int)resultMap.get(key) >= ii ){
	// list.add(rules[0]+"必须小于"+ii);
	// }
	// }
	// if(rules[2].equals("float")){
	// float ii = Float.parseFloat(sss[1]);
	// if((float)resultMap.get(key) >= ii){
	// list.add(rules[0]+"必须小于"+ii);
	// }
	// }
	// }
	// if(sss[0].equals("gte")){
	// if(rules[2].equals("int")){
	// int ii = Integer.parseInt(sss[1]);
	// if((int)resultMap.get(key) < ii ){
	// list.add(rules[0]+"必须大于等于"+ii);
	// }
	// }
	// if(rules[2].equals("float")){
	// float ii = Float.parseFloat(sss[1]);
	// if((float)resultMap.get(key) > ii){
	// list.add(rules[0]+"必须大于等于"+ii);
	// }
	// }
	// }
	// if(sss[0].equals("gt")){
	// if(rules[2].equals("int")){
	// int ii = Integer.parseInt(sss[1]);
	// if((int)resultMap.get(key) < ii ){
	// list.add(rules[0]+"必须大于"+ii);
	// }
	// }
	// if(rules[2].equals("float")){
	// float ii = Float.parseFloat(sss[1]);
	// if((float)resultMap.get(key) > ii){
	// list.add(rules[0]+"必须大于"+ii);
	// }
	// }
	// }
	// if(sss[0].equals("e")){
	// if(rules[2].equals("int")){
	// int ii = Integer.parseInt(sss[1]);
	// if((int)resultMap.get(key) != ii ){
	// list.add(rules[0]+"必须等于"+ii);
	// }
	// }
	// if(rules[2].equals("float")){
	// float ii = Float.parseFloat(sss[1]);
	// if((float)resultMap.get(key) != ii){
	// list.add(rules[0]+"必须等于"+ii);
	// }
	// }
	// }
	// if(sss[0].equals("ne")){
	// if(rules[2].equals("int")){
	// int ii = Integer.parseInt(sss[1]);
	// if((int)resultMap.get(key) == ii ){
	// list.add(rules[0]+"不能等于"+ii);
	// }
	// }
	// if(rules[2].equals("float")){
	// float ii = Float.parseFloat(sss[1]);
	// if((float)resultMap.get(key) == ii){
	// list.add(rules[0]+"不能等于"+ii);
	// }
	// }
	// }
	// if(sss[0].equals("unique")){
	// if(rules[2].equals("int") || rules[2].equals("float") ||
	// rules[2].equals("string")){
	// String[] kk = key.split("_");
	// String tableName = tableMap.get(kk[0])[0];
	// int id = Integer.parseInt(param.get(kk[0]+"_id"));
	// String str = resultMap.get(key).toString();
	// if(rules[2].equals("string")){
	// str = "'"+str+"'";
	// }
	// String sql =
	// "select count(1) as count from "+tableName+" where id != "+id+" and "+kk[1]+" = "+str;
	// List<Mapx<String, Object>> listAll = DB.getRunner().query(sql, new
	// MapxListHandler());
	// int count = listAll.get(0).getInt("count");
	// if(count>0){list.add(rules[0]+"已经存在，请填写其他值");}
	// }
	// }
	// if(sss[0].equals("exist")){
	// if(rules[2].equals("int") || rules[2].equals("float") ||
	// rules[2].equals("string")){
	// String tableName = tableMap.get(sss[1])[0];
	// String colName = sss[2];
	// String str = resultMap.get(key).toString();
	// if(rules[2].equals("string")){
	// str = "'"+str+"'";
	// }
	// String sql =
	// "select count(1) as count from "+tableName+" where "+colName+" = "+str;
	// List<Mapx<String, Object>> listAll = DB.getRunner().query(sql, new
	// MapxListHandler());
	// int count = listAll.get(0).getInt("count");
	// if(count==0){list.add(rules[0]+"不存在，请重新填写");}
	// }
	// }
	// }
	//
	// }
	// System.out.println("commoncheck errors "+list);
	// System.out.println("commoncheck resultMap "+resultMap);
	// return list;
	// }
	// /**
	// * 将从数据库中读出的记录转换成页面上的变量
	// * 如果ruleMap中没有定义规则，则不转换对应变量，可能需要手动转换
	// */
	// public static void commonConvert(Map<String,String[]> ruleMap,
	// HashMap<String,String> param, Mapx<String,Object> one){
	// for (Iterator iterator = one.entrySet().iterator(); iterator.hasNext();)
	// {
	// Mapx.Entry<String,Object> entry = (Mapx.Entry<String,Object>)
	// iterator.next();
	// String key = entry.getKey();
	// String[] rules = ruleMap.get(key);
	// if(rules!=null){
	// if(rules[2].equals("int")){
	// param.put(key, one.getIntView(key));
	// }
	// if(rules[2].equals("float")){
	// param.put(key, one.getFloatView(key));
	// }
	// if(rules[2].equals("string")){
	// param.put(key, one.getStringView(key));
	// }
	// if(rules[2].equals("date")){
	// param.put(key, one.getDateView(key));
	// }
	// if(rules[2].equals("money")){
	// param.put(key, one.getMoneyView(key));
	// }
	// }
	// }
	// //如果规则定义的字段，在查询结果里没有，则设置为空字符串
	// for (Iterator iterator = ruleMap.entrySet().iterator();
	// iterator.hasNext();) {
	// Map.Entry<String, String[]> entry = (Map.Entry<String, String[]>)
	// iterator.next();
	// String key = entry.getKey();
	// String value = param.get(key);
	// if(value==null){
	// param.put(key, "");
	// }
	// }
	// System.out.println("commonConvert param "+param);
	// }
	public static String toErrorStr(List<String> errors) {
		String result = "";
		for (Iterator iterator = errors.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			result += string + "<br>";
		}

		return result;

	}

	/**
	 * 根据用户id生成一个token
	 */
	public static String getToken(final int id, final String password)
			throws Exception {
		String str = JSON.toJSONString(new HashMap() {
			{
				put("id", id);
				put("password", password);
				put("time", new Date().getTime() / 1000);
			}
		});
		return DesUtils.encrypt(str);
	}

	/**
	 * 获得用户，或者null
	 */
	public static Mapx<String, Object> getUser(HttpServletRequest request)
			throws Exception {
		// if(request.getRequestURI().indexOf("admin_user_publish.jsp")>-1){//临时，未正式交付，无法创建新用户
		// return null;
		// }
		String token = G.getCookie("token", request);
		if (token == null) {// token不存在
			return null;
		}
		JSONObject json = JSON.parseObject(DesUtils.decrypt(token));
		int time = json.getIntValue("time");
		if (new Date().getTime() / 1000 - time > G.maxTime) {// token超时
			return null;
		}
		int id = json.getIntValue("userid");
		String password = json.getString("password");
		if (password == null) {
			password = " ";
		}
		password = DesUtils.encrypt(password);

		List<Mapx<String, Object>> list = DB
				.getRunner()
				.query("select userid,username,userrole,xingming,password,createtime, "
						+ "phone,touxiangpic,mail,shenhe,status,score,star,lasttime,login_count,shouldstar,tqcode,del from user where userid=? and password=?",
						new MapxListHandler(), id, password);
		if (list == null || list.size() == 0) {
			return null;
		}
		Mapx<String, Object> result = list.get(0);
		System.out.println("result+++"+result);
		return result;
	}

	/**
	 * 
	 */
	// {"高级管理员","客服","服务站","网站运维","财务","普通用户","生产企业","经销商","消费企业"}
	public static List<String[]> getNavs(String usertype) {
		if (usertype.equals("高级管理员")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "用户管理", "admin_user_list.jsp" });
			list.add(new String[] { "服务站管理", "admin_fuwuzhan_list.jsp" });
			list.add(new String[] { "商品管理", "admin_product_list.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			list.add(new String[] { "广告发布", "admin_ad_publish.jsp" });
			list.add(new String[] { "日志统计", "admin_jiaoyi_log_list.jsp" });
			list.add(new String[] { "咨询管理", "admin_zixun_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "系统设置", "admin_setting.jsp" });
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("客服")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "咨询管理", "admin_zixun_list.jsp" });
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("服务站")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("网站运维")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "用户管理", "admin_user_list.jsp" });
			list.add(new String[] { "服务站管理", "admin_fuwuzhan_list.jsp" });
			list.add(new String[] { "商品管理", "admin_product_list.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			list.add(new String[] { "广告发布", "admin_ad_publish.jsp" });
			list.add(new String[] { "日志统计", "admin_jiaoyi_log_list.jsp" });
			list.add(new String[] { "咨询管理", "admin_zixun_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("财务")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "用户管理", "admin_user_list.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "日志统计", "admin_jiaoyi_log_list.jsp" });
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("普通用户")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("生产企业")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "商品管理", "admin_product_list.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("经销商")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "商品管理", "admin_product_list.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			list.add(new String[] { "交易日志", "admin_jiaoyi_log_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		if (usertype.equals("消费企业")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			// list.add(new String[]{"商品管理","admin_product_list.jsp"});
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}

		if (usertype.equals("高级管理员")) {
			List<String[]> list = new ArrayList<String[]>();
			list.add(new String[] { "首页", "admin_index.jsp" });
			list.add(new String[] { "用户管理", "admin_user_list.jsp" });
			list.add(new String[] { "服务站管理", "admin_fuwuzhan_list.jsp" });
			list.add(new String[] { "商品管理", "admin_product_list.jsp" });
			list.add(new String[] { "订单管理", "admin_order_list.jsp" });
			list.add(new String[] { "信息发布", "admin_xinxi_list.jsp" });
			list.add(new String[] { "广告发布", "admin_ad_publish.jsp" });
			list.add(new String[] { "日志统计", "admin_jiaoyi_log_list.jsp" });
			// list.add(new String[]{"站内信","admin_message_list.jsp"});
			list.add(new String[] { "个人中心", "admin_self.jsp" });
			// list.add(new String[]{"退出","admin_exit.jsp"});
			return list;
		}
		return null;
	}

	/**
	 * 获得二级菜单
	 */
	// {"高级管理员","客服","服务站","网站运维","财务","普通用户","生产企业","经销商","消费企业"}
	public static List<String[]> getSubNavs(String usertype, String nav) {
		if (usertype.equals("高级管理员")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("用户管理")) {
				list.add(new String[] { "用户列表", "admin_user_list.jsp" });
				list.add(new String[] { "邀请码列表", "admin_code.jsp" });
			}
			if (nav.equals("服务站管理")) {
				list.add(new String[] { "服务站列表", "admin_fuwuzhan_list.jsp" });
			}
			if (nav.equals("商品管理")) {
				list.add(new String[] { "商品列表", "admin_product_list.jsp" });
				list.add(new String[] { "商品分类列表", "admin_product_type_list.jsp" });
			}
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("信息发布")) {
				list.add(new String[] { "信息列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("广告发布")) {
				list.add(new String[] { "广告发布", "admin_ad_publish.jsp" });
			}
			if (nav.equals("日志统计")) {
				list.add(new String[] { "交易日志", "admin_jiaoyi_log_list.jsp" });
				list.add(new String[] { "订单日志", "admin_order_log_list.jsp" });
				list.add(new String[] { "导出", "admin_daochu.jsp" });
			}
			if (nav.equals("咨询管理")) {
				list.add(new String[] { "咨询列表", "admin_zixun_list.jsp" });
			}
			if (nav.equals("站内信")) {
				list.add(new String[] { "站内信列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("系统设置")) {
				list.add(new String[] { "系统设置", "admin_setting.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
			}
			return list;
		}
		if (usertype.equals("客服")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("咨询管理")) {
				list.add(new String[] { "咨询列表", "admin_zixun_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
			}
			return list;
		}
		if (usertype.equals("服务站")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
			}
			return list;
		}
		if (usertype.equals("网站运维")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("用户管理")) {
				list.add(new String[] { "用户列表", "admin_user_list.jsp" });
			}
			if (nav.equals("服务站管理")) {
				list.add(new String[] { "服务站列表", "admin_fuwuzhan_list.jsp" });
			}
			if (nav.equals("商品管理")) {
				list.add(new String[] { "商品列表", "admin_product_list.jsp" });
				list.add(new String[] { "商品分类列表", "admin_product_type_list.jsp" });
			}
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("信息发布")) {
				list.add(new String[] { "信息列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("广告发布")) {
				list.add(new String[] { "广告发布", "admin_ad_publish.jsp" });
			}
			if (nav.equals("日志统计")) {
				list.add(new String[] { "交易日志", "admin_jiaoyi_log_list.jsp" });
				list.add(new String[] { "订单日志", "admin_order_log_list.jsp" });
				list.add(new String[] { "导出", "admin_daochu.jsp" });
			}
			if (nav.equals("站内信")) {
				list.add(new String[] { "站内信列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("咨询管理")) {
				list.add(new String[] { "咨询列表", "admin_zixun_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
			}
			return list;
		}
		if (usertype.equals("财务")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("用户管理")) {
				list.add(new String[] { "用户列表", "admin_user_list.jsp" });
			}
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("日志统计")) {
				list.add(new String[] { "交易日志", "admin_jiaoyi_log_list.jsp" });
				list.add(new String[] { "订单日志", "admin_order_log_list.jsp" });
				list.add(new String[] { "导出", "admin_daochu.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
			}
			return list;
		}
		if (usertype.equals("普通用户")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("信息发布")) {
				list.add(new String[] { "信息列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("站内信")) {
				list.add(new String[] { "站内信列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
			}
			return list;
		}
		if (usertype.equals("生产企业")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("商品管理")) {
				list.add(new String[] { "商品列表", "admin_product_list.jsp" });
			}
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("信息发布")) {
				list.add(new String[] { "信息列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("站内信")) {
				list.add(new String[] { "站内信列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
				list.add(new String[] { "余额提现", "admin_self.jsp?opt=tixian" });
			}
			return list;
		}
		if (usertype.equals("经销商")) {
			List<String[]> list = new ArrayList<String[]>();
			if (nav.equals("商品管理")) {
				list.add(new String[] { "商品列表", "admin_product_list.jsp" });
			}
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("信息发布")) {
				list.add(new String[] { "信息列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("站内信")) {
				list.add(new String[] { "站内信列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
				list.add(new String[] { "余额提现", "admin_self.jsp?opt=tixian" });
				list.add(new String[] { "余额充值", "admin_self.jsp?opt=chongzhi" });
			}
			return list;
		}
		if (usertype.equals("消费企业")) {
			List<String[]> list = new ArrayList<String[]>();
			// if(nav.equals("商品管理")){
			// list.add(new String[]{"商品列表","admin_product_list.jsp"});
			// }
			if (nav.equals("订单管理")) {
				list.add(new String[] { "订单列表", "admin_order_list.jsp" });
			}
			if (nav.equals("信息发布")) {
				list.add(new String[] { "信息列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("站内信")) {
				list.add(new String[] { "站内信列表", "admin_xinxi_list.jsp" });
			}
			if (nav.equals("个人中心")) {
				list.add(new String[] { "基本信息", "admin_self.jsp?opt=base" });
				list.add(new String[] { "修改密码", "admin_self.jsp?opt=password" });
				list.add(new String[] { "余额提现", "admin_self.jsp?opt=tixian" });
				list.add(new String[] { "余额充值", "admin_self.jsp?opt=chongzhi" });
			}
			return list;
		}
		return null;
	}
	
	public static float starRate(Integer star, Integer shouldStar) {
		if (star == null || shouldStar == null || shouldStar == 0) {
			return 100f;
		}
		return (float) (star * 1.0 / shouldStar) * 100;

	}

	public static String starRateView(Integer star, Integer shouldStar) {
		float f = starRate(star, shouldStar);
		return new DecimalFormat("#.00").format(f) + "%";
	}

	public static BigDecimal calcZongJia(BigDecimal bd, Integer count) {
		bd = bd.multiply(new BigDecimal(count));
		return bd;
	}

	/**
	 * 标记最后活动时间
	 * 
	 * @throws SQLException
	 */
	public static void markLastTime(Mapx<String, Object> user)
			throws SQLException {
		DB.getRunner().update("update admin_user set lasttime=? where id =? ",
				new Date().getTime() / 1000, user.getInt("id"));
	}

	public static void main(String[] args) throws ParseException,
			UnsupportedEncodingException {
		// HashMap<String,String> param = new HashMap<String,String>();
		// param.put("page", "1");
		// param.put("q_i_a_e_id", "123");
		// param.put("q_i_a_g_age", "13");
		// param.put("q_s_a_li_name", "ds");
		// // System.out.println(genSql(param));
		// System.out.println(toUri(param,"page",null));
		// System.out.println(toUri2(param));

		// Map aa=new HashMap();
		// aa.put("sd", 123);
		// aa.put("ss", "中文");
		// aa.put("s2", new String[]{"中","国"});
		// System.out.println(JSON.toJSONString(aa));
		// Map bb= JSON.parseObject(JSON.toJSONString(aa), Map.class);
		// System.out.println(bb);
		// System.out.println(((List)bb.get("s2")).get(0));
		//
		// JSONObject obj = JSON.parseObject(JSON.toJSONString(aa));
		// System.out.println(obj.getJSONArray("s2").get(0));
		// obj.getJSONArray("s2");

		// HashMap<String,String> map =new HashMap<String,String>();
		// map.put("page", "1");
		// List<String[]> list = pageList("/abc", map, 4);
		// System.out.println(JSON.toJSONString(list));

		// String s="a.s.d.fdfb.dc";
		// System.out.println(s.substring(s.lastIndexOf(".")));;

		// String str = URLEncoder.encode("http://a.com?b=1&c=2&n=中文", "utf-8");
		// System.out.println(str);
		// System.out.println(URLDecoder.decode(str, "utf-8"));

		// System.out.println(new Integer(1).equals(null));

		// System.out.println(Integer.parseInt(""));

		// DecimalFormat df = new DecimalFormat("#.00");
		// System.out.println(df.format(12345.6789f));
		// Map<String,String> param=new HashMap<String,String>();
		// for (Iterator iterator = param.entrySet().iterator();
		// iterator.hasNext();) {
		// Map.Entry<String,String> entry = (Map.Entry<String,String>)
		// iterator.next();
		// entry.getKey();
		// entry.getValue();
		// }

		System.out.println(Integer.MAX_VALUE);
	}

	public static BigDecimal getSettingYajin() throws SQLException {
		List<Mapx<String, Object>> list = DB.getRunner().query(
				"select value from admin_setting where name=?",
				new MapxListHandler(), "yajin");
		Mapx<String, Object> one = list.get(0);
		int yajin = one.getInt("value");
		float yajin_ = yajin * 1.0f / 100;
		return new BigDecimal(yajin_);
	}

	public static BigDecimal getSettingQuxiao() throws SQLException {
		List<Mapx<String, Object>> list = DB.getRunner().query(
				"select value from admin_setting where name=?",
				new MapxListHandler(), "quxiao");
		Mapx<String, Object> one = list.get(0);
		int quxiao = one.getInt("value");
		float quxiao_ = quxiao * 1.0f / 100;
		return new BigDecimal(quxiao_);
	}

	public static int getSettingScore() throws SQLException {
		List<Mapx<String, Object>> list = DB.getRunner().query(
				"select value from admin_setting where name=?",
				new MapxListHandler(), "score");
		Mapx<String, Object> one = list.get(0);
		int score = one.getInt("value");
		return score;
	}

	public static int getSettingPrice() throws SQLException {
		List<Mapx<String, Object>> list = DB.getRunner().query(
				"select value from admin_setting where name=?",
				new MapxListHandler(), "price");
		Mapx<String, Object> one = list.get(0);
		int score = one.getInt("value");
		return score;
	}

	/**
	 * 获得url下载前缀
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getUrlPre() throws IOException {
		Properties prop = new Properties();
		InputStream in = G.class.getResourceAsStream("/common.properties");
		prop.load(in);
		Set entrySet = prop.entrySet();
		for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<Object, Object> entry = (Entry<Object, Object>) iterator
					.next();
			if (entry.getKey().equals("urlPre")) {
				return entry.getValue().toString();
			}
		}
		return null;
	}

	/**
	 * 获得保存文件路径
	 * 
	 * @return
	 * @throws IOException
	 */
	public static String getUploadFolderName() throws IOException {
		Properties prop = new Properties();
		InputStream in = G.class.getResourceAsStream("/common.properties");
		prop.load(in);
		Set entrySet = prop.entrySet();
		for (Iterator iterator = entrySet.iterator(); iterator.hasNext();) {
			Entry<Object, Object> entry = (Entry<Object, Object>) iterator
					.next();
			if (entry.getKey().equals("uploadFolderName")) {
				return entry.getValue().toString();
			}
		}
		return null;
	}
}
