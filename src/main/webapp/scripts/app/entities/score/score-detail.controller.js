'use strict';

angular.module('adapApp')
    .controller('ScoreDetailController', function ($scope, $rootScope, $stateParams, entity, Score, Objrecordtype, Objclassification, Objcategory, Objtype, Asset) {
        $scope.score = entity;
        $scope.load = function (id) {
            Score.get({id: id}, function(result) {
                $scope.score = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:scoreUpdate', function(event, result) {
            $scope.score = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
