'use strict';

angular.module('adapApp')
	.controller('ScoreDeleteController', function($scope, $modalInstance, entity, Score) {

        $scope.score = entity;
        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            Score.delete({id: id},
                function () {
                    $modalInstance.close(true);
                });
        };

    });