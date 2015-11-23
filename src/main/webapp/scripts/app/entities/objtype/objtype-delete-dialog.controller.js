'use strict';

angular.module('adapApp')
	.controller('ObjtypeDeleteController', function($scope, $modalInstance, entity, Objtype) {

        $scope.objtype = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Objtype.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });