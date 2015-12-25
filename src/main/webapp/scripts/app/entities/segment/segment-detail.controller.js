'use strict';

angular.module('adapApp')
    .controller('SegmentDetailController', function ($scope, $rootScope, $stateParams, entity, Segment, Objrecordtype, Objclassification, Objcategory, Objtype, Asset, Route) {
        $scope.segment = entity;
        $scope.load = function (id) {
            Segment.get({id: id}, function(result) {
                $scope.segment = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:segmentUpdate', function(event, result) {
            $scope.segment = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
