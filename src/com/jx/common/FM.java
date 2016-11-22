package com.jx.common;

import java.io.IOException;

import javax.servlet.ServletContext;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;

/**
 * @author Administrator freemarker操作类
 */
public class FM {
	private static Configuration cfg = null;

	public static void init(ServletContext servletContext) {
		if (cfg != null) {
			return;
		}
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(servletContext,
				"WEB-INF/templates");

	}

	public static Template getTemplate(String name) throws IOException {

		return cfg.getTemplate(name, "utf-8");
	}

}
