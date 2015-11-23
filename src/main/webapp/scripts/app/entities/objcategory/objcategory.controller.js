'use strict';

angular.module('adapApp')
    .controller('ObjcategoryController', function ($scope, $state, $modal, Objcategory, ObjcategorySearch, ParseLinks) {
      
        $scope.objcategorys = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Objcategory.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.objcategorys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ObjcategorySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.objcategorys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.objcategory = {
                name: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
