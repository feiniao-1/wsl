package com.uploaddown;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jdk.nashorn.internal.ir.RuntimeNode.Request;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.constant.Constant;
import com.uploaddown.*;
/**
 * 处理文件上传
 * @author AdminTH
 *
 */
public class UploadServlet extends HttpServlet {
	
	// 跳转资源
	private String uri;
	String fileName;
	String fullName;
	String url;
	String fhurl;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		url = request.getParameter("url");
		if(url.equals("newspublish1")){
			fhurl="admin/admin_news_publish.jsp";
		}else if(url.equals("productpublish")){
			fhurl="admin/admin_product_publish.jsp";
		}else if(url.equals("newsadd")){
			fhurl="admin/admin_news_add.jsp";
		}else if(url.equals("productadd")){
			fhurl="admin/admin_product_add.jsp";
		}
		System.out.println("url="+url+";attr_file2="+request.getParameter("attr_file2")+";;photoname"+request.getParameter("photoname"));
		try {
			// 1. 创建文件上传工厂类
			DiskFileItemFactory factory = new DiskFileItemFactory();
			// a. 设置临时目录
			File file_temp = new File(getServletContext().getRealPath("/uploadtmp"));
			factory.setRepository(file_temp);
			// 2. servlet文件上传核心处理类
			ServletFileUpload upload = new ServletFileUpload();
			// a. 设置工厂对象
			upload.setFileItemFactory(factory);
			// b. 设置文件名编码 (相当于request编码设置)
			// c. 设置单个文件大小
			upload.setFileSizeMax(10*1024*1024);
			// d. 设置总文件大小
			upload.setSizeMax(50*1024*1024);
			// 判断： 是否为文件上传表单, 即是否指定： enctype="multipart/form-data"
			// 如果指定，返回true
			if (upload.isMultipartContent(request)){
				// 3. 把请求转换为封装了FileItem的List集合
				List<FileItem> items = upload.parseRequest(request);
				// 要封装的对象
				User user = new User();
				// 遍历
				for (FileItem item : items){
					
					// 判断： 是否为普通表单项还是文件上传表单项
					if (item.isFormField()) {
						// 普通表单项
						//---> 表单元素名称 (对象属性名称) 【<input type="text" name="userName"> 】
						String fieldName = item.getFieldName();
						//----> 名称，对应的值
						String value = item.getString("UTF-8");
						
						// BeanUtils组件设置值
						BeanUtils.copyProperty(user, fieldName, value);
					}
					
					else {
						// 文件上传表单项
						// 获取文件名
						fileName = item.getName();
						// 生成UUID
						String uuid = UUID.randomUUID().toString();
						// 全球唯一文件名
						fullName = uuid + "." + fileName;
						
						// 获取上传目录
						String bathPath = getServletContext().getRealPath("/upload");
						System.out.println(bathPath+";;fullName==="+fullName);
						// 文件对象 (以.隔开uuid与文件名)
						File file = new File(bathPath,fullName);
						// 上传
						item.write(file);
						// 删除临时文件
						item.delete();
						
						//---------db----------
						user.setFileName(fileName);  // 设置文件名
						user.setFullName(fullName);  // 设置文件，全名
						System.out.println("fileName=="+fileName+";;fullName==="+fullName);
						// dao 实例
						UserDao userDao = new UserDao();
						// 保存到数据库
						userDao.save(user);
						// 跳转
						uri = "/success.jsp";
					}
				}
				
			} else {
				request.setAttribute("message", "文件上传失败，请检查文件表单！");
				uri = "/message.jsp";
			}
		} catch (Exception e) {
			//request.setAttribute("message", "文件上传Servlet处理失败，请联系管理员！");
			//uri = "/upload.jsp?fileName="+fileName;
			if(url.equals("newsadd")){
				response.sendRedirect(fhurl+"?fileName="+fileName+"&fullName"+request.getParameter("shuzi")+"="+fullName+"&shuzi="+request.getParameter("shuzi")+"&url=newsadd");
			}else if(url.equals("productadd")){
				response.sendRedirect(fhurl+"?fileName="+fileName+"&fullName"+request.getParameter("shuzi")+"="+fullName+"&shuzi="+request.getParameter("shuzi")+"&url=productadd");
			}else if(url.equals("productpublish")){
				response.sendRedirect(fhurl+"?fileName="+fileName+"&fullName"+request.getParameter("shuzi")+"="+fullName+"&shuzi="+request.getParameter("shuzi")+"&caiid="+request.getParameter("caiid")+"&page="+request.getParameter("page")+"&paixu="+request.getParameter("paixu")+"&searchnr="+request.getParameter("searchnr"));
			}else if(url.equals("newspublish1")){
				response.sendRedirect(fhurl+"?fileName="+fileName+"&upimg"+request.getParameter("shuzi")+"="+fullName+"&shuzi="+request.getParameter("shuzi")+"&caiid="+request.getParameter("caiid")+"&page="+request.getParameter("page")+"&paixu="+request.getParameter("paixu")+"&searchnr="+request.getParameter("searchnr"));
			}

			e.printStackTrace();
		}
		
		// 跳转
		//request.getRequestDispatcher(uri).forward(request, response);

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.doGet(request, response);
	}

}
