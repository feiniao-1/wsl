package com.jx.action;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jx.common.FM;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;

/**
 * @author Administrator 基类，包含一些常用的方法。
 *         http请求过来后，FenFa.java就是调用BaseAction.java的各种子类来处理。
 *         除了BaseAction,其他以Action结尾的都是它的子类。
 */
public class BaseAction {
	protected HttpServletRequest req = null;
	protected HttpServletResponse res = null;
	protected String uri = null;
	private BufferedWriter bw = null;

	public void init(HttpServletRequest req, HttpServletResponse res, String uri)
			throws IOException {
		this.req = req;
		this.res = res;
		this.uri = uri;
	}

	public BufferedWriter getWriter() throws IOException {
		if (bw != null) {
			return bw;
		}
		bw = new BufferedWriter(res.getWriter());
		return bw;
	}

	public void render(String templateName, Map rootMap) throws IOException,
			TemplateException {
		this.res.setContentType("text/html;charset=utf-8");
		// 注册静态类
		BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
		TemplateHashModel staticModels = wrapper.getStaticModels();
		TemplateHashModel g = (TemplateHashModel) staticModels
				.get("com.jx.common.G");

		Template t = FM.getTemplate(templateName);
		// System.out.println(t);
		rootMap.put("G", g);
		t.process(rootMap, getWriter());
		getWriter().close();
	}

	public void go() throws Exception {

	}
}
