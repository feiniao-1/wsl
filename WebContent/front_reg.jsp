<%@page import="java.net.URLEncoder"%>
<%@page import="com.mchange.v2.c3p0.impl.DbAuth"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.jx.common.*" %>
<%@ page import="java.util.*" %>
<%@ page import="org.apache.commons.dbutils.QueryRunner" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
HashMap<String,String> param= G.getParamMap(request);
if(param==null){//临时，未正式交付，无法创建新用户
	response.sendRedirect("front_login.jsp");
	return;
}
HashMap<String,Object> myparam = new HashMap<String,Object>();//存储自用的一些变量
String opt = param.get("opt");
System.out.println(param.get("id"));
SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
System.out.println(df.format(new Date()));// new Date()为获取当前系统时间
///保存用户注册信息 -start  下面定义的变量 仅在此处使用     
if(param.get("Action")!=null && param.get("Action").equals("立即注册")){
	List<String> errors = new ArrayList<String>();
			String name = (String)G.commonCheckx(errors,param.get("username"),"用户名","must","string","between,2,50");
			String phone = (String)G.commonCheckx(errors,param.get("phone"),"电话","phone","string","between,11,20");
			String mail = (String)G.commonCheckx(errors,param.get("mail"),"邮箱","mail","string","between,5,50");
			String password = (String)G.commonCheckx(errors,param.get("password"),"密码","must","string","between,6,50");
			String password2 = (String)G.commonCheckx(errors,param.get("password2"),"密码确认","must","string","between,6,50");
			if(errors.size()>0){
				System.out.println(errors);%>
				<script type="text/javascript" language="javascript">
					alert("<%=errors%>");                                            // 弹出错误信息
					//window.location='front_reg.jsp' ; 
				</script>
			<%}else{//普通验证通过，继续确认
				if(!password.equals(password2)){
					errors.add("两次输入密码不一致");
					myparam.put("errorStr", G.toErrorStr(errors));
				}else{//验证通过，存库
					password = DesUtils.encrypt(password);
					DB.getRunner().update("insert into user(username,password,shenhe,status,createtime,phone,mail) values(?,?,?,?,?,?,?)", name,password,"审核通过","有效",df.format(new Date()),phone,mail);
					myparam.put("addResult", "1");
					out.print("<script>alert('注册成功'); window.location='front_index.jsp' </script>");
					//response.sendRedirect("front_boke.jsp");
				}
			}
		}
///end
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		  <meta name="viewport" content="width=device-width, initial-scale=1">
		  <meta name="description" content="舵爷，火锅的江湖。舵爷江湖老火锅旗舰店创立于北京财满街。舵爷品牌名来自在“京城孟尝君”之美誉的黄珂黄舵爷，一群骨灰级美食家创造了这一文化老火锅的饕餮盛宴，主打重庆传统火锅情怀。">
		 <meta name="keywords" content="火锅，舵爷火锅，美味火锅，舵爷文化，江湖老火锅，麻辣鲜美，涮锅，四川火锅，重庆火锅，好吃的火锅">
		<title>注册页面</title>
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
</head>
<body style="min-height: 1000px;">
			<div class="login-box register-box">
			<div class="container" style="padding: 70px 0;">
				<div class="col-md-4  col-md-offset-4">
					<div class="center ">
						<img src="img/duoyeLOGO.png"  style="display: inline-block; margin-bottom: 20px;"/>
						<p style="font-size: 18px;">舵爷火锅账号注册</p>
					</div>
				<form class="form" action="front_reg.jsp" method="POST">
					<input type="hidden" name="opt" value="addsave">
					<input type="hidden" name="usertype" value="<%= param.get("usertype")==null?"":param.get("usertype") %>">
					<input type="text" style="visibility: hidden;" readOnly="true" class="text"  autocomplete="off" name="id"
		                   onpaste="return false;" value="<%= param.get("id")==null?"":param.get("id") %>">
					<div class="input-group input-group-lg">
					<span class="input-group-addon glyphicon glyphicon-user"></span>
					    <label class="sr-only" for="">用户名</label>
					    <input type="text"   class="form-control"  autocomplete="off" name="username"
		                   onpaste="return false;" placeholder="请输入用户名" value="<%= param.get("username")==null?"":param.get("username") %>">
				   	</div>
				   	<div class="input-group input-group-lg">
				   	<span class="input-group-addon glyphicon glyphicon-lock"></span>
					    <label class="sr-only" for="">手机号</label>
					    <input type="text"  class="form-control" id="phone"  name="phone" placeholder="请输入手机号" value="<%= param.get("phone")==null?"":param.get("phone") %>">
				   	</div>
			<%if(false) {%>
			<script type="text/javascript" language="javascript">
				document.write('<span style="color:red;margin-top:-5px;">请输入正确的手机号</span>'); // 手机号验证
			</script>
			<%}%>
				   <div class="warning" style="display: none;"><span class="glyphicon glyphicon-info-sign"></span>请输入正确的手机号</div>
					<div class="input-group input-group-lg">
					<span class="input-group-addon glyphicon glyphicon-lock"></span>
					    <label class="sr-only" for="">邮箱</label>
					    <input type="text"  class="form-control"  name="mail" placeholder="请输入邮箱" value="<%= param.get("mail")==null?"":param.get("mail") %>">
				   	</div>
				   <div class="warning" style="display: none;"><span class="glyphicon glyphicon-info-sign"></span>请输入正确的邮箱</div>
			    <!-- <div class="form-group">
				    <label class="sr-only" for="">验证码</label>
				    <input type="text" id="" class="form-control text02 " placeholder="请输入验证码" style="display: inline-block;">
				    <div class="validate" style="display: inline-block;">
						<a href="javascipt:;" class="btn btn-primary get-validate">获取验证码</a>
						<a href="javascipt:;" class="btn wait-validate" style="display:none;">59秒后重新获取</a>
						</div>
				  </div>  -->
			  	 
			  	 	<div class="input-group input-group-lg">
			  	 	<span class="input-group-addon glyphicon glyphicon-lock"></span>
				    <label class="sr-only" for="">请设置密码</label>
				    <input type="password"  class="form-control"  style="ime-mode:disabled;"
	                   onpaste="return  false" autocomplete="off" name="password" placeholder="请设置密码" value="<%= param.get("password")==null?"":param.get("password") %>">
				   </div>
				  <div class="input-group input-group-lg">
				  <span class="input-group-addon glyphicon glyphicon-lock"></span>
				    <label class="sr-only" for="">请确认密码</label>
				    <input type="password" class="form-control"  onpaste="return  false" name="password2"
	                   autocomplete="off" placeholder="请确认密码" value="<%= param.get("password2")==null?"":param.get("password2") %>">
				   </div>
				   <div class="warning" style="display: none;"><span class="glyphicon glyphicon-info-sign"></span>密码输入有误</div>
				   <input type="submit" Name="Action" Value="立即注册"  class="btn btn-danger btn-lg"  tabindex="25" onclick="test()" style="width: 100%; margin-bottom: 20px;">
			  </form>
					<p class="center a-hover color-999999 mb10">点击“立即注册”即表示您同意并愿意遵守饺耳<a href="" target="_blank">用户协议</a></p>
					<p class="center a-hover color-999999">已有账号<a href="front_login.jsp" target="_blank">立即登录</a></p>
				</div>
			</div>
		</div>
	
	<!--获取验证码部分JS-->
	<script>
	$(function(){
	  $('.get-validate').click(function(){
		  $('.get-validate').hide();
		  $('.wait-validate').show();
	   });	
		
	});
	</script>
	<script>
	//手机号 规则匹配
	//function checkPhone(){ var phone = document.getElementById('phone').value; if(!(/^1[3|4|5|7|8]\d{9}$/.test(phone))){ alert("手机号码有误，请重填"); return false; } }
	//JS正则验证邮箱的格式
	function test()
        {
           var temp = document.getElementById("mail");
           //对电子邮件的验证
           var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
           if(!myreg.test(temp.value))
           {
                alert('提示\n\n请输入有效的E_mail！');
                myreg.focus();
                return false;
           }
        }
	</script>	
	<script type="text/javascript">  
	//电子邮箱正则表达式  
	var tel=document.getElementById("teltext");      if(!(reg.test(tel))){           alert("不是正确的11位手机号");          document.getElementById("teltext").Value="";      }else{            }}  
	//检查输入的数据是不是邮箱格式  function checkemail(){
	var email = document.getElementById("emailtext");    
	//获取email控件对象         
	var reg =/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
	//正则表达式        
	if (!reg.test(email.Value)) {                    alert("邮箱格式错误，请重新输入！");                    emailtext.focus();                    document.getElementById("emailtext").Value="";                      return;                    }          }    

</script>
</body>
</html>
