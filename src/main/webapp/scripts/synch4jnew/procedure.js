var app = angular.module('myapp',[]);
app.controller('myCtrl',function($scope,$http,procedureService){
	$http.get("procedure/getRemoteProcedure.do").success(function(response){
		$scope.data = response.result;
	});
	$scope.checkAll = function(isChecked){
		angular.forEach($scope.data,function(item){
			item.isSynch = isChecked;
		});
	};
	$scope.availableList=[{name:"启用",value:"1"},{name:"停用",value:"0"}];
	
	$scope.save = function(po){
		procedureService.save(po);
	};
	
	$scope.del = function(po){
		procedureService.del(po);
	}
	
	$scope.isChecked = function(synchObj){
		return synchObj.isSynch == "true";
	}
	
	$scope.updateChecked = function(po){
		
		if(po.isSynch == "true")
			po.isSynch = "false";
		else
			po.isSynch = "true";
	}
});

app.service('procedureService',function($http){
	this.save = function(po){
		po.editable = false;
		$http.post("procedure/saveRemoteProcedure.do",po).success(function(response){
			alert("保存成功");
		});
	};
	this.del = function(po){
		po.editable = false;
		var arr = new Array();
		arr.push(po.guid);
		$http.post("procedure/delRemoteProcedure.do",arr).success(function(response){
			po.hidden = true;
			alert("删除成功");
		});
	}
});