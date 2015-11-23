'use strict';

angular.module('adapApp')
    .controller('ObjtypeController', function ($scope, $state, $modal, Objtype, ObjtypeSearch, ParseLinks) {
      
        $scope.objtypes = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Objtype.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.objtypes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ObjtypeSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.objtypes = result;
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
            $scope.objtype = {
                name: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
