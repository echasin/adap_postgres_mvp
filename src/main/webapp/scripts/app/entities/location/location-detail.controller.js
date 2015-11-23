'use strict';

angular.module('adapApp')
    .controller('LocationDetailController', function ($scope, $rootScope, $stateParams, entity, Location, Objrecordtype, Objclassification, Objcategory, Objtype, Asset) {
        $scope.location = entity;
        $scope.load = function (id) {
            Location.get({id: id}, function(result) {
                $scope.location = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:locationUpdate', function(event, result) {
            $scope.location = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
