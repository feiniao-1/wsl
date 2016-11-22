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
<title>新闻编辑页</title>
<link href="img/dy-icon.png" rel="SHORTCUT ICON">
    <link rel="stylesheet" href="./css/pintuer.css">
    <link rel="stylesheet" href="./css/admin.css">
<script src="./js/jquery.js"></script>
<script src="./js/pintuer.js"></script>
<script type="text/javascript" src="../js/jquery-1.8.0.min.js"></script> 
	<script type="text/javascript" src="../ckeditor/ckeditor.js"></script>  
    <script type="text/javascript" src="../ckeditor/config.js"></script>  
    <script type="text/javascript">
	    $(document).ready(function(){  
	    	CKEDITOR.replace('content1'); 
	    	CKEDITOR.replace('content2'); 
	    });  
    </script>   
<%
//获取当前url
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String jishu = "";
String fileName = "";
String upimg1 = "";
String upimg2 = "";
String upydimg1 = "";
String upydimg2 = "";
String caiid = "";
String shuzi = "";
try{
jishu = request.getParameter("jishu");
fileName = request.getParameter("fileName");
upimg1 = request.getParameter("upimg1");
upimg2 = request.getParameter("upimg2");
upydimg1 = request.getParameter("upimg3");
upydimg2 = request.getParameter("upimg4");
caiid = request.getParameter("caiid");
shuzi = request.getParameter("shuzi");

System.out.println("caiid"+request.getParameter("caiid"));
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
if(fileName==null){
	session.removeAttribute("upimg1");
	session.removeAttribute("upimg2");
	session.removeAttribute("upydimg1");
	session.removeAttribute("upydimg2");
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
String searchnr;
if(request.getParameter("insearch")==null){
	searchnr=null;
}else if(request.getParameter("insearch")==""){
	searchnr="";
}else{
	searchnr=new String(request.getParameter("insearch").getBytes("iso-8859-1"),"utf-8");
}

//当前登录用户
//int dluserid=useridc.get(0).getInt("userid");
int dluserid=10196;	
HashMap<String,String> param= G.getParamMap(request); 
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
//新闻信息
List<Mapx<String,Object>> menu=DB.getRunner().query("select articleid,articletype,author,title,titlejs,content1,content2,img1,img2,ydimg1,ydimg2,articletype,substring(createtime,1,19) as createtime,substring(updatetime,1,19) as updatetime,zcount,tag1,tag2,tag3,tag4,tagid,origin  from article where del=? and articleid=?", new MapxListHandler(),"0",caiid);
System.out.println(menu);
//显示该博客的随机数信息
List<Mapx<String,Object>> showdiscuss1 = DB.getRunner().query("select canshu_url as canshu_url from article where  articleid=? ",new MapxListHandler(),caiid);
System.out.println("showdiscuss1=="+showdiscuss1+"caiid="+caiid);
int canshu_url=showdiscuss1.get(0).getInt("canshu_url");
//编辑保存博客信息
String title;
String titlejs;
String content1;
String content2;
String tag1;
String tag2;
String tag3;
String tag4;
String img1;
String img2;
String ydimg1;
String ydimg2;
String leixing;
String origin;
String zcount;
if(url_canshu!=canshu_url){
if(param.get("Action")!=null && param.get("Action").equals("编辑文章")){
	title=param.get("title");
	titlejs=param.get("titlejs");
	content1=param.get("content1");
	content2=param.get("content2");
	origin=new String(request.getParameter("origin").getBytes("iso-8859-1"),"utf-8");
	tag1=param.get("tag1");
	tag2=param.get("tag2");
	tag3=param.get("tag3");
	tag4=param.get("tag4");
	if((request.getParameter("zcount")==null)||(request.getParameter("zcount").equals(""))){
		zcount="100";
	}else{
		zcount=param.get("zcount");
	}
	leixing=new String(request.getParameter("leixing").getBytes("iso-8859-1"),"utf-8");
	if((String)session.getAttribute("upimg1")==null){
		img1=menu.get(0).getStringView("img1");
	}else{
		img1="upload/"+(String)session.getAttribute("upimg1");
	}
	if((String)session.getAttribute("upimg2")==null){
		img2=menu.get(0).getStringView("img2");
	}else{
		img2="upload/"+(String)session.getAttribute("upimg2");
	}
	if((String)session.getAttribute("upydimg1")==null){
		ydimg1=menu.get(0).getStringView("ydimg1");
	}else{
		ydimg1="upload/"+(String)session.getAttribute("upydimg1");
	}
	if((String)session.getAttribute("upydimg2")==null){
		ydimg2=menu.get(0).getStringView("ydimg2");
	}else{
		ydimg2="upload/"+(String)session.getAttribute("upydimg2");
	}
		DB.getRunner().update("update article set title=?,titlejs=?,content1=?,content2=?,tag1=?,tag2=?,tag3=?,tag4=?,updatetime=?,canshu_url=?,img1=?,img2=?,ydimg1=?,ydimg2=? ,articletype=?,origin=?,zcount=? where articleid=?",title,titlejs,content1,content2,tag1,tag2,tag3,tag4,df.format(new Date()),url_canshu,img1,img2,ydimg1,ydimg2,leixing,origin,zcount,caiid);
		DB.getRunner().update("update news set title=?,titlejs=?,content=?,newstype=?,img1=?,ydimg1=?,updatetime=?,type=?,origin=?,count=? where tagid=?",title,titlejs,content1,"boke",img1,ydimg1,df.format(new Date()),leixing,origin,zcount,menu.get(0).getIntView("tagid"));
		session.removeAttribute("upimg1");
		session.removeAttribute("upimg2");
		session.removeAttribute("upydimg1");
		session.removeAttribute("upydimg2");
		%>
		<script type="text/javascript" language="javascript">
				alert("修改成功");                                            // 弹出提示信息
				window.location='../admin/admin_news_list.jsp?page=<%=request.getParameter("page") %>&paixu=<%=param.get("paixu") %>&searchnr=<%= param.get("searchnr")%>' ;                           
		</script>
	<%
}else{
}
}
%> 
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
  <li><a href="##" id="a_leader_txt">新闻编辑页</a></li>
  <!-- <li><b>当前语言：</b><span style="color:red;">中文</php></span>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换语言：<a href="##">中文</a> &nbsp;&nbsp;<a href="##">英文</a> </li> -->
</ul>
<div class="admin">
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>编辑内容</strong></div>
  <div class="imgs" style="margin-left:100px;">
  <!-- 图片上传start  -->
     <div class="form-group">
        		<h5 class="mb10">PC封面图<span style="color:red;">*(240*180)</span></h5> 
					<form action="${pageContext.request.contextPath }/uploadServlet?url=newspublish1&caiid=<%= caiid%>&shuzi=1&page=<%=request.getParameter("page") %>&paixu=<%=request.getParameter("paixu") %>&searchnr=<%= param.get("searchnr")%>" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" > 
						<%if(shuzi!=null&&shuzi.equals("1")){
							if(upimg1==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("upimg1", upimg1);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("upimg1")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %> 	
							</div>
				  	 </form>
				  	 <br><br><br>
						 <%if((String)session.getAttribute("upimg1")!=null){ %>
							 	<img alt="" src="../upload/<%=(String)session.getAttribute("upimg1") %>" style="width:200px!important;" height="150px">
							 <%}else{ %>
							 	<img alt="" src="../<%=menu.get(0).getStringView("img1") %>" style="width:50px!important;" height="50px">
							 <%} %>
						<h5 class="mb10">PC详情图片<span style="color:red;">*(798*532)</span></h5> 
						<form action="${pageContext.request.contextPath }/uploadServlet?url=newspublish1&caiid=<%= caiid%>&shuzi=2&page=<%=request.getParameter("page") %>&paixu=<%=request.getParameter("paixu") %>&searchnr=<%= param.get("searchnr")%>" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" > 	
						<%if(shuzi!=null&&shuzi.equals("2")){
							if(upimg2==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("upimg2", upimg2);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("upimg2")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %>
						</div>
				  	 </form>
				  	 <br><br><br>
						 <%if((String)session.getAttribute("upimg2")!=null){ %>
							 	<img alt="" src="../upload/<%=(String)session.getAttribute("upimg2") %>" style="width:220px!important;" height="150px">
							 <%}else{ %>
							 	<img alt="" src="../<%=menu.get(0).getStringView("img2") %>" style="width:50px!important;" height="50px">
							 <%} %>
					<h5 class="mb10">移动端封面图<span style="color:red;">*</span></h5> 
					<form action="${pageContext.request.contextPath }/uploadServlet?url=newspublish1&caiid=<%= caiid%>&shuzi=3&page=<%=request.getParameter("page") %>&paixu=<%=request.getParameter("paixu") %>&searchnr=<%= param.get("searchnr")%>" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" > 
						<%if(shuzi!=null&&shuzi.equals("3")){
							if(upydimg1==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("upydimg1", upydimg1);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("upydimg1")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %>
						</div>
				  	 </form>
				  	 <br><br><br>
						 <%if((String)session.getAttribute("upydimg1")!=null){ %>
							 	<img alt="" src="../upload/<%=(String)session.getAttribute("upydimg1") %>" style="width:120px!important;" height="90px">
							 <%}else{ %>
							 	<img alt="" src="../<%=menu.get(0).getStringView("ydimg1") %>" style="width:50px!important;" height="50px">
							 <%} %>
					<h5 class="mb10">移动端详情图片<span style="color:red;">*</span></h5> 
					<form action="${pageContext.request.contextPath }/uploadServlet?url=newspublish1&caiid=<%= caiid%>&shuzi=4&page=<%=request.getParameter("page") %>&paixu=<%=request.getParameter("paixu") %>&searchnr=<%= param.get("searchnr")%>" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" >   
						<%if(shuzi!=null&&shuzi.equals("4")){
							if(upydimg2==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("upydimg2", upydimg2);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("upydimg2")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %>
						</div>
				  	 </form>
				  	 <br><br><br>
						 <%if((String)session.getAttribute("upydimg2")!=null){ %>
							 	<img alt="" src="../upload/<%=(String)session.getAttribute("upydimg2") %>" style="width:220px!important;" height="150px">
							 <%}else{ %>
							 	<img alt="" src="../<%=menu.get(0).getStringView("ydimg2") %>" style="width:50px!important;" height="50px">
							 <%} %>
				</div>
	<!-- 图片上传end -->
  </div>
  <div class="body-content">
  <br>
    <form class="form-x" id="form_tj" action="${pageContext.request.contextPath }/admin/admin_news_publish.jsp?jishu=<%=val%>&caiid=<%=caiid %>&fileName=tijiao&page=<%=request.getParameter("page") %>&paixu=<%=request.getParameter("paixu") %>&searchnr=<%= param.get("searchnr")%>" method="post">  
    	<div class="form-group">
    		<div class="label">
            <label>ID：</label>
            </div>
            <div class="field">
			<input type="text" class="input" 
				readOnly="true" value="<%= menu.get(0).getIntView("articleid") %>" name="id" style="width:7%;">
			</div>
		</div>
		<div class="form-group">
			<div class="label">
            <label>作者：</label>
            </div>
            <div class="field">
			<input type="text" class="input" 
				name="author" readOnly="true"
				value="<%= menu.get(0).getStringView("author") %>" style="width:7%;">
				</div>
		</div>  
        <div class="form-group">
          <div class="label">
            <label>标题分类：</label>
          </div>
          <div class="field">
            <select name="leixing" class="input w50">
            <option><%=menu.get(0).getStringView("articletype") %></option>
				<option>热门</option>
				<option>美食</option>
				<option>体育</option>		
				<option>娱乐</option>
				<option>科技</option>
			</select>
          </div>
        </div>
        <div class="form-group">
          <div class="label">
            <label>标题：</label>
          </div>
          <div class="field">
          <input type="text" Name="title" class="input" value="<%= menu.get(0).getStringView("title") %>" style="width:50%;"><span style="color:red;">*(最多20字)</span>
          </div>
        </div>
        <div class="form-group">
          <div class="label">
            <label>标题简述：</label>
          </div>
          <div class="field">
          <input type="text" Name="titlejs" class="input" value="<%= menu.get(0).getStringView("titlejs") %>" style="width:50%;"><span style="color:red;">*(最多20字)</span>
          </div>
        </div>
      <div class="form-group">
        <div class="label">
          <label>描述：</label>
        </div>
        <div class="field">
          <textarea name="content1" ><%=menu.get(0).getStringView("content1") %></textarea>
          <div class="tips"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>内容：</label>
        </div>
        <div class="field">
          <textarea name="content2"><%=menu.get(0).getStringView("content2") %></textarea>
          <div class="tips"></div>
        </div>
      </div>
      
      <div class="form-group">
	     <div class="label">
	         <label>创建时间：</label>
	       </div>
	       <div class="field">
	         <input type="text" class="input" name="createtime" value="<%=menu.get(0).getIntView("createtime") %>"  style="width:12%;" readOnly="true"/>
	         <div class="tips"></div>
	       </div>
	  </div>
	  <div class="form-group">
		   <div class="label">
	          <label>更新时间：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input" name="updatetime" value="<%=menu.get(0).getIntView("updatetime") %>" style="width:12%;" readOnly="true"/>
	          <div class="tips"></div>
	        </div>
	  </div>
	  <div class="form-group">
		   <div class="label">
	          <label>浏览量：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input" name="zcount" value="<%=menu.get(0).getIntView("zcount") %>" />
	          <div class="tips"></div>
	        </div>
	  </div>
	  
      <div class="form-group">
	        <div class="label">
	          <label>新闻出处：</label>
	        </div>
	        <div class="field">
	          <input type="text" class="input" name="origin" value="<%= menu.get(0).getStringView("origin") %>"/>
	          <div class="tips"></div>
	        </div>
      </div>
      <div class="form-group">
	        <div class="label">
	          <label>词条标签：</label>
	        </div>
	        <div class="field">
	        <input type="text" Name="tag1"  value="<%=menu.get(0).getStringView("tag1") %>" style="width:80px;">
			<input type="text" Name="tag2"  value="<%=menu.get(0).getStringView("tag2") %>" style="width:80px;">
			<input type="text" Name="tag3"  value="<%=menu.get(0).getStringView("tag3") %>" style="width:80px;">
			<input type="text" Name="tag4"  value="<%=menu.get(0).getStringView("tag4") %>" style="width:80px;">
	        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label></label>
        </div>
        <div class="field">
          <input type="submit" Name="Action" value="编辑文章" class="button bg-main icon-check-square-o" >
        </div>
      </div>
    </form>
  </div>
</div>
</body>
</html>