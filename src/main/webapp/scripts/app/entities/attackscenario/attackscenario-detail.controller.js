'use strict';

angular.module('adapApp')
    .controller('AttackscenarioDetailController', function ($scope, $rootScope, $stateParams, entity, Attackscenario, Objrecordtype, Objclassification, Objcategory, Objtype) {
        $scope.attackscenario = entity;
        $scope.load = function (id) {
            Attackscenario.get({id: id}, function(result) {
                $scope.attackscenario = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:attackscenarioUpdate', function(event, result) {
            $scope.attackscenario = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
