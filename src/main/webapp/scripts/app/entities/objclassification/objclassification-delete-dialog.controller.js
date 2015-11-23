'use strict';

angular.module('adapApp')
	.controller('ObjclassificationDeleteController', function($scope, $modalInstance, entity, Objclassification) {

        $scope.objclassification = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Objclassification.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });