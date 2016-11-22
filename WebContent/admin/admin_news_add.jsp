<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.jx.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.net.URLEncoder"%>
<%@ page import="org.apache.commons.dbutils.QueryRunner"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
//获取当前url
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String jishu = "";
String fileName = "";
String fullName1 = "";
String fullName2 = "";
String fullName3 = "";
String fullName4 = "";
String shuzi = "";
try{
jishu = request.getParameter("jishu");
fileName = request.getParameter("fileName");
fullName1 = request.getParameter("fullName1");
fullName2 = request.getParameter("fullName2");
fullName3 = request.getParameter("fullName3");
fullName4 = request.getParameter("fullName4");
shuzi = request.getParameter("shuzi");
}catch(Exception e){
	
}
if(fileName==null){
	session.removeAttribute("newsfullName1");
	session.removeAttribute("newsfullName2");
	session.removeAttribute("newsfullName3");
	session.removeAttribute("newsfullName4");
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
			window.location='front_login.jsp' ;                            // 跳转到登录界面
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
//显示博客信息
List<Mapx<String,Object>> showdiscuss1 = DB.getRunner().query("select canshu_url as canshu_url from article where  author=? order by articleid desc limit 1",new MapxListHandler(),dluserid);
System.out.println();
int canshu_url=showdiscuss1.get(0).getInt("canshu_url");
//编辑保存博客信息
System.out.println(request.getMethod());//获取request方法 POST or GET
HashMap<String,String> param= G.getParamMap(request);
String title;
String titlejs;
String content1;
String content2;
String tag1;
String tag2;
String tag3;
String tag4;
String img1;
String ydimg1;
String img2;
String ydimg2;
String leixing;
String origin;
System.out.println("url_canshu:"+url_canshu+";canshu_url:"+canshu_url+";提交前img:"+(String)session.getAttribute("fullName1"));
if(url_canshu!=canshu_url){
if(param.get("Action")!=null && param.get("Action").equals("发表文章")){
	title=new String(request.getParameter("title").getBytes("iso-8859-1"),"utf-8");
	titlejs=new String(request.getParameter("titlejs").getBytes("iso-8859-1"),"utf-8");
	content1=param.get("content1");
	content2=param.get("content2");
	tag1=new String(request.getParameter("tag1").getBytes("iso-8859-1"),"utf-8");
	tag2=new String(request.getParameter("tag2").getBytes("iso-8859-1"),"utf-8");
	tag3=new String(request.getParameter("tag3").getBytes("iso-8859-1"),"utf-8");
	tag4=new String(request.getParameter("tag4").getBytes("iso-8859-1"),"utf-8");
	leixing=new String(request.getParameter("leixing").getBytes("iso-8859-1"),"utf-8");
	origin=new String(request.getParameter("origin").getBytes("iso-8859-1"),"utf-8");
	img1="upload/"+(String)session.getAttribute("newsfullName1");
	ydimg1="upload/"+(String)session.getAttribute("newsfullName2");
	img2="upload/"+(String)session.getAttribute("newsfullName3");
	ydimg2="upload/"+(String)session.getAttribute("newsfullName4");
	System.out.println("img1"+img1);
	if((title.equals("")||title.equals(null))||(content1.equals("")||content1.equals(null))||(content2.equals("")||content2.equals(null))){
		%>
			<script type="text/javascript" language="javascript">
					alert("主体信息不能为空");                                            // 弹出错误信息
			</script>
		<%
	}else{
		DB.getRunner().update("insert into article(author,title,titlejs,content1,content2,createtime,tag1,tag2,tag3,tag4,canshu_url,img1,img2,ydimg1,ydimg2,tagid,del,articletype,origin) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",dluserid,title,titlejs,content1,content2,df.format(new Date()),tag1,tag2,tag3,tag4,url_canshu,img1,img2,ydimg1,ydimg2,tagid,"0",leixing,origin);
		DB.getRunner().update("insert into news(author,title,titlejs,content,createtime,newstype,img1,tagid,del,type,origin) values(?,?,?,?,?,?,?,?,?,?,?)",dluserid,title,titlejs,content1,df.format(new Date()),"boke",img1,tagid,"0",leixing,origin);
		session.removeAttribute("newsfullName1");
		session.removeAttribute("newsfullName2");
		session.removeAttribute("newsfullName3");
		session.removeAttribute("newsfullName4");
		%>
		<script type="text/javascript" language="javascript">
				alert("发表成功");                                            // 弹出错误信息
				window.location='admin_news_add.jsp' ;                            // 跳转到登录界面
		</script>
	<%
	}
}else{
}
}

%> 
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
    <meta name="renderer" content="webkit">
    <title>新闻添加页</title>  
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
  <li><a href="##" id="a_leader_txt">新闻添加页</a></li>
  <!-- <li><b>当前语言：</b><span style="color:red;">中文</php></span>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;切换语言：<a href="##">中文</a> &nbsp;&nbsp;<a href="##">英文</a> </li> -->
</ul>
<div class="admin">
<div class="panel admin-panel">
  <div class="panel-head" id="add"><strong><span class="icon-pencil-square-o"></span>增加内容</strong></div>
  <div class="imgs" style="margin-left:100px;">
  <!-- 图片上传start  -->
     <div class="form-group">
     <div class="label">
          <br>
          <label>PC新闻封面图片<span style="color:red;">*(240*180)</span></label>
        </div>
 	 <form action="${pageContext.request.contextPath }/uploadServlet?url=newsadd&shuzi=1" method="post" enctype="multipart/form-data">
 	 <div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" > 
						<%if(shuzi!=null&&shuzi.equals("1")){
							if(fullName1==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("newsfullName1", fullName1);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("newsfullName1")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
									</script>
							<%}else{ %>
							<%} %>
						
						</div>
  	 </form>
  	</div>
  	<br>	<br>
  		<img alt="" src="../upload/<%=(String)session.getAttribute("newsfullName1") %>" style="width:50px!important;" height="50px">
  		
  	<div class="form-group">
  	<div class="label">
          <label>PC新闻详细图片<span style="color:red;">*(798*532)</span></label>
        </div>
					<form action="${pageContext.request.contextPath }/uploadServlet?url=newsadd&shuzi=3" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" >
						<%if(shuzi!=null&&shuzi.equals("3")){
							if(fullName3==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("newsfullName3", fullName3);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("newsfullName3")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %>
						
						</div>
				  	 </form>
				</div>
				<br><br>
				<img alt="" src="../upload/<%=(String)session.getAttribute("newsfullName3") %>" style="width:220px!important;" height="150px">
				<br><br>
				<div class="form-group">
				 	<div class="label">
			         <label>移动端新闻封面图片<span style="color:red;">*</span></label>
			      	  </div>
					<form action="${pageContext.request.contextPath }/uploadServlet?url=newsadd&shuzi=2" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" > 	
						<%if(shuzi!=null&&shuzi.equals("2")){
							if(fullName2==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("newsfullName2", fullName2);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("newsfullName2")!=null){%>
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %>
						
						</div>
				  	 </form>
				</div>
				<br>	<br>	
				<img alt="" src="../upload/<%=(String)session.getAttribute("newsfullName2") %>" style="width:120px!important;" height="80px">
				<br><br>
				<div class="form-group">
				<div class="label">
			         <label>移动端新闻详情图片<span style="color:red;">*</span></label>
			      	  </div>
			      	  <div class="field">
					<form action="${pageContext.request.contextPath }/uploadServlet?url=newsadd&shuzi=4" method="post" enctype="multipart/form-data">
						<div class="mb10">
						<input type="file" id="url1" name="img" class="input tips" style="width:25%; float:left;"  value=""  data-toggle="hover" data-place="right" data-image="" />
						<input type="submit" value="浏览上传" class="button bg-blue margin-left" style="float:left;" >	
						<%if(shuzi!=null&&shuzi.equals("4")){
							if(fullName4==null){
								//session.removeAttribute("fullName2");
							}else{
								if(fileName=="") {%>
									<script type="text/javascript" language="javascript">
									document.write('<span style="color:red;">上传失败</span>');          // 跳转到登录界面
								</script>
								<%}else{ %>
								<%
								session.setAttribute("newsfullName4", fullName4);
								} %>
							<%}
						}%>
									<%
							if((String)session.getAttribute("newsfullName4")!=null){%>
							
									<script type="text/javascript" language="javascript">
										document.write('<span style="color:red;">上传成功</span>');          // 跳转到登录界面
										
									</script>
							<%}else{ %>
							<%} %>
						
						</div>
				  	 </form>
				  	 </div>
				</div>
				<br>	<br>	
				  	 <img alt="" src="../upload/<%=(String)session.getAttribute("newsfullName4") %>" style="width:120px!important;" height="80px">
	<!-- 图片上传end -->
  </div>
  <div class="body-content">
  <br>
    <form  class="form-x" id="form_tj" action="admin_news_add.jsp?jishu=<%=val%>&fileName=tijiao" method="post">    
        <div class="form-group">
          <div class="label">
            <label>标题分类：</label>
          </div>
          <div class="field">
            <select name="leixing" class="input w50">
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
          <input type="text" Name="title" class="input" placeholder="标题" style="width:50%;"><span style="color:red;">*(最多20字)</span>
          </div>
        </div>
        <div class="form-group">
          <div class="label">
            <label>标题简述：</label>
          </div>
          <div class="field">
          <input type="text" Name="titlejs" class="input" placeholder="标题简述" style="width:50%;"><span style="color:red;">*(最多20字)</span>
          </div>
        </div>
      <div class="form-group">
        <div class="label">
          <label>描述：</label>
        </div>
        <div class="field">
          <textarea name="content1" ></textarea>
          <div class="tips"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>内容：</label>
        </div>
        <div class="field">
          <textarea name="content2"></textarea>
          <div class="tips"></div>
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>新闻出处：</label>
        </div>
        <div class="field">
          <input type="text" class="input" name="origin" placeholder="新闻出处(不填默认是饺耳世家)" />
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label>词条标签：</label>
        </div>
        <div class="field">
        <input type="text" Name="tag1"  placeholder="标签1" style="width:80px;">
		<input type="text" Name="tag2"  placeholder="标签2" style="width:80px;">
		<input type="text" Name="tag3"  placeholder="标签3" style="width:80px;">
		<input type="text" Name="tag4"  placeholder="标签4" style="width:80px;">
        </div>
      </div>
      <div class="form-group">
        <div class="label">
          <label></label>
        </div>
        <div class="field">
          <input type="submit" Name="Action" value="发表文章" class="button bg-main icon-check-square-o" >
        </div>
      </div>
    </form>
  </div>
</div>
</div>
</body>
</html>