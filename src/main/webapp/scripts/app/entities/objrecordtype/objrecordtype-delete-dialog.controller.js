'use strict';

angular.module('adapApp')
	.controller('ObjrecordtypeDeleteController', function($scope, $modalInstance, entity, Objrecordtype) {

        $scope.objrecordtype = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Objrecordtype.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });