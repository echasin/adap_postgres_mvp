'use strict';

angular.module('adapApp')
    .controller('ObjrecordtypeController', function ($scope, $state, $modal, Objrecordtype, ObjrecordtypeSearch, ParseLinks) {
      
        $scope.objrecordtypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Objrecordtype.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.objrecordtypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ObjrecordtypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.objrecordtypes = result;
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
            $scope.objrecordtype = {
                objecttype: null,
                name: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
