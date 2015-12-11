'use strict';
angular.module('adapApp')
        .controller('AssetController', function ($scope, $state,$http, $filter, $modal, Asset, AssetSearch, uiGridConstants, ParseLinks)
        {
            console.info('In asset.controller.js');
            $scope.assets = [];
            $scope.page = 0;
            $scope.loadAll = function () {
                Asset.query({page: $scope.page, size: 2500}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.assets = result;
                    console.info('In AssetController - $scope.assets:' + $scope.assets);
                    console.info('In AssetController - selectedCount:' + $scope.assets.length);
                });
            };
            $scope.loadPage = function (page) {
                $scope.page = page;
                $scope.loadAll();
            };
            $scope.loadAll();
            $scope.search = function () {
                console.info('In $scope.search');
                AssetSearch.query({query: $scope.searchQuery}, function (result) {
                    console.info('In AssetSearchquery');
                    $scope.assets = result;
                    console.info('$scope.assets' + $scope.assets);
                }, function (response) {
                    if (response.status === 404) {
                        console.info('In AssetSearchquery 404 Error');
                        $scope.loadAll();
                    }
                });
            };
            $scope.refresh = function () {
                $scope.loadAll();
                $scope.clear();
            };
            $scope.clear = function () {
                $scope.asset = {
                    name: null,
                    //description: null,
                    //details: null,
                    //status: null,
                    //lastmodifiedby: null,
                    //lastmodifieddate: null,
                    //domain: null,
                    id: null
                };
            };
            $scope.gridAssets = {
                paginationPageSizes: [5, 10, 25, 50, 75, 100],
                paginationPageSize: 10,
                //showGridFooter: true,
                //enableColumnResizing: true,
                //flatEntityAccess: true,
                // enableSorting: true,
                //enableFiltering: true,
                //enableScrollbars: false,
                //enableGridMenu: true,
                //enableSelectAll: true,
                //exporterCsvFilename: 'myFile.csv',
                //data: [{
                //        "id": "1",
                //        "name": "Eric"
                //    }],
                data: 'assets',
                columnDefs: [
                    {name: 'id', field: 'id'},
                    {name: 'name', field: 'name'},
                    {name: 'country', field: 'asset.location.countryname'},
                    {name: 'Action ', enableSorting: false, enableFiltering: false, width: '10%', cellTemplate:
                                '<div>   \n\
                              <center><button type="submit"  ui-sref="asset.detail({id:asset.id})" class="btn btn-info btn-xs"><span class="glyphicon glyphicon-eye-open"></button>\n\
                              <button type="submit"  ui-sref="asset.edit({id:asset.id})"   class="btn btn-primary btn-xs"><span class="glyphicon glyphicon-pencil"></button>\n\
                              <button type="submit"  ui-sref="asset.delete({id:asset.id})" class="btn btn-danger btn-xs"><span class="glyphicon glyphicon-remove-circle"></button>\n\
                              </center>\n\
                    </div>'
                    }
                ]
            };
            var paginationOptions = {
                pageNumber: 1,
                pageSize: 2500,
                sort: null
            };
            $scope.gridOptions = {
                paginationPageSizes: [25, 50, 75, 100],
                paginationPageSize: 25,
                useExternalPagination: true,
                useExternalSorting: true,
                columnDefs: [
                    {name: 'id', field: 'id'},
                    {name: 'name', field: 'name'},
                    {name: 'country', field: 'asset.location.countryname'},
                ],
                onRegisterApi: function (gridApi) {
                    $scope.gridApi = gridApi;
                    $scope.gridApi.core.on.sortChanged($scope, function (grid, sortColumns) {
                        if (sortColumns.length == 0) {
                            paginationOptions.sort = null;
                        } else {
                            paginationOptions.sort = sortColumns[0].sort.direction;
                        }
                        getPage();
                    });
                    gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                        paginationOptions.pageNumber = newPage;
                        paginationOptions.pageSize = pageSize;
                        getPage();
                    });
                }
            };
            var getPage = function () {
                var url;
                switch (paginationOptions.sort) {
                    case uiGridConstants.ASC:
                        url = '/api/assets?size=2500';
                        break;
                    case uiGridConstants.DESC:
                        url = '/api/assets?size=2500';
                        break;
                    default:
                        url = '/api/assets?size=2500';
                        break;
                }
                $http.get(url)
                        .success(function (data) {
                            $scope.gridOptions.totalItems = 25005;
                            console.info('$scope.gridOptions' + $scope.gridOptions);
                            var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
                            $scope.gridOptions.data = data.slice(firstRow, firstRow + paginationOptions.pageSize);
                        });
            };

            getPage();
        });
 
