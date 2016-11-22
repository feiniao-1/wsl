<%@page import="com.mchange.v2.c3p0.impl.DbAuth"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.jx.common.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.apache.commons.dbutils.QueryRunner"%>

<% 
//退出
session.removeAttribute("username");
HashMap<String,String> param= G.getParamMap(request);
HashMap<String,Object> myparam=new HashMap<String,Object>();
List<String> errors=new ArrayList<String>();
if(param.get("opt")!=null && param.get("opt").equals("login")){
	if(param.get("str")==null || param.get("str").equals("") || param.get("password")==null || param.get("password").equals("")){//如果用户名 或密码填写的空值 则报错
		errors.add("用户名密码错误");
	}
	if(errors.size()==0){
		String password2 = DesUtils.encrypt(param.get("password")); // DesUtils加密
		System.out.println(password2);
		List<Mapx<String, Object>> listAll = DB.getRunner().query("select password ,userid from user where username=? ",new MapxListHandler(), param.get("str"));
		if((listAll==null || listAll.size()==0)){
		System.out.println("用户名不存在");
			errors.add("用户名不存在");
		}else if(listAll.get(0).getStringView("password").equals(password2)){//登陆成功
			System.out.println(" password YES");
			G.setCookie("token", G.getToken(listAll.get(0).getInt("userid"),param.get("password")), response);
			response.sendRedirect("admin/admin_index.jsp");
			session.setAttribute("username", param.get("str"));
			return;
		}else{
			System.out.println(" password NO");
			errors.add("密码错误");
		}
	}
	myparam.put("errorStr", G.toErrorStr(errors));
}
%>
<!DOCTYPE HTML >
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <meta name="description" content="舵爷，火锅的江湖。舵爷江湖老火锅旗舰店创立于北京财满街。舵爷品牌名来自在“京城孟尝君”之美誉的黄珂黄舵爷，一群骨灰级美食家创造了这一文化老火锅的饕餮盛宴，主打重庆传统火锅情怀。">
		 <meta name="keywords" content="火锅，舵爷火锅，美味火锅，舵爷文化，江湖老火锅，麻辣鲜美，涮锅，四川火锅，重庆火锅，好吃的火锅">
		<title>登陆页面</title>
		<link href="img/dy-icon.png" rel="SHORTCUT ICON">
		<link href="css/_main.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<script src="js/jquery-1.11.1.min.js"></script>
		<script src="js/bootstrap.min.js" type="text/javascript"></script>
		
		<!--[if it iE8]>
			<p class="tixin">为了达到最佳观看效果，请升级到最新浏览器</p>
        -->
    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    	
</HEAD>
<body>
		<div class="login-box">
			<div class="container" style="padding: 70px 0;">
				<div class="col-md-4  col-md-offset-4">
					<div class="center mb30">
						<img src="img/duoyeLOGO.png"  style="display: inline-block; margin-bottom: 20px;"/>
						<p style="font-size: 18px;">舵爷火锅账号登陆</p>
					</div>
					<form action="front_login.jsp" method="POST">
					<input type="hidden" name="referer"	value="<%=request.getHeader("referer") %>">
						<input type="hidden" name="opt" value="login">
						<div class="input-group input-group-lg">
						  <span class="input-group-addon glyphicon glyphicon-user"></span>
						  <input type="text" class="form-control" tabIndex=1 placeholder="请输入账号" autocomplete="off" name="str">
						</div>
						<div class="input-group input-group-lg">
						  <span class="input-group-addon glyphicon glyphicon-lock"></span>
						  <input type="password" class="form-control" placeholder="请输入密码" tabIndex=2 autocomplete="off" name="password">
						</div>
						<div class="warning" style="display: none;"><span class="glyphicon glyphicon-info-sign"></span>密码输入有误</div>
						<input class="btn btn-danger btn-lg"  onclick="checkPhone()" type="submit" value="立即登录" style="width: 100%; margin-bottom: 20px;">
					</form>
					<p class="center a-hover"><a href="front_reg.jsp" target="_blank">注册账号</a><span class="m_r_l-10">|</span><a href="#" target="_blank">忘记密码?</a></p>
				</div>
			</div>
		</div>
<script>
	//手机号 规则匹配
function checkPhone(){ var phone = document.getElementById('phone').value; if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){ alert("手机号码有误，请重填"); return false; } }
</script>	
</body>
</html>

