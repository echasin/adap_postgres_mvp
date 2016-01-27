'use strict';

angular.module('adapApp')
    .controller('EventmbrController', function ($scope, $state, Eventmbr, EventmbrSearch, ParseLinks) {

        $scope.eventmbrs = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Eventmbr.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.eventmbrs = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            EventmbrSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.eventmbrs = result;
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
            $scope.eventmbr = {
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
