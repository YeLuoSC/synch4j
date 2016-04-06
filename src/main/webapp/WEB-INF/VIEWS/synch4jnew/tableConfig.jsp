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
<link href="<%=path %>/css/bootstrap-table.css" rel="stylesheet">
<link href="<%=path %>/css/styles.css" rel="stylesheet">
<script src="<%=path %>/scripts/js/jquery-1.11.1.min.js"></script>
<script src="<%=path %>/scripts/js/angular.min.js"></script>
<script src="<%=path %>/scripts/js/bootstrap.min.js"></script>
<script src="<%=path %>/scripts/synch4jnew/tableConfig.js"></script>
<%--<script src="<%=path %>/scripts/js/bootstrap-table.js"></script>


--%><script>
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

<body ng-app="myapp">
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
			<li class="active"><a href="tables.html"><span class="glyphicon glyphicon-list-alt"></span>数据同步设置</a></li>
			<li><a href="index.html"><span class="glyphicon glyphicon-info-sign"></span>远程脚本执行设置</a></li>
			<li><a href="widgets.html"><span class="glyphicon glyphicon-th"></span>标准模式导出</a></li>
			<li><a href="charts.html"><span class="glyphicon glyphicon-pencil"></span>数据导入</a></li>
			<%--<li><a href="forms.html"><span class="glyphicon glyphicon-pencil"></span> Forms</a></li>
			--%><li><a href="panels.html"><span class="glyphicon glyphicon-info-sign"></span>关于作者</a></li>
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
		
	<div class="col-sm-9 col-sm-offset-3 col-lg-10 col-lg-offset-2 main">			
		<div class="row">
			<ol class="breadcrumb">
				<li><a href="#"><span class="glyphicon glyphicon-home"></span></a></li>
				<li class="active">数据同步设置</li>
			</ol>
		</div><!--/.row-->
		
		<div class="row">
			<div class="col-lg-12">
				<h1 class="page-header">导出表格设置</h1>
				<p>下列设置表格中，单次导出最大行数、时间戳列名均为增量导出配置项，可不录入信息</p>
			</div>
		</div><!--/.row-->
				
		
		<div class="row"  ng-controller="myCtrl">
			<div class="col-md-12">
				<div class="panel panel-default">
					<div class="panel-heading">Advanced Table</div>
					<div class="panel-body">
						<table class="table table-striped table-hover">
						    <thead>
						    <tr>
						        <th><input type="checkbox"  ng-model="allchecked" ng-change="checkAll(allchecked)"/></th>
						        <th>导入时顺序</th>
						        <th>物理表名</th>
						        <th>表类型</th>
						        <th>单次导出最大行数</th>
						        <th>时间戳列名</th>
						        <th>导出条件设置</th>
						        <th>过滤列</th>
						        <th>操作</th>
						    </tr>
						    </thead>
						    <tbody>
						    	<tr ng-repeat="x in data">
						    		<td><input type="checkbox" ng-model="x.isSynch"/></td>
						    		<td><span ng-if="!x.editable">{{x.synchOrder}}</span><input type="number" class="form-control" ng-if="x.editable" ng-model="x.synchOrder"/></td>
						    		<td>{{x.physDBName}}</td>
									<td>
										<span ng-if="!x.editable"><span ng-if="x.tableType==1">普通表</span><span ng-if="x.tableType==2">附件表</span></span>
										<span ng-if="x.editable">
											<%--<select class="form-control" ng-model="x.tableType">
												<option ng-repeat="tableType in tableTypes" value="{{tableType.value}}">{{tableType.name}}</option>
											</select>
										--%>
											<select class="form-control" ng-model="x.tableType" ng-options="tableType.value as tableType.name for tableType in tableTypes">
												
											</select>
										</span>
									</td>
									<td><span ng-if="!x.editable">{{x.maxRow}}</span><span ng-if="x.editable"><input type="number" class="form-control" ng-model="x.maxRow"/></span></td>
						    		<td><span ng-if="!x.editable">{{x.synchRecogCol}}</span><span ng-if="x.editable"><input type="text" class="form-control" ng-model="x.synchRecogCol"/></span></td>
						    		<td><span ng-if="!x.editable">{{x.condition}}</span><span ng-if="x.editable"><input type="text" class="form-control" ng-model="x.condition"/></span></td>
						    		<td><span ng-if="!x.editable">{{x.filterCol}}</span><span ng-if="x.editable"><input type="text" class="form-control" ng-model="x.filterCol"/></span></td>
						    		<td>
						    			<span class="btn btn-primary btn-xs" title="编辑" ng-click="x.editable=true" ng-if="!x.editable"><i class="glyphicon glyphicon-pencil"></i></span>
						    			<span class="btn btn-primary btn-xs" title="保存" ng-click="x.editable=false" ng-if="x.editable"><i class="glyphicon glyphicon-floppy-disk"></i></span>
						    		</td>
						    	</tr>
						    </tbody>
						</table>
					</div>
				</div>
			</div>
		</div><!--/.row-->	
		<div class="row">
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">Basic Table</div>
					<div class="panel-body">
						<table data-toggle="table" data-url="tables/data2.json" >
						    <thead>
						    <tr>
						        <th data-field="id" data-align="right">Item ID</th>
						        <th data-field="name">Item Name</th>
						        <th data-field="price">Item Price</th>
						    </tr>
						    </thead>
						</table>
					</div>
				</div>
			</div>
			<div class="col-md-6">
				<div class="panel panel-default">
					<div class="panel-heading">Styled Table</div>
					<div class="panel-body">
						<table data-toggle="table" id="table-style" data-url="tables/data2.json" data-row-style="rowStyle">
						    <thead>
						    <tr>
						        <th data-field="id" data-align="right" >Item ID</th>
						        <th data-field="name" >Item Name</th>
						        <th data-field="price" >Item Price</th>
						    </tr>
						    </thead>
						</table>
						<script>
						    $(function () {
						        $('#hover, #striped, #condensed').click(function () {
						            var classes = 'table';
						
						            if ($('#hover').prop('checked')) {
						                classes += ' table-hover';
						            }
						            if ($('#condensed').prop('checked')) {
						                classes += ' table-condensed';
						            }
						            $('#table-style').bootstrapTable('destroy')
						                .bootstrapTable({
						                    classes: classes,
						                    striped: $('#striped').prop('checked')
						                });
						        });
						    });
						
						    function rowStyle(row, index) {
						        var classes = ['active', 'success', 'info', 'warning', 'danger'];
						
						        if (index % 2 === 0 && index / 2 < classes.length) {
						            return {
						                classes: classes[index / 2]
						            };
						        }
						        return {};
						    }
						</script>
					</div>
				</div>
			</div>
		</div><!--/.row-->	
		
		
	</div><!--/.main-->

</body>

</html>
