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
#registerForm {
	margin-top: 10%;
	/* margin-left: 30%; */
}
</style>

</head>

<body>
    
    <h1 class="page-title text-center ">注 册</h1>
	<form  class="form-horizontal" id="registerForm" method="post" action="/customClothing/loginRequest/newEmailRegister" autocomplete="off" target="_self">
		<div class="form-group" id="reEmailGroup">
			<label for="reEmail" class="sr-only">电子邮箱</label>
			<div class="col-md-3 col-md-offset-5">
				<input type="email" class="form-control" id="reEmail" placeholder="电子邮箱" required 
				                                         name="email" aria-describedby="help1" >
				<span id="help1" class="help-block"></span>                                         
			</div>
		</div>
		<div class="form-group" id="pwd1Group">
			<label for="pwd1" class="sr-only">密码</label>
			<div class="col-md-3 col-md-offset-5">
				<input type="password" class="form-control " id="pwd1" placeholder="密码" required 
				                                             name="pwd1" aria-describedby="help2" >
				<span id="help2" class="help-block"></span>               
			</div>
		</div>
		<div class="form-group" id="pwd2Group">
			<label for="pwd2" class="sr-only">确认密码</label>
			<div class="col-md-3 col-md-offset-5">
				<input type="password" class="form-control" id="pwd2" placeholder="确认密码" required  
				                                            name="pwd2" aria-describedby="help3" >
				<span id="help3" class="help-block"></span>                       
			</div>
		</div>
		<div class="form-group" id="reCheckGroup">
		    <div class="col-md-offset-5" >
				<div class="checkbox" style="margin-left: 15px;">
				    <label>
					    <input type="checkbox" id="reCheck"  required aria-describedby="help4" name="reCheck" >
					    <span >同意<a href="#" > 用户协议</a></span> 
					    <span id="help4" class="help-block"></span>
			        </label>
			</div>
		  </div>
		</div>
		<div class="form-group">
            <div class="col-md-3 col-md-offset-5">
				<button type="button" class="btn btn-primary btn-block" id="registerSubmit">注 册</button>
		    </div>
		</div>
	</form>


	<script type="text/javascript" src="<spring:url value="/resources/js/jquery/jquery-3.3.1.min.js" />"></script>
	<script type="text/javascript" src="<spring:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>

	<script type="text/javascript">
    $(function() {
	    $("#reEmail").focus(function(){
	    	  $("#reEmail").attr("placeholder","");
	    }).blur(function(){
	    	  $("#reEmail").attr("placeholder","电子邮箱");
	    }).keydown(function(){
	    	$("#help1").text("");
	    	$("#reEmailGroup").attr("class","form-group");
	    	
	    });
	    $("#pwd1").focus(function(){
	    	  $("#pwd1").attr("placeholder","");
	    }).blur(function(){
	    	  $("#pwd1").attr("placeholder","密码");
	    }).keydown(function(){
	    	$("#help2").text("");
	    	$("#pwd1Group").attr("class","form-group");
	    });
	    $("#pwd2").focus(function(){
	    	  $("#pwd2").attr("placeholder","");
	    }).blur(function(){
	    	  $("#pwd2").attr("placeholder","确认密码");
	    }).keydown(function(){
	    	$("#help3").text("");
	    	$("#pwd2Group").attr("class","form-group");
	    });
	    $("#reCheckGroup").mouseover(function(){
	    	  $("#reCheckGroup").attr("class","form-group");
	    });
	    
	    var registerCount=0;
	    $("#registerSubmit").click(function(){
	    	  var email = $.trim($("#reEmail").val());
	    	  var pwd1 = $.trim($("#pwd1").val());
	    	  var pwd2 = $.trim($("#pwd2").val());
	    	  var recheck = $("#reCheck").prop("checked");
	    	  
	    	  var emptyPattern = /^\s+$/;
	    	  if(!email || emptyPattern.test(email)){
	    		  $("#help1").text("请输入电子邮箱地址");
	    		  $("#reEmailGroup").attr("class","form-group has-error");
	    		  return;
	    	  }
	    	  if (!pwd1 || emptyPattern.test(pwd1)) {
	    		  $("#help2").text("请输入密码");
	    		  $("#pwd1Group").attr("class","form-group has-error");
	    		  return;
			  }
	    	  if (!pwd2 || emptyPattern.test(pwd2)) {
	    		  $("#help3").text("请确认密码");
	    		  $("#pwd2Group").attr("class","form-group has-error");
	    		  return;
			  }
	    	  
	    	  if (pwd1 != pwd2) {
	    		  $("#help3").text("两次密码不一致");
	    		  $("#pwd1Group").attr("class","form-group has-error");
	    		  $("#pwd2Group").attr("class","form-group has-error");
	    		  return;
			  }
	    	  if(!recheck){
	    		  $("#help4").text("请仔细查看我们的用户协议，我们遵守相关法律，保护您的个人隐私不被侵犯");
	    		  $("#reCheckGroup").attr("class","form-group has-error");
	    		  return;
	    	  }
	    	  registerCount++;
	    	  var emailPattern = /^([\w\-\.])+@\w+([\-\.]\w+)*\.\w+([\-\.]\w+)*$/;
	    	  if(!emailPattern.test(email)){
	    		  if(registerCount>2) {
	    			  $("#help1").text("邮箱地址只能包含英文字母、数字、下划线_、减号-和点号.");
	    			  $("#reEmailGroup").attr("class","form-group has-error");
	    			  return;
	    		  }
	    		  $("#help1").text("请输入正确的电子邮箱");
	    		  $("#reEmailGroup").attr("class","form-group has-error");
	    		  return;
	    	  }
	    	  
	    	  var pwdPattern = /^([\w\-\.]){6,20}$/;
	    	  if(!pwdPattern.test(pwd1)){
	    		  if(registerCount>2) {
	    			  $("#help2").text("密码为6-20位字母、数字组合，可包含(下划线_、减号-和点号.)三种特殊字符");
	    			  $("#pwd1Group").attr("class","form-group has-error");
	    			  return;
	    		  }
	    		  $("#help2").text("密码包含非法字符");
	    		  $("#pwd1Group").attr("class","form-group has-error");
	    		  return;
	    	  }
	    	  
	    	  $("#registerForm").submit();
	    	  
	    });
	   
    });
    
    
    
    
    </script>



</body>
</html>
