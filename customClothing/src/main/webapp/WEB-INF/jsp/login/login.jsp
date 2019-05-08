<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML>
<html lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="keywords" content="小清新、服装定制、衣服定制、个性化。">
<meta http-equiv="description" content="小清新网是一家把顾客和设计师联系在一起的平台，致力于个人穿戴的个性化追求，和设计师直接面对客户的梦想。">
<link rel="shortcut icon" href="/customClothing/resources/images/favicon.ico" />
<title>小清新网</title>
<link rel="stylesheet" type="text/css" href="/customClothing/resources/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/customClothing/resources/fontAwesome/css/font-awesome.min.css">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<style type="text/css">
#loginForm {
	 margin-top: 10%; 
	/* margin-left: 30%; */
}
</style>

</head>

<body>

	<h1 class="page-title text-center">登 录</h1>

	<form class="form-horizontal" id="loginForm" method="post" action="/customClothing/loginRequest/loginMitEmail" autocomplete="off" target="_self">
		<div class="form-group" id="accountGroup">
			<label for="loginAccount" class="sr-only">账户</label>
			<div class="col-md-3 col-md-offset-5">
				<input type="text" class="form-control" id="loginAccount" placeholder="手机号/电子邮箱" required aria-describedby="help1" name="email">
				<span id="help1" class="help-block"></span>
	      </div>
		</div>
		<div class="form-group" id="pwdGroup">
			<label for="loginPwd" class="sr-only">密码</label>
			<div class="col-md-3 col-md-offset-5">
				<input type="password" class="form-control" id="loginPwd" placeholder="密码" required aria-describedby="help2" name="pwd">
				<span id="help2" class="help-block"></span>
	        </div>
		</div>
		<div class="form-group">
		  <div class="col-md-offset-5">
			<div class="checkbox" style="margin-left: 15px;" id="lgCheck" >
				<label >
					<input type="checkbox"  >
					<span>记住我</span>
				</label>
				<a href="#"  style="margin-left: 30px;">忘记密码</a>
			</div>
			  
	      </div>
		</div>
		<div class="form-group">
			  <div class="col-md-3 col-md-offset-5">
				<button type="button" class="btn btn-default btn-block" id="lgSubmit">登 录</button>
			</div>
		</div>
	</form>





	<script type="text/javascript" src="<spring:url value="/resources/js/jquery/jquery-3.3.1.min.js" />"></script>
	<script type="text/javascript" src="<spring:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>

	<script type="text/javascript">
	 $(function() {
		    $("#loginAccount").focus(function(){
		    	  $("#loginAccount").attr("placeholder","");
		    }).blur(function(){
		    	  $("#loginAccount").attr("placeholder","手机号/电子邮箱");
		    }).keydown(function(){
		    	$("#help1").text("");
		    	$("#accountGroup").attr("class","form-group");
		    	
		    });
		    $("#loginPwd").focus(function(){
		    	  $("#loginPwd").attr("placeholder","");
		    }).blur(function(){
		    	  $("#loginPwd").attr("placeholder","密码");
		    }).keydown(function(){
		    	$("#help2").text("");
		    	$("#pwdGroup").attr("class","form-group");
		    });
		    
		    var loginCount=0;
		    $("#lgSubmit").click(function(){
		    	  var account = $.trim($("#loginAccount").val());
		    	  var loginPwd = $.trim($("#loginPwd").val());
		    	  var lgCheck = $("#lgCheck").prop("checked");
		    	  
		    	  var emptyPattern = /^\s+$/;
		    	  if(!account || emptyPattern.test(account)){
		    		  $("#help1").text("请输入账号");
		    		  $("#accountGroup").attr("class","form-group has-error");
		    		  return;
		    	  }
		    	  if (!loginPwd || emptyPattern.test(loginPwd)) {
		    		  $("#help2").text("请输入密码");
		    		  $("#pwdGroup").attr("class","form-group has-error");
		    		  return;
				  }
		    	  
		    	  if(lgCheck){
		    		  
		    	  }
		    	  loginCount++;
		    	  var emailPattern = /^([\w\-\.])+@\w+([\-\.]\w+)*\.\w+([\-\.]\w+)*$/;
		    	  var tpnPattern = /^1[0-9]{10}$/;
		    	  if(!(emailPattern.test(account) || tpnPattern.test(account))){
		    		  if(loginCount>3) {
		    			  $("#help1").text("请输入正确的手机号码或电子邮箱,中间不要有空格");
		    			  $("#accountGroup").attr("class","form-group has-error");
		    			  return;
		    		  }
		    		  $("#help1").text("请输入正确的手机号码或电子邮箱");
		    		  $("#accountGroup").attr("class","form-group has-error");
		    		  return;
		    	  }
		    	  var pwdPattern = /^([\w\-\.]){6,20}$/;
		    	  if(!pwdPattern.test(loginPwd)){
		    		  if(loginCount>3) {
		    			  $("#help2").text("密码为6-20位字母、数字组合，可包含(下划线_、减号-和点号.)三种特殊字符");
		    			  $("#pwdGroup").attr("class","form-group has-error");
		    			  return;
		    		  }
		    		  $("#help2").text("密码错误");
		    		  $("#pwdGroup").attr("class","form-group has-error");
		    		  return;
		    	  }
		    	  if(emailPattern.test(account)){
		    		  $("#loginForm").submit();
		    		  return;
		    	  }
		    	  
		    	  $("#loginForm").submit();
		    });
		   
	    });
		
	</script>



</body>
</html>
