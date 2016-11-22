package com.jx;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx.action.BaseAction;
import com.jx.action.IndexAction;
import com.jx.common.DB;
import com.jx.common.DesUtils;
import com.jx.common.FM;

import freemarker.template.TemplateModelException;

/**
 * 分发servlet，全局的请求基本都到这个servlet，然后分发处理。
 */
public class FenFa extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Class<?>> mapping = new HashMap<String, Class<?>>();

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		// 初始化数据库
		DB.init();
		// 初始化freemarker
		FM.init(getServletContext());
		// 初始化加解密工具
		try {
			DesUtils.init();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 初始化url映射
		mapping.put("/", IndexAction.class);
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		String uri = req.getRequestURI();
		try {
			// 通过不同的uri找到对应的处理类，进行处理
			BaseAction action = (BaseAction) mapping.get(uri).newInstance();
			action.init(req, res, uri);
			action.go();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
