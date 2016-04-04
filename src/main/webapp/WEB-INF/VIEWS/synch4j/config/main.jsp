<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html> 
<html>
  <head>
  	<%String path = request.getContextPath(); %>
  	<meta charset="UTF-8"/>
    <title>数据同步</title> 
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.base.css" type="text/css" /> 
	<link rel="stylesheet" href="<%=path %>/scripts/synch4j/css/styles/jqx.ui-smoothness.css" type="text/css" />     
	<script type="text/javascript" src="<%=path %>/scripts/jquery.js"></script>
  	<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/jqxGrid/jqx-all.js"></script>  
  </head>
  <style>
  	html{
  		height:100%;
  	}
  	body{
  		height:100%;   
  		overflow:hidden;  
  	}
  </style>
  <script>
	  $(document).ready(function () { 
		  
		  $('#mainSplitter').jqxSplitter({ 
			  width: '100%',   
			  height: $(document.body).height()-10,      
			  orientation: 'horizontal',  
			  splitBarSize: 2,    
			  panels: [{size:30 }, { size: $(document.body).height()-30}]  
		  }); 
		  
		  $("#jqxMenu").jqxMenu({ 
			  width: '100%', 
			  height: '30px',
			  //mode: "popup",  
			  animationShowDuration: 200
		  });  
		  $("#jqxMenu").css('visibility', 'visible');  
		  $("#mainContent").css({width:'100%',height:'100%'});
		  //默认页 
		  //document.getElementById('mainContent').src = 'synch2/config/SynchConfigMain.do?' + tokenParam;  
		  
		}); 
	  var io = null;
  </script>
  <body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" scroll="no"> 
  	<div id='mainSplitter'>
	    <div id='jqxWidget' style='height:100px;'>             
	    	<div id='jqxMenu' style='visibility: hidden;'>   
	    		 <ul>
		    		<li id="datasynconfig"><a href="config/synchConfigMain.do" target="mainContent" >数据同步设置</a></li>
		    		<li><a href="procedureConfig.do" target="mainContent" id="procedureConfig">远程脚本同步</a></li>
		    		 <li><a href="execute/expMain.do?appID=BG" target="mainContent" id="dataexportBGT">标准模式导出</a></li>
		    		 <li><a href="execute/impMain.do" target="mainContent" id="dataimport">数据导入</a></li>      
	    		 </ul>   
	    	</div>
	    </div>
	    <div id='main'> 
	    	<iframe id='mainContent' name='mainContent' frameborder=0 style="overflow:hidden;"/> 
	    </div>	 
    </div> 
  </body> 
</html>
