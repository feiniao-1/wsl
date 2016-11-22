<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.jx.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.apache.commons.dbutils.QueryRunner"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	HashMap<String,String> param= G.getParamMap(request);
System.out.println("indexurl="+request.getParameter("url")); 
String srcurl;
if(param.get("url")==null){
	srcurl="../admin/admin_info.jsp";
}else if(param.get("url").equals("info")){
	srcurl="../admin/admin_info.jsp";
}else{
	srcurl="../admin/admin_info.jsp";
}
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
<meta name="renderer" content="webkit">
<title>后台管理中心</title>
<link rel="stylesheet" href="./css/pintuer.css">
<link rel="stylesheet" href="./css/admin.css">
<script src="./js/jquery.js"></script>
</head>
<body style="background-color: #f2f9fd;">
	<!-- top start-->
	<%@ include file="admin_top.jsp"%>
	<!-- top end-->
	<!-- leftnav start-->
	<%@ include file="admin_left.jsp"%>
	<!-- leftnav end-->
	<ul class="bread">
		<li><a href="{:U('Index/info')}" target="right" class="icon-home">首页</a></li>
		<li><a href="##" id="a_leader_txt">网站信息</a></li>
		<li><b>当前语言：</b><span style="color: red;">中文</php></span>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换语言：<a href="##">中文</a> &nbsp;&nbsp;
			<a href="##">英文</a>
			&nbsp;&nbsp;
			<a href="##">德文</a></li>
	</ul>
	
		
</body>


		<div class="admin">
			<div class="row">
				<div class="col-md-9" style="padding-top:200px;">
	             <center>
					<h1>
						<b>欢迎登陆寰宇汇智后台管理界面</b>
					</h1>
					<p style="padding-top:10px;">
					<br> © Copyright © 2008-2016.Localhost All rights reserved.
					Designed By David <br> 京ICP备15008545号
					</p>
					</center>
				</div>
			</div>
		</div>

</html>