'use strict';

angular.module('adapApp')
    .controller('RouteDetailController', function ($scope, $rootScope, $stateParams, entity, Route, Objrecordtype, Objclassification, Objcategory, Objtype, Segment) {
        $scope.route = entity;
        $scope.load = function (id) {
            Route.get({id: id}, function(result) {
                $scope.route = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:routeUpdate', function(event, result) {
            $scope.route = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
