var app = angular.module('myapp',['tm.pagination']);
app.controller('myCtrl',function($rootScope,$http,procedureService){
	
	
	//配置分页基本参数
	$rootScope.paginationConf = {
		page:{
			pageNum: 1,
			pageSize: 20
		},
		callback:{
			onChange:function(){
	        	procedureService.getData($rootScope);
	        }
		}
    };
});
app.controller('tableCtrl',function($scope,$http,procedureService){	
	$scope.checkAll = function(isChecked){
		angular.forEach($scope.data,function(item){
			item.isSelected = isChecked;
		});
	};
	$scope.availableList=[{name:"启用",value:"1"},{name:"停用",value:"0"}];
	
	$scope.save = function(po){
		procedureService.save(po);
	};
	
	$scope.del = function(po,index){
		procedureService.del(po,index);
	};
	
	$scope.isChecked = function(po){
		return po.isSelected == true;
	};
	
	$scope.updateChecked = function(po){
		if(po.isSelected == true)
			po.isSelected = false;
		else
			po.isSelected = true;
	};
	
	$scope.delBatchProcedure = function(){
		procedureService.delBatchProcedure($scope.data);
	};
});

app.controller('addCtrl',function($scope,$http,procedureService){
	
	$scope.availableList=[{name:"启用",value:"1"},{name:"停用",value:"0"}];
	
	$scope.addProcedure = function(proPO){
		procedureService.addProcedure(proPO,$scope);
	};
});


app.service('procedureService',function($http){
	var proData = {};
	var me = this;
	this.getData = function(scope){
		$http.post("procedure/getRemoteProcedure.do",scope.paginationConf.page).success(function(response){
			scope.paginationConf.page.total = response.total;
			scope.data = response.list;
			proData = scope.data;
		});
	};
	
	this.save = function(po){
		po.editable = false;
		$http.post("procedure/saveRemoteProcedure.do",po).success(function(response){
			alert("保存成功");
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
	this.addProcedure = function(proPO,scope){
		$http.post('procedure/saveRemoteProcedure.do',proPO).success(function(response){
			//me.getData(scope);
			proPO.guid = response.success;
			var cloneObj = {};
			$.extend(cloneObj,proPO);
			scope.data.push(cloneObj);
			alert("创建成功");
			me.clearProPO(proPO);
			$("#addWin").modal('hide');
		}).error(function(){
			alert("error");
		});
	};
	
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
	
	this.clearProPO = function(proPO){
		proPO.guid = "";
		proPO.name = "";
		proPO.available = 0;
		proPO.programCode = "";
		proPO.describe = "";
	};
});
