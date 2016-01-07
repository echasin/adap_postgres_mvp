'use strict';

angular.module('adapApp')
    .controller('LocationController', function ($scope, $state, $modal, Location, LocationSearch, ParseLinks) {
      
        $scope.locations = [];
        $scope.page = 0;
        $scope.loadAll = function() {
            Location.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.locations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.index = function () {
        	Location.index();
        };
        
        $scope.search = function () {
            LocationSearch.query({query: $scope.searchQuery}, function(result) {
                console.info('In LocationSearch.query');
                $scope.locations = result;
                console.info('$scope.locations', $scope.locations);
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
            $scope.location = {
                isprimary: null,
                address1: null,
                address2: null,
                cityname: null,
                citynamealias: null,
                countyname: null,
                countyfips: null,
                countyansi: null,
                statename: null,
                statecode: null,
                statefips: null,
                stateiso: null,
                stateansi: null,
                zippostcode: null,
                countryname: null,
                countryiso2: null,
                countryiso3: null,
                latitudedd: null,
                longitudedd: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
    });
