'use strict';

angular.module('adapApp')
	.controller('ObjcategoryDeleteController', function($scope, $modalInstance, entity, Objcategory) {

        $scope.objcategory = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Objcategory.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });