'use strict';

angular.module('adapApp').controller('EventDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Eventmbr',
        function($scope, $stateParams, $uibModalInstance, entity, Event, Objrecordtype, Objclassification, Objcategory, Objtype, Eventmbr) {

        $scope.event = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.eventmbrs = Eventmbr.query();
        $scope.load = function(id) {
            Event.get({id : id}, function(result) {
                $scope.event = result;
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
