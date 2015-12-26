'use strict';

angular.module('adapApp')
    .controller('IdentifierDetailController', function ($scope, $rootScope, $stateParams, entity, Identifier, Objrecordtype, Objclassification, Objcategory, Objtype, Asset, Vulnerability, Attackscenario) {
        $scope.identifier = entity;
        $scope.load = function (id) {
            Identifier.get({id: id}, function(result) {
                $scope.identifier = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:identifierUpdate', function(event, result) {
            $scope.identifier = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
