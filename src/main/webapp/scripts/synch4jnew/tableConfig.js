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
	
	$scope.isChecked = function(synchObj){
		return synchObj.isSynch == "true";
	}
	
	$scope.updateChecked = function(synchPO){
		if(synchPO.isSynch == "true")
			synchPO.isSynch = "false";
		else
			synchPO.isSynch = "true";
	}
});

app.service('configService',function($http){
	this.save = function(synchPO){
		synchPO.editable = false;
		$http.post("config2/saveSynchPO.do",synchPO).success(function(response){
			console.log(response);
		});
	};
});