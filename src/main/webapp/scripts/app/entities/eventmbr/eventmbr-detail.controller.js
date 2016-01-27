'use strict';

angular.module('adapApp')
    .controller('EventmbrDetailController', function ($scope, $rootScope, $stateParams, entity, Eventmbr, Event, Asset) {
        $scope.eventmbr = entity;
        $scope.load = function (id) {
            Eventmbr.get({id: id}, function(result) {
                $scope.eventmbr = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:eventmbrUpdate', function(event, result) {
            $scope.eventmbr = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
