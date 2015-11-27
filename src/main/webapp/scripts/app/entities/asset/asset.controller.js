'use strict';
angular.module('adapApp')
        .controller('AssetController', function ($scope, $state, $modal, Asset, AssetSearch, ParseLinks)
        {
            console.info('In asset.controller.js');
            $scope.assets = [];
            $scope.page = 0;
            $scope.loadAll = function () {
                Asset.query({page: $scope.page, size: 2500}, function (result, headers) {
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.assets = result;
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
                }, function (response) {
                    if (response.status === 404) {
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
                paginationPageSizes: [5, 10, 25, 50, 75],
                paginationPageSize: 10,
                enableColumnResizing: true,
                enableSorting: true,
                enableFiltering: true,
                enableScrollbars: false,
                enableGridMenu: true,
                enableSelectAll: true,
                exporterCsvFilename: 'myFile.csv',
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
        });
