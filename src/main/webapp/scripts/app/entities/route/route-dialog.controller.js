'use strict';

angular.module('adapApp').controller('RouteDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Route', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Segment',
        function($scope, $stateParams, $uibModalInstance, entity, Route, Objrecordtype, Objclassification, Objcategory, Objtype, Segment) {

        $scope.route = entity;
        $scope.objrecordtypes = Objrecordtype.getrecordtypes();
        $scope.objclassifications;
        $scope.objcategorys;
        $scope.objtypes;
        $scope.segments = Segment.query();
        $scope.load = function(id) {
            Route.get({id : id}, function(result) {
                $scope.route = result;
            });
        };

        $scope.getclassifications = function (id) {
            Objclassification.getclassifications({id: id}, function (result) {
               $scope.objclassifications = result;
               $scope.objcategorys="";
               $scope.objtypes="";
           });
        };
        
        
        $scope.getcategories = function (id) {
        	Objcategory.getcategories({id: id}, function (result) {
               $scope.objcategorys = result;
               $scope.objtypes="";
           });
        };
        
        $scope.gettypes = function (id) {
        	Objtype.gettypes({id: id}, function (result) {
               $scope.objtypes = result;
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
