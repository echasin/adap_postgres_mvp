'use strict';

angular.module('adapApp').controller('RouteDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Route', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Segment',
        function($scope, $stateParams, $uibModalInstance, entity, Route, Objrecordtype, Objclassification, Objcategory, Objtype, Segment) {

        $scope.route = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.segments = Segment.query();
        $scope.load = function(id) {
            Route.get({id : id}, function(result) {
                $scope.route = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:routeUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.route.id != null) {
                Route.update($scope.route, onSaveSuccess, onSaveError);
            } else {
                Route.save($scope.route, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForLastmodifieddate = {};

        $scope.datePickerForLastmodifieddate.status = {
            opened: false
        };

        $scope.datePickerForLastmodifieddateOpen = function($event) {
            $scope.datePickerForLastmodifieddate.status.opened = true;
        };
}]);
