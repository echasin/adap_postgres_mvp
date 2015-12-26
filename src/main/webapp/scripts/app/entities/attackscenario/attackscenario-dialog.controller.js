'use strict';

angular.module('adapApp').controller('AttackscenarioDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Attackscenario', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype',
        function($scope, $stateParams, $uibModalInstance, entity, Attackscenario, Objrecordtype, Objclassification, Objcategory, Objtype) {

        $scope.attackscenario = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.load = function(id) {
            Attackscenario.get({id : id}, function(result) {
                $scope.attackscenario = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:attackscenarioUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.attackscenario.id != null) {
                Attackscenario.update($scope.attackscenario, onSaveSuccess, onSaveError);
            } else {
                Attackscenario.save($scope.attackscenario, onSaveSuccess, onSaveError);
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
