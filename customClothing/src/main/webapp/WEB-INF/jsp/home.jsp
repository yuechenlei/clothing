<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE HTML>
<html  lang="zh-CN">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="keywords" content="小清新、服装定制、衣服定制、个性化。">
<meta http-equiv="description" content="小清新网是一家把顾客和设计师联系在一起的平台，致力于个人穿戴的个性化追求，和设计师直接面对客户的梦想。">
<link rel="shortcut icon" href="/customClothing/resources/images/favicon.ico" />
<title>小清新网</title>
<link rel="stylesheet" type="text/css" href="/customClothing/resources/bootstrap/css/bootstrap.min.css">
<!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
      <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->

<style type="text/css">

body{
  padding-top: 50px;
  padding-bottom:1000px;
  font-family: "Helvetica Neue",Helvetica, Arial, sans-serif;
}
.label{
  -webkit-box-shadow:2px 2px 2px #dfdfdf;
  box-shadow: 2px 2px 2px #dfdfdf;
  border:medium dotted #fff;
}
.label-default{
  background-image: -webkit-linear-gradient(270deg,rgba(153,153,153,1.00) 0%,
                     rgba(255,255,255,1.00) 100%);
  background-image: linear-gradient(180deg,rgba(153,153,153,1.00) 0%,
                     rgba(255,255,255,1.00) 100%);
  color: #000000;
}
.containerTest{
  background-color: #E8E8E7;
}
#mainhead{
  background-image: url("/customClothing/resources/images/xqx.jpg");
  background-repeat: no-repeat;
  width: 458px;
  height: 76px;
}

</style>
</head>

<body>

<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>        
            <span class="icon-bar"></span>    
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>    
         </button>
        <a class="navbar-brand" href="/customClothing/homepage">
            小清新
        </a>
      </div>
      
      <div id="navbar" class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
          <li class="active"><a href="/customClothing/homepage">主页</a></li>
          <li><a href="#">关于</a></li>
          <li class="dropdown">
              <a class="dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true"
               aria-expanded="false">加盟 <span class="caret"></span></a>
              <ul class="dropdown-menu">
                  <li><a href="#">设计师加盟</a></li>
                  <li role="separator" class="divider"></li>
                  <li><a href="#">面料商加盟</a></li>
             </ul>
         </li>
        </ul>
        <p class="navbar-text navbar-right" style="margin-right: 5px;"><a href="/customClothing/loginRequest/toLogin" class="navbar-link" >登录</a> |
            <a href="/customClothing/loginRequest/toRegister" class="btn btn-default btn-xs" target="_blank">注册 </a>
        </p>
      </div>
      
    </div>
  </nav>


<!-- 轮播 -->
<div class="container">
<div class="row">
<div id="cjIndicators" class="carousel slide" data-ride="carousel" data-interval="10000" style="margin-top: 10px;margin-left: 10%;">
  <ol class="carousel-indicators">
    <li data-target="#cjIndicators" data-slide-to="0" class="active"></li>
    <li data-target="#cjIndicators" data-slide-to="1"></li>
    <li data-target="#cjIndicators" data-slide-to="2"></li>
    <li data-target="#cjIndicators" data-slide-to="3"></li>
    <li data-target="#cjIndicators" data-slide-to="4"></li>
    <li data-target="#cjIndicators" data-slide-to="5"></li>
    <li data-target="#cjIndicators" data-slide-to="6"></li>
  </ol>
  <div class="carousel-inner" role="listbox">
    <div class="item active ">
      <img class="" src="/customClothing/resources/images/cj001_800x400.jpg" alt="第一张" width="800" height="400">
      <div class="carousel-caption">
        <h3>雏菊</h3>
        <p>清新，自然，无拘无束</p>
      </div>
    </div>
    <div class="item">
      <img class="" src="/customClothing/resources/images/cj002_800x400.jpg" alt="第二张" width="800" height="400">
    </div>
    <div class="item">
      <img class="" src="/customClothing/resources/images/cj003_800x400.jpg" alt="第三张" width="800" height="400">
    </div>
    <div class="item">
      <img class="" src="/customClothing/resources/images/cj004_800x400.jpg" alt="第四张" width="800" height="400">
    </div>
    <div class="item">
      <img class="" src="/customClothing/resources/images/cj005_800x400.jpg" alt="第五张" width="800" height="400">
    </div>
    <div class="item">
      <img class="" src="/customClothing/resources/images/cj006_800x400.jpg" alt="第六张" width="800" height="400">
    </div>
    <div class="item">
      <img class="" src="/customClothing/resources/images/cj007_800x400.jpg" alt="第七张" width="800" height="400">
    </div>
  </div>
</div>
</div>
</div>

	


<script type="text/javascript" src="<spring:url value="/resources/js/jquery/jquery-3.3.1.min.js" />"></script>
<script type="text/javascript" src="<spring:url value="/resources/bootstrap/js/bootstrap.min.js" />"></script>

<script type="text/javascript">
    $(function() {
    });
</script>



</body>
</html>
