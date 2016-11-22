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
List<Mapx<String,Object>> menupage=DB.getRunner().query("select count(1) as count from product where (del is NULL or del <>1) ", new MapxListHandler());
int pagetotal=Integer.parseInt(menupage.get(0).getIntView("count"))/10;
System.out.println("总页数="+pagetotal);
//排序类型
String paixu;
if((param.get("paixu")==null)){
	paixu="id";
}else if(param.get("paixu").equals("默认")){
	paixu="id";
}else if(param.get("paixu").equals("浏览量")){
	paixu="seecount";
}else if(param.get("paixu").equals("seecount")){
	paixu="seecount";
}else{
	paixu="id";
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
//产品列表信息
/*
CREATE TABLE `product` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '产品id',
  `name` varchar(255) DEFAULT NULL COMMENT '商品名字',
  `createtime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatetime` datetime DEFAULT NULL COMMENT '更新时间',
  `img` varchar(255) DEFAULT NULL COMMENT '图片',
  `param1` varchar(255) DEFAULT NULL COMMENT '产品参数1',
  `value1` varchar(255) DEFAULT NULL COMMENT '产品参数值1',
  `param2` varchar(255) DEFAULT NULL,
  `value2` varchar(255) DEFAULT NULL,
  `param3` varchar(255) DEFAULT NULL,
  `value3` varchar(255) DEFAULT NULL,
  `param4` varchar(255) DEFAULT NULL,
  `value4` varchar(255) DEFAULT NULL,
  `param5` varchar(255) DEFAULT NULL,
  `value5` varchar(255) DEFAULT NULL,
  `param6` varchar(255) DEFAULT NULL,
  `value6` varchar(255) DEFAULT NULL,
  `param7` varchar(255) DEFAULT NULL,
  `value7` varchar(255) DEFAULT NULL,
  `param8` varchar(255) DEFAULT NULL,
  `value8` varchar(255) DEFAULT NULL,
  `param9` varchar(255) DEFAULT NULL,
  `value9` varchar(255) DEFAULT NULL,
  `param10` varchar(255) DEFAULT NULL,
  `value10` varchar(255) DEFAULT NULL,
  `content` text COMMENT '图文详情',
  `seecount` int(11) DEFAULT NULL COMMENT '浏览量',
  `lovecount` int(11) DEFAULT NULL COMMENT '点赞量',
  `sharecount` int(11) DEFAULT NULL COMMENT '分享量',
  `pricemin` decimal(15,2) DEFAULT NULL COMMENT '最小价格',
  `pricemax` decimal(15,2) DEFAULT NULL COMMENT '最大价格',
  `user` int(11) DEFAULT NULL COMMENT '所属用户',
  `status` varchar(255) DEFAULT NULL COMMENT '产品状态',
  `type1` int(11) DEFAULT NULL COMMENT '所属1级分类',
  `type2` int(11) DEFAULT NULL COMMENT '所属2级分类',
  `type3` int(11) DEFAULT NULL COMMENT '所属3级分类',
  `usertype` varchar(255) DEFAULT NULL COMMENT '所属用户类别',
  `del` int(11) DEFAULT NULL COMMENT '状态',
  `base_weight` double DEFAULT '0' COMMENT '基本权重',
  `manual_weight` double DEFAULT '0' COMMENT '人工权重',
  `canshu_url` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
*/
//设置标题栏信息
String[] colNames={"产品ID","产品名称","缩略图","用户名","创建时间","浏览量","最低价格","最高价格","一级分类","二级分类","三级分类","权重","操作"};
//博客列表信息
System.out.println("searchnr"+searchnr);
List<Mapx<String,Object>> menu;
if((searchnr==null)||(searchnr=="")||searchnr.equals("null")){
	menu=DB.getRunner().query("select id,name,img,substring(createtime,1,19) as createtime,user,seecount,pricemin,pricemax,type1,type2,type3,base_weight from product where (del is NULL or del <>1) order by "+paixu+" desc limit "+intdhpage*10+",10  ", new MapxListHandler());
}else{
	menu=DB.getRunner().query("select id,name,img,substring(createtime,1,19) as createtime,user,seecount,pricemin,pricemax,type1,type2,type3,base_weight from product where (del is NULL or del <>1) and name like '%"+param.get("searchnr")+"%'  ", new MapxListHandler());
}

//删除
String dhid; 
if((param.get("Action")!=null)&&(param.get("Action").equals("删除"))){
	dhid=new String(request.getParameter("id").getBytes("iso-8859-1"),"utf-8");
		DB.getRunner().update("update product set del=?,deltime=? where id=?","1",df.format(new Date()),dhid);
		%>
		<script type="text/javascript" language="javascript">
				alert("删除成功");                                            // 弹出错误信息
				window.location='${pageContext.request.contextPath}/admin/admin_product_list.jsp?page=<%=request.getParameter("page") %>&paixu=<%=param.get("paixu") %>&searchnr=<%= param.get("searchnr")%>' ;                            // 跳转到登录界面
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
    <title>产品列表页</title>  
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
  <li><a href="##" id="a_leader_txt">产品列表页</a></li>
  <!-- <li><b>当前语言：</b><span style="color:red;">中文</php></span>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换语言：<a href="##">中文</a> &nbsp;&nbsp;<a href="##">英文</a> </li> -->
</ul>
<div class="admin">
  <!-- <iframe scrolling="auto" rameborder="0" src="admin_info.jsp" name="right" width="100%" height="100%"></iframe> -->
  
  <div class="panel admin-panel">
    <div class="panel-head"><strong class="icon-reorder"> 产品列表</strong> <a href="../admin/admin_product_add.jsp" style="float:right; display:none;">添加产品</a></div>
    <div class="padding border-bottom">
      <div class="search" style="padding-left:10px;">
        <li> <a class="button border-main icon-plus-square-o" href="../admin/admin_product_add.jsp"> 添加产品</a> </li>
        <li>搜索：</li>
         <form action="../admin/admin_product_list.jsp"  method="POST" >
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
					<%List<Mapx<String,Object>> users=DB.getRunner().query("select username from user where userid=?", new MapxListHandler(),menu.get(j).getStringView("user")); %>
						<tr>
							<td style="text-align:center; padding-left:20px;"><%=menu.get(j).getIntView("id") %></td>
							<td><%=menu.get(j).getStringView("name") %></td>
							<td><img alt="" src="../<%=menu.get(j).getStringView("img") %>" width="50px" height="50px"></td>
							<td><%=users.get(0).getStringView("username") %></td>
							<td><%=menu.get(j).getIntView("createtime") %></td>
							<td><%=menu.get(j).getIntView("seecount") %></td>
							<td><%=menu.get(j).getIntView("pricemin") %></td>
							<td><%=menu.get(j).getIntView("pricemax") %></td>
							<td><%=menu.get(j).getIntView("type1") %></td>
							<td><%=menu.get(j).getIntView("type2") %></td>
							<td><%=menu.get(j).getIntView("type3") %></td>
							<td><%=menu.get(j).getIntView("base_weight") %></td>
							<td>
								<a class="button border-main" href="admin_product_publish.jsp?caiid=<%=menu.get(j).getIntView("articleid")%>&page=<%=intdhpage%>&paixu=<%=paixu%>&searchnr=<%=insearch%>"><span class="icon-edit"></span> 管理</a>
								<form action="admin_news_list.jsp" id="subform<%=j%>" method="POST" style="float:right;">
									<input type="hidden" value="<%=menu.get(j).getIntView("id") %>" name="id">
									<input type="hidden" value="删除" name="Action">
								</form>
								<a class="button border-red"  name="删除" onclick="test_post<%=j%>()"><span class="icon-trash-o"> 删除</a>
							</td>
						</tr>
<script type="text/javascript">
function test_post<%=j%>() {
if (window.confirm("确认删除  <%=menu.get(j).getStringView("title") %>?")) {
	var testform=document.getElementById("subform<%=j%>");
	testform.action="admin_product_list.jsp?aa=<%=j%>";
	testform.submit();
	} else {
	alert("操作取消");window.location = "${pageContext.request.contextPath}/admin_product_list.jsp?page=<%=request.getParameter("page") %>&paixu=<%=param.get("paixu") %>&searchnr=<%= param.get("searchnr")%>" ;
	}// 跳转到登录界面
}
</script>
<%} %>

					</tbody>
					 <tr >
					 <td colspan="13" >
					 <!-- 分页start -->
				<%if((searchnr==null)||(searchnr=="")||(searchnr.equals("null"))){ %>
								<div class="nav-page">
								<%if(pagetotal>4){ %>
								  <ul class="pagination">
								  	<%if(intdhpage>=3){ %>
								    <li id="t1"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=0">首页</a></li>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=minus%>">上一页</a></li>
								    <%}else{ %>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=minus%>">上一页</a></li>
								    <%} %>
								    
								    <%if(intdhpage<3) {%>
								    <li id="t1"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=0">1</a></li>
								    <li id="t2"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=1">2</a></li>
								    <li id="t3"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=2">3</a></li>
								    <%}else if((intdhpage>=3)&&(intdhpage<(pagetotal-3))){ %>
								    <li id="t<%=intdhpage+1%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=intdhpage%>"><%=intdhpage+1%></a></li>
								    <li id="t<%=intdhpage+2%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=intdhpage+1%>"><%=intdhpage+2%></a></li>
								    <li id="t<%=intdhpage+3%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=intdhpage+2%>"><%=intdhpage+3%></a></li>
								    <%}else{ %>
								    <li id="t<%=pagetotal-3%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-4%>"><%=pagetotal-3%></a></li>
								    <li id="t<%=pagetotal-2%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-3%>"><%=pagetotal-2%></a></li>
								    <li id="t<%=pagetotal-1%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-2%>"><%=pagetotal-1%></a></li>
								    <%} %>
								    <li><a>...</a></li>
								    <li id="t<%=pagetotal%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal-1%>"><%=pagetotal%></a></li>
								    <li id="t<%=pagetotal+1%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal%>"><%=pagetotal+1%></a></li>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=plus%>">下一页</a></li>
								    <%if(intdhpage!=pagetotal){ %>
								    <li id="t<%=pagetotal+1%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=pagetotal%>">尾页</a></li>
								    <%} %>
								  </ul>
								  <%}else{ %>
								  <ul class="pagination">
								    <li><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=minus%>">上一页</a></li>
								    <%for(int i=0;i<=pagetotal;i++){ %>
								    <li id="t<%=i+1%>"><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=i%>"><%=i+1%></a></li>
								    <%} %>
								    <li><a href="${pageContext.request.contextPath}/admin/admin_product_list.jsp?paixu=<%=param.get("paixu") %>&page=<%=plus%>">下一页</a></li>
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