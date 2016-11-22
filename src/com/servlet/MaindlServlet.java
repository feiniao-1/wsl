package com.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
 


import javax.servlet.http.HttpSession;



import com.constant.Constant;
import com.utils.Sha1Util;

public class MaindlServlet extends HttpServlet {

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	//网页授权获取用户信息
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//共账号及商户相关参数
		String jine=request.getParameter("jine");
		getServletContext().log("String--jine++++++===="+jine);
		String orderid=request.getParameter("orderid");
		getServletContext().log("main--describe++++++===="+orderid);
		String txt3=request.getParameter("txt3");
		getServletContext().log("String--txt3++++++===="+txt3);
		String txt4=request.getParameter("txt4");
		getServletContext().log("String--txt4++++++===="+txt4);
		//String txt2=new String(request.getParameter("txt2").getBytes("ISO-8859-1"),"UTF-8");
		//txt2=new String(txt2.getBytes("ISO-8859-1"),"GBK" );
		//txt2=new String(txt2.getBytes("ISO-8859-1"),"UTF-8" );
		//txt2=new String(txt2.getBytes("ISO-8859-1"),"gb2312" );
		System.out.println("MainServlet====" + jine);
		String orderNo=Constant.appid+Sha1Util.getTimeStamp();
	//	String backUri = Constant.redirect_uri + "?userId=b88001&orderNo="+orderNo+"&describe=test&money=1780.00";
		String backUri = Constant.redirect_uri + "?userId=b88001&orderNo="+orderNo+"&describe=test&money=1";
		getServletContext().log("backUri--txt1++++++===="+jine);
		try{
		backUri = URLEncoder.encode(backUri,"utf-8");
		}catch(Exception e){
			e.printStackTrace();
		}
		//scope 参数视各自需求而定，这里用scope=snsapi_base 不弹出授权页面直接授权目的只获取统一支付接口的openid
		String url = "https://open.weixin.qq.com/connect/oauth2/authorize?" +
				"appid=" + Constant.appid+
				"&redirect_uri=" +
				 backUri+
				"&response_type=code&scope=snsapi_base&state=123#wechat_redirect";
				
		response.sendRedirect(url);
	}

	/**   
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
}
