'use strict';

angular.module('adapApp')
	.controller('LocationDeleteController', function($scope, $modalInstance, entity, Location) {

        $scope.location = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Location.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });