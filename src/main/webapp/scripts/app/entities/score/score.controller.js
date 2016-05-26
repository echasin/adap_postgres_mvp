'use strict';

angular
		.module('adapApp')
		.controller(
				'ScoreController',
				function($scope, $state, $resource, $modal, Score,
						ScoreService, ScoreSearch, Filter, ParseLinks) {

					$scope.scores = [];
					$scope.page = 0;
					$scope.loadAll = function() {
						Score.query({
							page : $scope.page,
							size : 20
						}, function(result, headers) {
							$scope.links = ParseLinks.parse(headers('link'));
							$scope.scores = result;
						});
					};
					$scope.loadPage = function(page) {
						$scope.page = page;
						// $scope.loadAll();
					};
					// $scope.loadAll();

					$scope.rulesName = Score.getRules();
					$scope.workFlowsName = Score.getWorkFlows();

					$scope.search = function() {
						console.log($scope.searchQuery)
						if ($scope.searchQuery == ""
								|| $scope.searchQuery == null) {
							$scope.searchdata = [];
							$scope.gridOptions.data = $scope.data
									.slice(
											(paginationOptions.pageNumber - 1)
													* paginationOptions.pageSize,
											((paginationOptions.pageNumber - 1) * paginationOptions.pageSize)
													+ paginationOptions.pageSize);
							getcount();
							getData();

						} else {
							ScoreSearch.query({
								query : $scope.searchQuery
							}, function(result) {
								$scope.gridOptions.totalItems = result.length;
								$scope.assets = result;
								$scope.searchdata = result;
								getPagesearch();
								console.log(result)
							});
						}
					};

					$scope.refresh = function() {
						$scope.loadAll();
						$scope.clear();
					};

					$scope.index = function() {
						Score.index();
					};

					$scope.fireRules = function() {
						Score.fireRules({
							filterId : $scope.filterId,
							fileName : $scope.fileName
						}, function(result) {
							console.log(result);
						});
					};

					$scope.fireWorkflows = function() {
						Score
								.fireWorkflows(
										{
											filterId : $scope.filterId,
											fileName : $scope.fileName
										},
										function(result) {
											$scope.workflow = result;
											for ( var keyName in $scope.workflow) {
												var key = keyName;
												var value = $scope.workflow[keyName];
												if (key == 'Route Found') {
													$scope.keyRoute = key;
													$scope.valueRoute = " Look for updated score below";
													getcount();
													getData();
												} else if (key == 'No Route Found') {
													$scope.keyRoute = key;
													$scope.valueRoute = " for this selection";
													getcount();
													getData();
												}

											}
											console.log(result);
										});
					};
					
					$scope.fireRouteWorkflows = function() {
						Score
								.fireRouteWorkflows(
										{
											routeId : $scope.routeId,
											fileName : $scope.fileName
										},
										function(routeResult) {
											$scope.routeworkflow = routeResult;
											for ( var keyName in $scope.routeworkflow) {
												var routeKey = keyName;
												var routeValue = $scope.routeworkflow[keyName];
												if (routeKey == 'Route Found Value') {
													$scope.routeKeyRoute = routeKey;
													$scope.routeValueRoute = " Look for updated score below";
													getcount();
													getData();
												} else if (routeKey == 'No Route Found Value') {
													$scope.routeKeyRoute = routeKey;
													$scope.routeValueRoute = " for this selection";
													getcount();
													getData();
												}

											}
											console.log(routeResult);
										});
					};

					$scope.loadFilters = function() {
						Filter.filtersByRecordtype({
							name : "Route"
						}, function(data) {
							$scope.filters = data;
						});
					}
					$scope.loadFilters();

					$scope.clear = function() {
						$scope.score = {
							value : null,
							text : null,
							rulename : null,
							ruleversion : null,
							details : null,
							status : null,
							lastmodifiedby : null,
							lastmodifieddate : null,
							domain : null,
							id : null
						};
					};

					var x = 0;
					var page = 0;
					var size = 2500;
					function getcount() {
						ScoreService.count().then(function(obj) {
							$scope.gridOptions.totalItems = obj;
							console.log(obj);
							return obj
						});
					}
					getcount();
					var paginationOptions = {
						pageNumber : 1,
						pageSize : 10,
						sort : null
					};
					$scope.gridOptions = {
						enableFiltering : true,
						paginationPageSizes : [ 10, 20, 50, 100 ],
						paginationPageSize : 10,
						useExternalPagination : true,
						useExternalSorting : false,
						columnDefs : [
								{
									field : 'id',
									displayName : 'ID',
									width : 60,
									enableSorting : true
								},
								{
									field : 'value',
									displayName : 'Value',
									enableSorting : true
								},
								{
									field : 'runid',
									displayName : 'Runid',
									enableSorting : true
								},
								{
									field : 'details',
									displayName : 'details',
									enableSorting : true
								},
								{
									field : 'rulename',
									displayName : 'rulename',
									enableSorting : true
								},
								{
									field : 'rulefilename',
									displayName : 'rulefilename',
									enableSorting : true
								},
								{
									field : 'domain',
									displayName : 'Domain',
									enableSorting : true
								},
								{
									name : 'Action',
									field : 'action',
									enableFiltering : false,
									enableSorting : false,
									cellTemplate : ' <button type="submit" ui-sref="score.detail({id:row.entity.id})" class="btn btn-info btn-sm"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;<span translate="entity.action.view"> View</span></button>'
											+ ' <button type="submit" ui-sref="score.edit({id:row.entity.id})" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>&nbsp;<span translate="entity.action.edit"> Edit</span></button>'
											+ ' <button type="submit" ng-click="grid.appScope.delete(row.entity.id)" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove-circle"></span>&nbsp;<span translate="entity.action.delete"> Delete</span></button>'
								} ],
						onRegisterApi : function(gridApi) {
							$scope.gridApi = gridApi;
							$scope.gridApi.core.on
									.sortChanged(
											$scope,
											function(grid, sortColumns) {
												if (sortColumns.length == 0) {
													paginationOptions.sort = null;
												} else {
													paginationOptions.sort = sortColumns[0].sort.direction;
												}
												getPage();
											});
							$scope.gridApi.core.on
									.filterChanged(
											$scope,
											function() {
												if ($scope.searchdata != null
														&& $scope.searchdata.length > 0) {
													$scope.gridOptions.data = $scope.searchdata;
												} else {
													$scope.gridOptions.data = $scope.data;
												}

											});
							gridApi.pagination.on
									.paginationChanged(
											$scope,
											function(newPage, pageSize) {
												console.log(newPage)
												if ($scope.searchdata != null
														&& $scope.searchdata.length > 0) {
													paginationOptions.pageNumber = newPage;
													paginationOptions.pageSize = pageSize;
													getPagesearch();
												} else {
													var length = $scope.gridOptions.totalItems
															/ size;
													var x = size
															/ (paginationOptions.pageSize);
													Number.prototype.between = function(
															min, max) {
														return this >= min
																&& this <= max;
													};
													for (var i = 1; i < length; i++) {
														if ((newPage).between(x
																* i, (i + 1)
																* x)) {
															page = i;
															loadMore(size * i);
														}
													}
													getPage();
													paginationOptions.pageNumber = newPage;
													paginationOptions.pageSize = pageSize;
												}
											});
						}
					};

					function getData() {
						var sasa = $resource('api/scores/:page/:size', {
							page : page,
							size : size
						}).query();
						$scope.data = sasa;
					}

					function loadMore(startposition) {
						if ($scope.data[startposition + 1] != null) {
							console.log("required data are available")
						} else {
							Asset
									.getassets(
											{
												page : page,
												size : size
											},
											function(data, headers) {
												console
														.log("new data has loaded");
												for (var i = 0; i < data.length; ++i) {
													$scope.data[startposition
															+ i] = data[i];
												}
												$scope.gridOptions.data = $scope.data
														.slice(
																(paginationOptions.pageNumber - 1)
																		* paginationOptions.pageSize,
																((paginationOptions.pageNumber - 1) * paginationOptions.pageSize)
																		+ paginationOptions.pageSize);
											});
						}
					}

					getData();

					function getPage() {
						$scope.data.$promise
								.then(function(result) {
									var firstRow = (paginationOptions.pageNumber - 1)
											* paginationOptions.pageSize;
									$scope.gridOptions.data = $scope.data
											.slice(
													firstRow,
													firstRow
															+ paginationOptions.pageSize);

									$scope.chartdata = []
									for (var i = 0; i < $scope.gridOptions.data.length; ++i) {
										$scope.chartdata
												.push({
													x : $scope.gridOptions.data[i].name,
													y : $scope.gridOptions.data[i].id
												})
									}
								});
					}

					getPage();

					function getPagesearch() {
						$scope.gridOptions.data = $scope.searchdata
								.slice(
										(paginationOptions.pageNumber - 1)
												* paginationOptions.pageSize,
										((paginationOptions.pageNumber - 1) * paginationOptions.pageSize)
												+ paginationOptions.pageSize);

						$scope.chartdata = []
						for (var i = 0; i < $scope.gridOptions.data.length; ++i) {
							$scope.chartdata.push({
								x : $scope.gridOptions.data[i].name,
								y : $scope.gridOptions.data[i].id
							})
						}
					}
				});
