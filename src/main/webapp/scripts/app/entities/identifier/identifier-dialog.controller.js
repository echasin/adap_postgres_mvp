'use strict';

angular.module('adapApp').controller('IdentifierDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Identifier', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Asset', 'Vulnerability', 'Attackscenario',
        function($scope, $stateParams, $uibModalInstance, entity, Identifier, Objrecordtype, Objclassification, Objcategory, Objtype, Asset, Vulnerability, Attackscenario) {

        $scope.identifier = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.assets = Asset.query();
        $scope.vulnerabilitys = Vulnerability.query();
        $scope.attackscenarios = Attackscenario.query();
        $scope.load = function(id) {
            Identifier.get({id : id}, function(result) {
                $scope.identifier = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:identifierUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.identifier.id != null) {
                Identifier.update($scope.identifier, onSaveSuccess, onSaveError);
            } else {
                Identifier.save($scope.identifier, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForEffectivedatetime = {};

        $scope.datePickerForEffectivedatetime.status = {
            opened: false
        };

        $scope.datePickerForEffectivedatetimeOpen = function($event) {
            $scope.datePickerForEffectivedatetime.status.opened = true;
        };
        $scope.datePickerForEnddatetime = {};

        $scope.datePickerForEnddatetime.status = {
            opened: false
        };

        $scope.datePickerForEnddatetimeOpen = function($event) {
            $scope.datePickerForEnddatetime.status.opened = true;
        };
        $scope.datePickerForLastmodifieddate = {};

        $scope.datePickerForLastmodifieddate.status = {
            opened: false
        };

        $scope.datePickerForLastmodifieddateOpen = function($event) {
            $scope.datePickerForLastmodifieddate.status.opened = true;
        };
}]);
