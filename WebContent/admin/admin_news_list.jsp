<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.jx.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.apache.commons.dbutils.QueryRunner"%>
<%@ page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<META HTTP-EQUIV="expires" CONTENT="Wed, 26 Feb 1997 08:21:57 GMT">
<title>新闻列表</title>
<link href="img/dy-icon.png" rel="SHORTCUT ICON">
<link rel="stylesheet" href="css/bootstrap.css"/>
<link rel="stylesheet" href="css/backstage.css"/>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script src="js/jquery-1.11.1.min.js"></script>
<script src="layer/layer.js"></script>
<%
//获取当前url
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String jishu = "";
String fileName = "";
String fullName = "";
String dhpage = "";
String searchnr="";
try{
jishu = request.getParameter("jishu");
fileName = request.getParameter("fileName");
fullName = request.getParameter("fullName");
dhpage = request.getParameter("page");
searchnr = request.getParameter("searchnr");
System.out.println("jishu"+request.getParameter("paixu"));
}catch(Exception e){
	
}
//验证用户登陆
Mapx<String,Object> user = G.getUser(request);
String pageType = null;
String userType = null;
//验证用户登陆
String username = (String)session.getAttribute("username");
List<Mapx<String, Object>> useridc= DB.getRunner().query("SELECT userid FROM user where username=?", new MapxListHandler(),username);
int flag=0;
if(username==null){
	%>
	<script type="text/javascript" language="javascript">
			alert("请登录");                                            // 弹出错误信息
			window.location='../front_login.jsp' ;                            // 跳转到登录界面
	</script>
<%
}else{
	flag=1;
}
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式  
System.out.println(df.format(new Date()));// new Date()为获取当前系统时间 
//设置随机数
int  val = (int)(Math.random()*10000)+1;
int tagid=(int)new Date().getTime()/1000+(int)(Math.random()*10000)+1;
int url_canshu;
if(jishu==null){
	url_canshu=Integer.parseInt("1");
}else{	
	url_canshu=Integer.parseInt(jishu);
}
//当前登录用户
//int dluserid=useridc.get(0).getInt("userid");
int dluserid=10196;	
HashMap<String,String> param= G.getParamMap(request); 
//统计菜品总页数
List<Mapx<String,Object>> menupage=DB.getRunner().query("select count(1) as count from article where del=? ", new MapxListHandler(),"0");
int pagetotal=Integer.parseInt(menupage.get(0).getIntView("count"))/10;
System.out.println("总页数="+pagetotal);
//排序类型
String paixu;
if((param.get("paixu")==null)){
	paixu="articleid";
}else if(param.get("paixu").equals("默认")){
	paixu="articleid";
}else if(param.get("paixu").equals("浏览量")){
	paixu="zcount";
}else if(param.get("paixu").equals("zcount")){
	paixu="zcount";
}else{
	paixu="articleid";
}
System.out.println("paixu="+paixu);
String insearch;
if(request.getParameter("searchnr")!=null){
	insearch=new String(request.getParameter("searchnr").getBytes("iso-8859-1"),"utf-8");
}else{
	insearch="";
}
System.out.println("search="+insearch);
//如果urlpage为null
int intdhpage;
if((request.getParameter("page")==null)||request.getParameter("page").equals("null")){
	intdhpage=Integer.parseInt("0");
}else{	
	intdhpage=Integer.parseInt(dhpage);
}
System.out.println("当前页数="+intdhpage);
int plus;
int minus;
//下一页
if(intdhpage==pagetotal){
	plus=pagetotal;
}else{
	plus =intdhpage+1;
}
//上一页
if(intdhpage==0){
	minus =0;	
}else{
	minus =intdhpage-1;
}
//博客列表信息
//CREATE TABLE `article` (
//  `articleid` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
//  `author` int(11) DEFAULT NULL COMMENT '作者',
//  `title` varchar(255) DEFAULT NULL COMMENT '文章标题',
//  `content1` text COMMENT '文章内容',
//  `content2` text,
//  `img1` varchar(255) DEFAULT NULL COMMENT '图片1',
//  `img2` varchar(255) DEFAULT NULL COMMENT '图片2',
//  `img3` varchar(255) DEFAULT NULL COMMENT '图片3',
//  `articletype` int(11) DEFAULT NULL COMMENT '所属文章类型',
//  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
//  `updatetime` datetime DEFAULT NULL COMMENT '最新修改时间',
//  `is_discuss` tinyint(1) DEFAULT NULL COMMENT '是否被评论',
//  `del` int(11) DEFAULT NULL,
//  `zcount` int(11) DEFAULT NULL,
//  `tag1` varchar(255) DEFAULT NULL,
//  `tag2` varchar(255) DEFAULT NULL,
//  `tag3` varchar(255) DEFAULT NULL,
//  `tag4` varchar(255) DEFAULT NULL,
//  `canshu_url` int(11) DEFAULT NULL,
//  `tagid` int(22) DEFAULT NULL,
//  `visitor` varchar(255) DEFAULT NULL,
//  PRIMARY KEY (`articleid`)
//) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;
//设置标题栏信息
String[] colNames={"新闻ID","标题","类型","作者","创建时间","浏览量","操作"};
//博客列表信息
System.out.println("searchnr"+searchnr);
List<Mapx<String,Object>> menu;
if((searchnr==null)||(searchnr=="")||searchnr.equals("null")){
	menu=DB.getRunner().query("select articleid,articletype,title,author,substring(createtime,1,19) as createtime,zcount,tagid from article where del=? order by "+paixu+" desc limit "+intdhpage*10+",10  ", new MapxListHandler(),"0");
}else{
	menu=DB.getRunner().query("select articleid,articletype,title,author,substring(createtime,1,19) as createtime,zcount,tagid from article where del=? and title like '%"+param.get("searchnr")+"%'  ", new MapxListHandler(),"0");
}

//删除
String dhid; 
if((param.get("Action")!=null)&&(param.get("Action").equals("删除"))){
	dhid=new String(request.getParameter("tagid").getBytes("iso-8859-1"),"utf-8");
		DB.getRunner().update("update article set del=?,deltime=? where tagid=?","1",df.format(new Date()),dhid);
		DB.getRunner().update("update news set del=?,deltime=? where tagid=?","1",df.format(new Date()),dhid);
		%>
		<script type="text/javascript" language="javascript">
				alert("删除成功");                                            // 弹出错误信息
				window.location='${pageContext.request.contextPath}/admin/admin_news_list.jsp?page=<%=request.getParameter("page") %>&paixu=<%=param.get("paixu") %>&searchnr=<%= param.get("searchnr")%>' ;                            // 跳转到登录界面
		</script>
	<%
	
}
System.out.println("paixu="+request.getParameter("paixu"));
%> 
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title>新闻列表页</title>  
    <link rel="stylesheet" href="./css/pintuer.css">
    <link rel="stylesheet" href="./css/admin.css">
    <script src="./js/jquery.js"></script>   
</head>
<body style="background-color:#f2f9fd;">
<!-- top start-->
<%@ include file="admin_top.jsp"%>
<!-- top end-->
<!-- leftnav start-->
<%@ include file="admin_left.jsp"%>
<!-- leftnav end-->
<ul class="bread">
  <li><a href="./admin_index.jsp" class="icon-home"> 首页</a></li>
  <li><a href="##" id="a_leader_txt">新闻列表页</a></li>
  <!-- <li><b>当前语言：</b><span style="color:red;">中文</php></span>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换语言：<a href="##">中文</a> &nbsp;&nbsp;<a href="##">英文</a> </li> -->
</ul>
<div class="admin">
  <!-- <iframe scrolling="auto" rameborder="0" src="admin_info.jsp" name="right" width="100%" height="100%"></iframe> -->
  
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 新闻列表</strong> <a href="../admin/admin_news_add.jsp" style="float:right; display:none;">添加新闻</a></div>
    <div class="padding border-bottom">
      <div class="search" style="padding-left:10px;">
        <li> <a class="button border-main icon-plus-square-o" href="../admin/admin_news_add.jsp"> 添加新闻</a> </li>
        <li>搜索：</li>
         <form action="../admin/admin_news_list.jsp"  method="POST" >
          <select name="paixu" class="input"  style="width:90px; line-height:17px; display:inline-block">
            <%if((param.get("paixu")!=null)&&(param.get("paixu").equals("浏览量"))) {%>
				<option>浏览量</option>
				<option>默认</option>
				<%}else if((param.get("paixu")!=null)&&(param.get("paixu").equals("zcount"))) {%>
				<option>浏览量</option>
				<option>默认</option>
				<%}else{ %>
				<option>默认</option>
				<option>浏览量</option>
				<%} %>
          </select>
          <input type="text" placeholder="搜索标题名" name="searchnr" class="input" style="width:250px; line-height:17px;display:inline-block" />
          <input type="submit" value="搜索" name="search" class="button border-main icon-search">
          </form>
      </div>
    </div>
    <!-- 表格 start -->
				<table class="table table-hover text-center">
					<thead>
						<tr>
							<% for(int i=0;i<colNames.length;i++){%>
							<th><%= colNames[i] %></th>
							<%} %>
						</tr>
					</thead>
					<tbody>
					<%for(int j=0;j<menu.size();j++) {%>
					<%List<Mapx<String,Object>> users=DB.getRunner().query("select username from user where userid=?", new MapxListHandler(),menu.get(j).getStringView("author")); %>
						<tr>
							<td style="text-align:left; padding-left:20px;"><%=menu.get(j).getIntView("articleid") %></td>
							<td><%=menu.get(j).getStringView("title") %></td>
							<td><%=menu.get(j).getStringView("articletype") %></td>
							<td><%=users.get(0).getStringView("username") %></td>
							<td><%=menu.get(j).getIntView("createtime") %></td>
							<td><%=menu.get(j).getIntView("zcount") %></td>
							<td>
								<a class="button border-main" href="admin_news_publish.jsp?caiid=<%=menu.get(j).getIntView("articleid")%>&page=<%=intdhpage%>&paixu=<%=paixu%>&searchnr=<%=insearch%>"><span class="icon-edit"></span> 管理</a>
								<form action="admin_news_list.jsp" id="subform<%=j%>" method="POST" style="float:right;">
									<input type="hidden" value="<%=menu.get(j).getIntView("tagid") %>" name="tagid">
									<input type="hidden" value="删除" name="Action">
								</form>
								<a class="button border-red"  name="删除" onclick="test_post<%=j%>()"><span class="icon-trash-o"> 删除</a>
							</td>
						</tr>
<script type="text/javascript">
function test_post<%=j%>() {
if (window.confirm("确认删除  <%=menu.get(j).getStringView("title") %>?")) {
	var testform=document.getElementById("subform<%=j%>");
	testform.action="admin_news_list.jsp?aa=<%=j%>";
	testform.submit();
	} else {
	alert("操作取消");window.location = "${pageContext.request.contextPath}/admin_news_list.jsp?page=<%=request.getParameter("page") %>&paixu=<%=param.get("paixu") %>&searchnr=<%= param.get("searchnr")%>" ;
	}// 跳转到登录界面
}
</script>
<%} %>

					</tbody>
					 <tr>
					 <td colspan="8">
					 <!-- 分页start -->
				<%if((searchnr==null)||(searchnr=="")||(searchnr.equals("null"))){ %>
								<div class="nav-page">
								<%if(pagetotal>4){ %>
								  <ul class="pagination">
								  	<%if(intdhpage>=3){ %>
								    <li id="t1"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=0">首页</a></li>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=minus%>">上一页</a></li>
								    <%}else{ %>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=minus%>">上一页</a></li>
								    <%} %>
								    
								    <%if(intdhpage<3) {%>
								    <li id="t1"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=0">1</a></li>
								    <li id="t2"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=1">2</a></li>
								    <li id="t3"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=2">3</a></li>
								    <%}else if((intdhpage>=3)&&(intdhpage<(pagetotal-3))){ %>
								    <li id="t<%=intdhpage+1%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=intdhpage%>"><%=intdhpage+1%></a></li>
								    <li id="t<%=intdhpage+2%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=intdhpage+1%>"><%=intdhpage+2%></a></li>
								    <li id="t<%=intdhpage+3%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=intdhpage+2%>"><%=intdhpage+3%></a></li>
								    <%}else{ %>
								    <li id="t<%=pagetotal-3%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-4%>"><%=pagetotal-3%></a></li>
								    <li id="t<%=pagetotal-2%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-3%>"><%=pagetotal-2%></a></li>
								    <li id="t<%=pagetotal-1%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-2%>"><%=pagetotal-1%></a></li>
								    <%} %>
								    <li><a>...</a></li>
								    <li id="t<%=pagetotal%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-1%>"><%=pagetotal%></a></li>
								    <li id="t<%=pagetotal+1%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal%>"><%=pagetotal+1%></a></li>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=plus%>">下一页</a></li>
								    <%if(intdhpage!=pagetotal){ %>
								    <li id="t<%=pagetotal+1%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal%>">尾页</a></li>
								    <%} %>
								  </ul>
								  <%}else{ %>
								  <ul class="pagination">
								    <li><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=minus%>">上一页</a></li>
								    <%for(int i=0;i<=pagetotal;i++){ %>
								    <li id="t<%=i+1%>"><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=i%>"><%=i+1%></a></li>
								    <%} %>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_news_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=plus%>">下一页</a></li>
								  </ul>
								  <%} %>
								</div>
<script type="text/javascript">
<%for(int j=0;j<=pagetotal;j++){ %>
	if(<%=intdhpage%>==<%=j%>){
		$("#t<%=j+1%>").addClass("current"); 
	}
	<%} %>
</script>
<%} %>
				<!-- 分页end -->
        </td>
      </tr>
				</table>
				<!-- 表格 end -->
  </div>
</div>
</body>
</html>