<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="t" %>
<%@ page session="false" %>
<html>
  <head>
    <title>小清新网</title>
    <link rel="stylesheet" type="text/css" href="<s:url value="/resources/css/style.css" />" >
  </head>
  <body>
    <div id="header">
      <t:insertAttribute name="header" />
    </div>
    <div id="content">
      <t:insertAttribute name="body" />
    </div>
    <div id="footer">
      <t:insertAttribute name="footer" />
    </div>
  </body>
</html>
