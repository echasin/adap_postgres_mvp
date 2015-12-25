'use strict';

angular.module('adapApp')
	.controller('SegmentDeleteController', function($scope, $uibModalInstance, entity, Segment) {

        $scope.segment = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Segment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
