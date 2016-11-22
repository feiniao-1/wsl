package com.admin;

import java.util.ArrayList;
import java.util.List;

import com.jx.common.Mapx;

public class list {
	
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
		return null;
	}
}
