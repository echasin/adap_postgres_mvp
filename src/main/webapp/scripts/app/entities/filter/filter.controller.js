'use strict';

angular.module('adapApp')
    .controller('FilterController', function ($scope, Filter, FilterSearch, ParseLinks) {
        $scope.filters = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Filter.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.filters = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Filter.get({id: id}, function(result) {
                $scope.filter = result;
                $('#deleteFilterConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Filter.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteFilterConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            FilterSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.filters = result;
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
            $scope.filter = {
                name: null,
                description: null,
                querysql: null,
                queryspringdata: null,
                queryelastic: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
        
      
    });

