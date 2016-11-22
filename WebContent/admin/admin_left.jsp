<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.jx.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.apache.commons.dbutils.QueryRunner"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%//获取url
HashMap<String,String> param1= G.getParamMap(request);
String  urlfootor;
String patha = request.getContextPath();  
String basePatha = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+patha+"/";   
String servletPatha=request.getServletPath();    
String requestURIa=request.getRequestURI();  
System.out.println("path:"+patha);  
System.out.println("basePath:"+basePatha);   
System.out.println("servletPath:"+servletPatha);   
if(request.getQueryString()==null){
	urlfootor=requestURIa;
	
}else{
	urlfootor=requestURIa+"?"+request.getQueryString();
	
} 
if(servletPatha.equals("/admin/admin_news_list.jsp")){
	%>
	<script type="text/javascript">
		$("#span<%=request.getParameter("caid")%>").addClass("on"); 
		$("#a<%=request.getParameter("caid")%>").addClass("on");
	 </script><%
}
%>
<div class="leftnav">

  <div class="leftnav-title"><strong><span class="icon-list"></span>管理页面</strong></div>
   	<li>
    	 <h4><a href="admin_news_list.jsp?caid=1" id="a1">新闻列表页</a></h4>
    </li>
    <li>
    	<h4><a href="admin_product_list.jsp?caid=2">产品列表页</a></h4>
    </li>
</div>
<script type="text/javascript">
$(function(){
  $(".leftnav h2").click(function(){
	  $(this).next().slideToggle(200);	
	  $(this).toggleClass("on"); 
  })
  $(".leftnav h4").click(function(){
	  $(this).next().slideToggle(200);	 
  })
//$(".leftnav ul li a").click(function(){
//	    $("#a_leader_txt").text($(this).text());
//		$(".leftnav ul li a").removeClass("on");
//		$(this).addClass("on");
//})
});
</script>