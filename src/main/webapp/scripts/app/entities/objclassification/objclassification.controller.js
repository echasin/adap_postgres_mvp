'use strict';

angular.module('adapApp')
    .controller('ObjclassificationController', function ($scope, $state, $modal, Objclassification, ObjclassificationSearch, ParseLinks) {
      
        $scope.objclassifications = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Objclassification.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.objclassifications = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ObjclassificationSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.objclassifications = result;
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
            $scope.objclassification = {
                name: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
