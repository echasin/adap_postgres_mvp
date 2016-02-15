'use strict';

angular.module('adapApp')
	.controller('ScorefactorDeleteController', function($scope, $uibModalInstance, entity, Scorefactor) {

        $scope.scorefactor = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Scorefactor.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
