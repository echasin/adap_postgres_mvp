'use strict';

angular.module('adapApp')
	.controller('AssetDeleteController', function($scope, $modalInstance, entity, Asset) {

        $scope.asset = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Asset.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });