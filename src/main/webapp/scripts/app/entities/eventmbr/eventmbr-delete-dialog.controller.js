'use strict';

angular.module('adapApp')
	.controller('EventmbrDeleteController', function($scope, $uibModalInstance, entity, Eventmbr) {

        $scope.eventmbr = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Eventmbr.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
