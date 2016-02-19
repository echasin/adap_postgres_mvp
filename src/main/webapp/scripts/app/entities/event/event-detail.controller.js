'use strict';

angular.module('adapApp')
    .controller('EventDetailController', function ($scope, $rootScope, $stateParams, entity, Event, Objrecordtype, Objclassification, Objcategory, Objtype, Eventmbr) {
        $scope.event = entity;
        $scope.load = function (id) {
            Event.get({id: id}, function(result) {
                $scope.event = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:eventUpdate', function(event, result) {
            $scope.event = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
