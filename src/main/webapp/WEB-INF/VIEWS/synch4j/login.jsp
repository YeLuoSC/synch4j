<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html> 
<html>
  <head>
    <base href="<%=basePath%>"> 
    <title>登录</title> 
	<link rel="stylesheet" href="css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="css/styles/jqx.ui-smoothness.css" type="text/css" />     
  	<script type="text/javascript" src="js/jqxGrid/jqx-all.js"></script>  
  </head>
   <script type="text/javascript"> 
      $(document).ready(function () { 
	      $('#loginWin').jqxWindow({                   
	    	  showCollapseButton: false, 
	    	  showCloseButton:false,  
	    	  title:'数据同步', 
	    	  height: 250,         
	    	  width: 420,    
	    	  draggable:true,
	    	  resizable:true,   
	    	  initContent: function () {                     
	    		  var theme = 'ui-smoothness'; 
	        	  $("#code, #password").addClass('jqx-input');            
	        	  $("#code, #password").addClass('jqx-input-' + theme);            
	        	  $("#loginButton").jqxButton({theme: theme});            // add validation rules.             
	        	  $('#form').jqxValidator({                
	        		  rules: [{ input: '#code', message: '用户名不能为空!', action: 'keyup, blur', rule: 'required' },                       
	        		          { input: '#passWord', message: '密码不能为空', action: 'keyup, blur', rule: 'required' }
	        		          ]                                   
	    		  });            
	    		  $("#loginButton").click(function () {                
	    			  $('#form').jqxValidator('validate');             
	    		  });             
	    		  $("#form").on('validationSuccess',  
	    			 function () {                
	    			  		$("#form-iframe").fadeIn('fast');            
	    		   });              
	    	  }                
	      	});
		      var msg = "${loginInfo}" 
	   		  if(msg!=null&&msg!=''){
	   			  var code = '${code}';
	   			  var passWord = '${passWord}';
	   			  $('#code').val(code);
	   			  $('#passWord').val(passWord);  
	   			  alert(msg);     
	   		  }
	      }); 	
      </script> 
<body>         
	<div align="center" id="loginWin" style="font-size: 13px; font-family: Verdana;">
		<div align="center"> 
			<form class="form" id="form" method="post" action="login.do" style="text-align:center;">    
				<div style="margin-top:10px;">        
					<label>用户名:</label><input type="text" id="code" name="code" style="width:150px;" />
				</div>
				<div style="margin-top:20px;">  
					<label>密&nbsp;&nbsp;码:</label><input type="passWord" id="passWord" name="passWord" style="width:150px;"/>
				</div>    
				<div style="margin-top:20px;display: none">  
					<label>年&nbsp;&nbsp;份:</label>
					<select  id="year" name="year" style="width:150px;">
						<option value="2014">2014</option>
						<option value="2015">2015</option> 
					</select>
				</div>
				<div style="margin-top:30px;">     
					<input id="loginButton" type="submit" value="登   录" /> 
				</div> 
			</form>
		</div>
	</div>
</body>
</html>
