'use strict';

angular.module('adapApp')
	.controller('AttackscenarioDeleteController', function($scope, $uibModalInstance, entity, Attackscenario) {

        $scope.attackscenario = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Attackscenario.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
