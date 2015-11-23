'use strict';

angular.module('adapApp')
    .controller('ObjrecordtypeDetailController', function ($scope, $rootScope, $stateParams, entity, Objrecordtype, Objclassification, Asset, Location, Score) {
        $scope.objrecordtype = entity;
        $scope.load = function (id) {
            Objrecordtype.get({id: id}, function(result) {
                $scope.objrecordtype = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:objrecordtypeUpdate', function(event, result) {
            $scope.objrecordtype = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
