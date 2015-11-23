'use strict';

angular.module('adapApp')
    .controller('ScoreController', function ($scope, $state, $modal, Score, ScoreSearch, ParseLinks) {
      
        $scope.scores = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Score.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.scores = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ScoreSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.scores = result;
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
            $scope.score = {
                value: null,
                text: null,
                rulename: null,
                ruleversion: null,
                details: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
