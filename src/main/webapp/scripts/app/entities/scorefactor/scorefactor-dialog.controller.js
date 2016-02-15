'use strict';

angular.module('adapApp').controller('ScorefactorDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Scorefactor', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype',
        function($scope, $stateParams, $uibModalInstance, entity, Scorefactor, Objrecordtype, Objclassification, Objcategory, Objtype) {

        $scope.scorefactor = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.load = function(id) {
            Scorefactor.get({id : id}, function(result) {
                $scope.scorefactor = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:scorefactorUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.scorefactor.id != null) {
                Scorefactor.update($scope.scorefactor, onSaveSuccess, onSaveError);
            } else {
                Scorefactor.save($scope.scorefactor, onSaveSuccess, onSaveError);
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
