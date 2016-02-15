'use strict';

angular.module('adapApp')
    .controller('ScorefactorDetailController', function ($scope, $rootScope, $stateParams, entity, Scorefactor, Objrecordtype, Objclassification, Objcategory, Objtype) {
        $scope.scorefactor = entity;
        $scope.load = function (id) {
            Scorefactor.get({id: id}, function(result) {
                $scope.scorefactor = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:scorefactorUpdate', function(event, result) {
            $scope.scorefactor = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
