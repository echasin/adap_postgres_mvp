'use strict';

angular.module('adapApp')
    .controller('ObjclassificationDetailController', function ($scope, $rootScope, $stateParams, entity, Objclassification, Objrecordtype, Objcategory, Asset, Location, Score) {
        $scope.objclassification = entity;
        $scope.load = function (id) {
            Objclassification.get({id: id}, function(result) {
                $scope.objclassification = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:objclassificationUpdate', function(event, result) {
            $scope.objclassification = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
