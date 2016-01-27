'use strict';

angular.module('adapApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Eventmbr',
        function($scope, $stateParams, $uibModalInstance, entity, Event, Objrecordtype, Objclassification, Objcategory, Objtype, Eventmbr) {

        $scope.event = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.eventmbrs = Eventmbr.query();
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:eventUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.event.id != null) {
                Event.update($scope.event, onSaveSuccess, onSaveError);
            } else {
                Event.save($scope.event, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForEventdate = {};

        $scope.datePickerForEventdate.status = {
            opened: false
        };

        $scope.datePickerForEventdateOpen = function($event) {
            $scope.datePickerForEventdate.status.opened = true;
        };
        $scope.datePickerForLastmodifieddate = {};

        $scope.datePickerForLastmodifieddate.status = {
            opened: false
        };

        $scope.datePickerForLastmodifieddateOpen = function($event) {
            $scope.datePickerForLastmodifieddate.status.opened = true;
        };
}]);
