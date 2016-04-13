var app = angular.module('myapp',['tm.pagination']);
app.controller('myCtrl',function($scope,$http,configService){
	//配置分页基本参数
	$scope.paginationConf = {
		page:{
			pageNum: 1,
			pageSize: 20
		},
		callback:{
			onChange:function(){
				$http.post("config2/getSynchSettingList.do",$scope.paginationConf.page).success(function(response){
					$scope.paginationConf.page.total = response.total;
					$scope.data = response.list;
				});
	        }
		}
    };
	
	$scope.checkAll = function(isChecked){
		angular.forEach($scope.data,function(item){
			item.isSynch = isChecked;
		});
	};
	$scope.tableTypes=[{name:"普通表",value:"1"},{name:"附件表",value:"2"}];
	
	$scope.save = function(synchPO){
		configService.save(synchPO);
	};
	
	$scope.del = function(synchPO){
		configService.del(synchPO);
	}
	
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
			alert("保存成功");
		}).error(function(){
			alert("保存失败！");
		});
	};
	this.del = function(synchPO){
		synchPO.editable = false;
		synchPO.isSynch = "false";
		$http.post("config2/delSynchPO.do",synchPO.physDBName).success(function(response){
			alert("删除成功");
		}).error(function(){
			alert("删除失败！");
		});
	}
});