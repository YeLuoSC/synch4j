/**
 * name: tm.pagination
 * Version: 0.0.2
 * conf = {
 * 	setting:{
 * 				perPageOptions:[],
 * 				pagesLength:int
 * 			},
 * 	page:{
 * 			pageNum:当前页
 * 			pageSize:每页的数量
 * 			size:当前页的数量
 * 			total:总记录数
 * 			pages：总页数
 * 		},
 * 	list:[]数据集合
 * 	}
 * 

 */
angular
		.module('tm.pagination', [])
		.directive(
				'tmPagination',
				[ function() {
					return {
						restrict : 'EA',
						template : '<div class="page-list">'
								+ '<ul class="pagination" ng-show="conf.page.total > 0">'
								+ '<li ng-class="{disabled: conf.page.pageNum == 1}" ng-click="prevPage()"><span>&laquo;</span></li>'
								+ '<li ng-repeat="item in pageList track by $index" ng-class="{active: item == conf.page.pageNum, separate: item == \'...\'}" '
								+ 'ng-click="changeCurrentPage(item)">'
								+ '<span>{{ item }}</span>'
								+ '</li>'
								+ '<li ng-class="{disabled: conf.page.pageNum  == conf.page.pages}" ng-click="nextPage()"><span>&raquo;</span></li>'
								+ '</ul>'
								+ '<div class="page-total" ng-show="conf.page.total > 0">'
								+ '第<input type="text" ng-model="jumpPageNum"  ng-keyup="jumpToPage($event)" style="width:30px"/>页 '
								+ '每页<select ng-model="conf.page.pageSize" ng-options="option for option in conf.setting.perPageOptions "></select>'
								+ '/共<strong>{{ conf.page.total }}</strong>条'
								+ '</div>'
								+ '<div class="no-items" ng-show="conf.page.total <= 0">暂无数据</div>'
								+ '</div>',
						replace : true,
						scope : {
							conf : '='
						},
						link : function(scope, element, attrs) {

							if(!scope.conf.setting){
								scope.conf.setting = {};
							}
							
							// 变更当前页
							scope.changeCurrentPage = function(item) {
								if (item == '...') {
									return;
								} else {
									scope.conf.page.pageNum = item;
								}
							};

							// 定义分页的长度必须为奇数 (default:9)
							
							scope.conf.setting.pagesLength = parseInt(scope.conf.setting.pagesLength) ? parseInt(scope.conf.setting.pagesLength)
									: 9;
							if (scope.conf.setting.pagesLength % 2 === 0) {
								// 如果不是奇数的时候处理一下
								scope.conf.setting.pagesLength = scope.conf.setting.pagesLength - 1;
							}

							// conf.erPageOptions
							if (!scope.conf.setting.perPageOptions) {
								scope.conf.setting.perPageOptions = [ 10, 15, 20, 30, 50 ];
							}

							// pageList数组
							function getPagination(newValue, oldValue) {

								// conf.pageNum
								scope.conf.page.pageNum = parseInt(scope.conf.page.pageNum) ? parseInt(scope.conf.page.pageNum)
										: 1;

								// conf.total
								scope.conf.page.total = parseInt(scope.conf.page.total) ? parseInt(scope.conf.page.total)
										: 0;

								// conf.pageSize (default:15)
								scope.conf.page.pageSize = parseInt(scope.conf.page.pageSize) ? parseInt(scope.conf.page.pageSize)
										: 15;

								// pages
								scope.conf.page.pages = Math
										.ceil(scope.conf.page.total
												/ scope.conf.page.pageSize);

								// judge pageNum > scope.pages
								if (scope.conf.page.pageNum < 1) {
									scope.conf.page.pageNum = 1;
								}

								// 如果分页总数>0，并且当前页大于分页总数
								if (scope.conf.page.pages > 0
										&& scope.conf.page.pageNum > scope.conf.page.pages) {
									scope.conf.page.pageNum = scope.conf.page.pages;
								}

								// jumpPageNum
								scope.jumpPageNum = scope.conf.page.pageNum;

								// 如果pageSize在不在perPageOptions数组中，就把pageSize加入这个数组中
								var perPageOptionsLength = scope.conf.setting.perPageOptions.length;
								// 定义状态
								var perPageOptionsStatus;
								for ( var i = 0; i < perPageOptionsLength; i++) {
									if (scope.conf.setting.perPageOptions[i] == scope.conf.page.pageSize) {
										perPageOptionsStatus = true;
									}
								}
								// 如果pageSize在不在perPageOptions数组中，就把pageSize加入这个数组中
								if (!perPageOptionsStatus) {
									scope.conf.setting.perPageOptions
											.push(scope.conf.page.pageSize);
								}

								// 对选项进行sort
								scope.conf.setting.perPageOptions.sort(function(a, b) {
									return a - b
								});

								scope.pageList = [];
								if (scope.conf.page.pages <= scope.conf.setting.pagesLength) {
									// 判断总页数如果小于等于分页的长度，若小于则直接显示
									for (i = 1; i <= scope.conf.page.pages; i++) {
										scope.pageList.push(i);
									}
								} else {
									// 总页数大于分页长度（此时分为三种情况：1.左边没有...2.右边没有...3.左右都有...）
									// 计算中心偏移量
									var offset = (scope.conf.setting.pagesLength - 1) / 2;
									if (scope.conf.page.pageNum <= offset) {
										// 左边没有...
										for (i = 1; i <= offset + 1; i++) {
											scope.pageList.push(i);
										}
										scope.pageList.push('...');
										scope.pageList
												.push(scope.conf.page.pages);
									} else if (scope.conf.page.pageNum > scope.conf.page.pages
											- offset) {
										scope.pageList.push(1);
										scope.pageList.push('...');
										for (i = offset + 1; i >= 1; i--) {
											scope.pageList
													.push(scope.conf.page.pages
															- i);
										}
										scope.pageList
												.push(scope.conf.page.pages);
									} else {
										// 最后一种情况，两边都有...
										scope.pageList.push(1);
										scope.pageList.push('...');

										for (i = Math.ceil(offset / 2); i >= 1; i--) {
											scope.pageList
													.push(scope.conf.page.pageNum
															- i);
										}
										scope.pageList
												.push(scope.conf.page.pageNum);
										for (i = 1; i <= offset / 2; i++) {
											scope.pageList
													.push(scope.conf.page.pageNum
															+ i);
										}

										scope.pageList.push('...');
										scope.pageList
												.push(scope.conf.page.pages);
									}
								}

								if (scope.conf.callback.onChange) {

									// 防止初始化两次请求问题
									if (!(oldValue != newValue && oldValue[0] == 0)) {
										scope.conf.callback.onChange();
									}

								}
								scope.$parent.conf = scope.conf;
							}

							// prevPage
							scope.prevPage = function() {
								if (scope.conf.page.pageNum > 1) {
									scope.conf.page.pageNum -= 1;
								}
							};
							// nextPage
							scope.nextPage = function() {
								if (scope.conf.page.pageNum < scope.conf.page.pages) {
									scope.conf.page.pageNum += 1;
								}
							};

							// 跳转页
							scope.jumpToPage = function() {
								scope.jumpPageNum = scope.jumpPageNum.replace(
										/[^0-9]/g, '');
								if (scope.jumpPageNum !== '') {
									scope.conf.page.pageNum = scope.jumpPageNum;
								}
							};

							scope.$watch(function() {

								if (!scope.conf.page.total) {
									scope.conf.page.total = 0;
								}

								var newValue = scope.conf.page.total + ' '
										+ scope.conf.page.pageNum + ' '
										+ scope.conf.page.pageSize;

								return newValue;

							}, getPagination);

						}
					};
				} ]);