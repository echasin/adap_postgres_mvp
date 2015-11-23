'use strict';

angular.module('adapApp')
    .controller('ObjcategoryDetailController', function ($scope, $rootScope, $stateParams, entity, Objcategory, Objclassification, Objtype, Asset, Location, Score) {
        $scope.objcategory = entity;
        $scope.load = function (id) {
            Objcategory.get({id: id}, function(result) {
                $scope.objcategory = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:objcategoryUpdate', function(event, result) {
            $scope.objcategory = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
