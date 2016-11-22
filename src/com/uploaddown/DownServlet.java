package com.uploaddown;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.uploaddown.*;

/**
 * 文件下载Servlet
 * 1. 先看下载列表
 * 2. 文件下载
 * @author AdminTH
 *
 */
public class DownServlet extends HttpServlet {
	
	// 要调用的dao
	//private UserDao userDao = new UserDao();
	// 跳转的资源
	private String uri;
	

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		// 先获取操作的类型
		String method = request.getParameter("method");
		// 如果为null，默认进入下载列表
		if (method == null || "".equals(method)){
			method = "list";
		}
		
		// 判断
		if ("list".equals(method)) {
			//  * 1. 先看下载列表
			list(request,response);
		}
		
		else if ("down".equals(method)) {
			//  * 2. 文件下载
			down(request,response);
		}
			
		
	}

	

	// 1. 先看下载列表
	public void list(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		
		try {
			//1.1 调用Dao, 查看所有的下载列表
			List<User> listUser = UserDao.getAll();
			
			//1.2保存到request域中
			request.setAttribute("listUser", listUser);
			
			//1.3查询成功，转发到列表页面
			uri = "/list.jsp";
		} catch (Exception e) {
			request.setAttribute("message", "查看下载列表出错！");
			uri = "/message.jsp";
			e.printStackTrace();  // 方便后台人员查看错误
		}
		
		// 转发
		request.getRequestDispatcher(uri).forward(request, response);
	}
	
	//2.  文件下载
	public void down(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			
			//2.1 获取下载文件的主键
			String id = request.getParameter("id");
			
			//2.2调用dao，根据主键查询
			User user = UserDao.findById(Integer.parseInt(id));
			
			// 用户下载的文件的全名
			String fullName = user.getFullName();
			/*
			// 获取下载的目录路径
			String bathPath = getServletContext().getRealPath("/upload");
			// 得到文件对象
			FileInputStream in = new FileInputStream(new File(bathPath,fullName));
			*/
			
			/*
			 * 设置文件下载的相应头
			 */
			response.setHeader("content-disposition", "attachment;fileName="+URLEncoder.encode(user.getFileName(), "UTF-8"));
			
			// 直接获取文件流
			InputStream in = getServletContext().getResourceAsStream("/upload/" + fullName);
			// 获取输出流
			OutputStream out = response.getOutputStream();
			// 流拷贝
			byte[] b = new byte[1024];
			int len = -1;
			while((len = in.read(b))!=-1){
				out.write(b, 0, len);
			}
			
			// 关闭
			in.close();
			
		} catch (Exception e) {
			request.setAttribute("message", "下载操作出错！");
			uri = "/message.jsp";
			e.printStackTrace();  // 方便后台人员查看错误
			request.getRequestDispatcher(uri).forward(request, response);
		}
		
		
	}
	

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
