'use strict';

angular.module('adapApp').controller('SegmentDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Segment', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Asset', 'Route',
        function($scope, $stateParams, $uibModalInstance, entity, Segment, Objrecordtype, Objclassification, Objcategory, Objtype, Asset, Route) {

        $scope.segment = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.assets = Asset.query();
        $scope.routes = Route.query();
        $scope.load = function(id) {
            Segment.get({id : id}, function(result) {
                $scope.segment = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:segmentUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.segment.id != null) {
                Segment.update($scope.segment, onSaveSuccess, onSaveError);
            } else {
                Segment.save($scope.segment, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForOrigineta = {};

        $scope.datePickerForOrigineta.status = {
            opened: false
        };

        $scope.datePickerForOriginetaOpen = function($event) {
            $scope.datePickerForOrigineta.status.opened = true;
        };
        $scope.datePickerForOriginAta = {};

        $scope.datePickerForOriginAta.status = {
            opened: false
        };

        $scope.datePickerForOriginAtaOpen = function($event) {
            $scope.datePickerForOriginAta.status.opened = true;
        };
        $scope.datePickerForDestinationeta = {};

        $scope.datePickerForDestinationeta.status = {
            opened: false
        };

        $scope.datePickerForDestinationetaOpen = function($event) {
            $scope.datePickerForDestinationeta.status.opened = true;
        };
        $scope.datePickerForDestinationata = {};

        $scope.datePickerForDestinationata.status = {
            opened: false
        };

        $scope.datePickerForDestinationataOpen = function($event) {
            $scope.datePickerForDestinationata.status.opened = true;
        };
        $scope.datePickerForLastmodifieddate = {};

        $scope.datePickerForLastmodifieddate.status = {
            opened: false
        };

        $scope.datePickerForLastmodifieddateOpen = function($event) {
            $scope.datePickerForLastmodifieddate.status.opened = true;
        };
}]);
