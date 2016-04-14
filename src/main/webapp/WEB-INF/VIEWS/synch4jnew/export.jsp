<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%String path = request.getContextPath(); %>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Lumino - Tables</title>

<link href="<%=path %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=path %>/css/datepicker3.css" rel="stylesheet">
<%--<link href="<%=path %>/css/bootstrap-table.css" rel="stylesheet">--%>
<link href="<%=path %>/css/styles.css" rel="stylesheet">
<link href="<%=path %>/css/pagination.css" rel="stylesheet">
<script src="<%=path %>/scripts/js/jquery-1.11.1.min.js"></script>
<script src="<%=path %>/scripts/js/angular.min.js"></script>
<script src="<%=path %>/scripts/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=path %>/scripts/synch4j/js/json2.js"></script>  
<script src="<%=path %>/scripts/synch4jnew/export.js"></script>
<script src="<%=path %>/scripts/js/tm.pagination.js"></script>
<%--<script src="<%=path %>/scripts/js/bootstrap-table.js"></script>--%>
<script>
	!function ($) {
		$(document).on("click","ul.nav li.parent > a > span.icon", function(){		  
			$(this).find('em:first').toggleClass("glyphicon-minus");	  
		}); 
		$(".sidebar span.icon").find('em:first').addClass("glyphicon-plus");
	}(window.jQuery);

	$(window).on('resize', function () {
	  if ($(window).width() > 768) $('#sidebar-collapse').collapse('show')
	})
	$(window).on('resize', function () {
	  if ($(window).width() <= 767) $('#sidebar-collapse').collapse('hide')
	})
	
	
</script>
<!--[if lt IE 9]>
<script src="js/html5shiv.js"></script>
<script src="js/respond.min.js"></script>
<![endif]-->

</head>

<body ng-app="myapp"  ng-controller="myCtrl">
	<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#sidebar-collapse">
					<span class="sr-only">Toggle navigation</span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
					<span class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#"><span>Synch4j</span></a>
				<ul class="user-menu">
					<li class="dropdown pull-right">
						<a href="#" class="dropdown-toggle" data-toggle="dropdown"><span class="glyphicon glyphicon-user"></span> User <span class="caret"></span></a>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#"><span class="glyphicon glyphicon-user"></span> Profile</a></li>
							<li><a href="#"><span class="glyphicon glyphicon-cog"></span> Settings</a></li>
							<li><a href="#"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
						</ul>
					</li>
				</ul>
			</div>
							
		</div><!-- /.container-fluid -->
	</nav>
		
	<div id="sidebar-collapse" class="col-sm-3 col-lg-2 sidebar">
		<form role="search">
			<div class="form-group">
				<input type="text" class="form-control" placeholder="Search">
			</div>
		</form>
		<ul class="nav menu">
			<li><a href="config2.do"><span class="glyphicon glyphicon-list-alt"></span>数据同步设置</a></li>
			<li ><a href="procedure.do"><span class="glyphicon glyphicon-info-sign"></span>远程脚本执行设置</a></li>
			<li class="active"><a href="#"><span class="glyphicon glyphicon-th"></span>标准模式导出</a></li>
			<li><a href="import.do"><span class="glyphicon glyphicon-pencil"></span>数据导入</a></li>
			<%--<li><a href="forms.html"><span class="glyphicon glyphicon-pencil"></span> Forms</a></li>--%>
			<li><a href="about.do"><span class="glyphicon glyphicon-info-sign"></span>关于作者</a></li>
			<%--<li class="parent ">
				<a href="#">
					<span class="glyphicon glyphicon-list"></span> Dropdown <span data-toggle="collapse" href="#sub-item-1" class="icon pull-right"><em class="glyphicon glyphicon-s glyphicon-plus"></em></span> 
				</a>
				<ul class="children collapse" id="sub-item-1">
					<li>
						<a class="" href="#">
							<span class="glyphicon glyphicon-share-alt"></span> Sub Item 1
						</a>
					</li>
					<li>
						<a class="" href="#">
							<span class="glyphicon glyphicon-share-alt"></span> Sub Item 2
						</a>
					</li>
					<li>
						<a class="" href="#">
							<span class="glyphicon glyphicon-share-alt"></span> Sub Item 3
						</a>
					</li>
				</ul>
			</li>
			--%><li role="presentation" class="divider"></li>
			<li><a href="login.html"><span class="glyphicon glyphicon-user"></span> Login Page</a></li>
		</ul>
		<div class="attribution">Template by <a href="http://www.medialoot.com/item/lumino-admin-bootstrap-template/">Medialoot</a></div>
	</div><!--/.sidebar-->
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main" >			
		<div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">数据导出</li>
			</ol>
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">数据导出日志</h1>
				<p>下表为之前的导出日志</p>
			</div>
		</div><!--/.row-->
				
		
		<div class="row">
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">
						<button type="button" class="btn btn-success"  ng-click="export$()">导出</button>
						<button type="button" class="btn btn-danger" ng-click="delBatchProcedure()">删除</button>
					</div>
					<div class="panel-body">
						<table class="table table-striped table-hover">
						    <thead>
						    <tr>
						        <th><input type="checkbox"  ng-model="allchecked" ng-change="checkAll(allchecked)"/></th>
						        <th>导出文件名称</th>
						        <th>开始时间</th>
						        <th>结束时间</th>
						        <th>用时(s)</th>
						        <th>操作</th>
						    </tr>
						    </thead>
						    <tbody>
						    	<tr ng-repeat="x in data" >
						    		<td><input type="checkbox"  ng-click="updateChecked(x)" ng-model="x.isSelected"/></td>
						    		<td><span ng-if="!x.editable">{{x.fileName}}</span></td>
						    		<td><span ng-if="!x.editable">{{x.startDate}}</span></td>
						    		<td><span ng-if="!x.editable">{{x.endDate}}</span></td>
						    		<td><span ng-if="!x.editable">{{x.usedDate}}s</span></td>
						    		<td>
						    			<span class="btn btn-primary btn-xs" title="查看详细导出日志" ng-click="moreInfo(x)"><i class="glyphicon glyphicon-info-sign"></i></span>
						    			<%--<span class="btn btn-primary btn-xs" title="下载导出包" ng-click="download(x)"><i class="glyphicon  glyphicon-download"></i></span>--%>
						    		</td>
						    	</tr>
						    </tbody>
						</table>
					</div>
				</div>
			</div>
		</div><!--/.row-->
		<!-- Pagination -->
		<tm-pagination conf="paginationConf"></tm-pagination>
	</div><!--/.main-->
	<!-- 模态窗口 -->
	<div class="modal fade"  id="moreInfoWin">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
	        <h4 class="modal-title" id="myModalLabel"></h4>
	      </div>
	      <div class="modal-body">
	        	<table class="table table-striped table-hover">
						    <thead>
						    <tr>
						        <th>物理表名称</th>
						        <th>导出条数</th>
						    </tr>
						    </thead>
						    <tbody>
						    	<tr ng-repeat="x in detailList" >
						    		<td><span>{{x.expPhysDBName}}</span></td>
						    		<td><span>{{x.expDatas}}</span></td>
						    	</tr>
						    </tbody>
						</table>
	      </div>
	      <div class="modal-footer">
	       		<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
	      </div>
	    </div>
	  </div>
	</div>
	<form action="export/export.do" id="downloadForm">
			<button type="submit"  style="display:none" id="downloadSubmit"></button>
	</form>
</body>

</html>
