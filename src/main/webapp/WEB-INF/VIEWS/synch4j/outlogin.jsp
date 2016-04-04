<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html> 
<html>
  <head>
    <base href="<%=basePath%>"> 
    <title>重新登录</title> 
	<link rel="stylesheet" href="css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="css/styles/jqx.ui-smoothness.css" type="text/css" />     
  	<script type="text/javascript" src="js/jqxGrid/jqx-all.js"></script>  
  </head>
   <script type="text/javascript"> 
	   var user  = '${user}';
	   if(user==''){
	 	  alert("超时,请重新登录");
	 	  parent.window.location.href = '<%=basePath%>login.do';   
	   }
    </script> 
<body>         
	
</body>
</html>
