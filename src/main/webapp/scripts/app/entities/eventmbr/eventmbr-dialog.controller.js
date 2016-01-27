'use strict';

angular.module('adapApp').controller('EventmbrDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Eventmbr', 'Event', 'Asset',
        function($scope, $stateParams, $uibModalInstance, entity, Eventmbr, Event, Asset) {

        $scope.eventmbr = entity;
        $scope.events = Event.query();
        $scope.assets = Asset.query();
        $scope.load = function(id) {
            Eventmbr.get({id : id}, function(result) {
                $scope.eventmbr = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:eventmbrUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.eventmbr.id != null) {
                Eventmbr.update($scope.eventmbr, onSaveSuccess, onSaveError);
            } else {
                Eventmbr.save($scope.eventmbr, onSaveSuccess, onSaveError);
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
