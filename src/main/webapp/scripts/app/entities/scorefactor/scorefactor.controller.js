'use strict';

angular.module('adapApp')
    .controller('ScorefactorController', function ($scope, $state, Scorefactor, ScorefactorSearch, ParseLinks) {

        $scope.scorefactors = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Scorefactor.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.scorefactors = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ScorefactorSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.scorefactors = result;
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
            $scope.scorefactor = {
                name: null,
                description: null,
                matchattribute: null,
                matchvalue: null,
                scorevalue: null,
                scoretext: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
