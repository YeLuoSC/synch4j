<html>
<title>同步测试</title>
<jsp:include  page="/static/include/pub2.0.jsp"/>
<script src="static/pub2.0/js/Hq.js"></script>
<script type="text/javascript">
$(function(){
	$("#testbtn").click(function(){
		$.ajax({
	        url: "synch2/test2/click.do"
	    });
	}
	);
})
function click(){
	
}
//Hq.getScript(["static/app/synch2/test.js"]);
</script>
<body>         
 	adfadsf
	<input id="testbtn" type="button" value="测试"/>
	<div id="testDiv"></div>
</body>
</html>
