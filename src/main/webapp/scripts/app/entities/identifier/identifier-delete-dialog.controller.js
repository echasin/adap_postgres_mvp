'use strict';

angular.module('adapApp')
	.controller('IdentifierDeleteController', function($scope, $uibModalInstance, entity, Identifier) {

        $scope.identifier = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Identifier.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
