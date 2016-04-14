var app = angular.module('myapp',['tm.pagination']);
app.controller('myCtrl',function($scope,$http,importService){
	
	//配置分页基本参数
	$scope.paginationConf = {
		page:{
			pageNum: 1,
			pageSize: 20
		},
		callback:{
			onChange:function(){
	        	importService.getData($scope);
	        }
		}
    };
	
	$scope.checkAll = function(isChecked){
		angular.forEach($scope.data,function(item){
			item.isSelected = isChecked;
		});
	};
	
	$scope.moreInfo = function(po){
		importService.moreInfo(po,$scope);
	};
	
	$scope.import$ = function(){
		importService.import$();
	}
	$scope.del = function(po,index){
		importService.del(po,index);
	};
	
	$scope.updateChecked = function(po){
		if(po.isSelected == true)
			po.isSelected = true;
		else
			po.isSelected = false;
	};
	
	$scope.delBatchProcedure = function(){
		importService.delBatchProcedure($scope.data);
	};
});

app.service('importService',function($http){
	var proData = {};
	var me = this;
	this.getData = function(scope){
		$http.post("import/getImportMainLog.do",scope.paginationConf.page).success(function(response){
			scope.paginationConf.page.total = response.total;
			scope.data = response.list;
			proData = scope.data;
		});
	};
	
	this.moreInfo = function(po,scope){
		$("#moreInfoWin").modal('show');
		$("#myModalLabel").html("详细导入日志信息:"+po.fileName);
		$http.post("import/getImportDetail.do",po.logId).success(function(response){
			scope.detailList = response;
		});
	};
	
	this.del = function(po,index){
		po.editable = false;
		var arr = new Array();
		arr.push(po.guid);
		$http.post("procedure/delRemoteProcedure.do",arr).success(function(response){
			po.hidden = true;
			//proData.splice(index,1);
			alert("删除成功");
		});
	};
	
	this.download = function(po,scope){
		$http.post('export/download.do',po).success(function(response){
			
		}).error(function(response){
			alert(response);
		});
	};
	
	this.import$ = function(){
		$("#downloadForm").submit();
	}
	this.delBatchProcedure = function(arr){
		var idArr = new Array();
		angular.forEach(arr, function(po,index,array){
			 //data等价于array[index]
			if(po.isSelected == true){
				idArr.push(po.guid);
			}
		});
		if(idArr.length > 0){
			$http.post("procedure/delRemoteProcedure.do",idArr).success(function(response){
				angular.forEach(arr, function(po,index,array){
					 //data等价于array[index]
					if(po.isSelected == true){
						po.hidden = true;
					}
				});
				alert("删除成功");
			});
		}
		
	};
});