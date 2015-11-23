'use strict';

angular.module('adapApp')
    .controller('ObjtypeDetailController', function ($scope, $rootScope, $stateParams, entity, Objtype, Objcategory, Asset, Location, Score) {
        $scope.objtype = entity;
        $scope.load = function (id) {
            Objtype.get({id: id}, function(result) {
                $scope.objtype = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:objtypeUpdate', function(event, result) {
            $scope.objtype = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
