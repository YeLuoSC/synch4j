var app = angular.module('myapp',[]);
app.controller('myCtrl',function($scope,$http,configService){
	$http.get("config2/getSynchSettingList.do").success(function(response){
		$scope.data = response.result;
	});
	$scope.checkAll = function(isChecked){
		angular.forEach($scope.data,function(item){
			item.isSynch = isChecked;
		});
	};
	$scope.tableTypes=[{name:"普通表",value:"1"},{name:"附件表",value:"2"}];
	
	$scope.save = function(synchObj){
		configService.save(synchObj);
	};
});

app.service('configService',function($http){
	this.save = function(synchObj){
		console.log(synchObj);
	}
});