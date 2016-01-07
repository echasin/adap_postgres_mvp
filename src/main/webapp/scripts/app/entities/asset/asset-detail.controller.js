'use strict';

angular.module('adapApp')
    .controller('AssetDetailController', function ($scope, $rootScope, $stateParams, entity, Asset, Objrecordtype, Objclassification, Objcategory, Objtype, Location, Score, Vulnerability, Identifier) {
        $scope.asset = entity;
        $scope.load = function (id) {
            Asset.get({id: id}, function(result) {
                $scope.asset = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:assetUpdate', function(event, result) {
            $scope.asset = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
