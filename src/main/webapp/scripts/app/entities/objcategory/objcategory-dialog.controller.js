'use strict';

angular.module('adapApp').controller('ObjcategoryDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Objcategory', 'Objclassification', 'Objtype', 'Asset', 'Location', 'Score',
        function($scope, $stateParams, $modalInstance, entity, Objcategory, Objclassification, Objtype, Asset, Location, Score) {

        $scope.objcategory = entity;
        $scope.objclassifications = Objclassification.query();
        $scope.objtypes = Objtype.query();
        $scope.assets = Asset.query();
        $scope.locations = Location.query();
        $scope.scores = Score.query();
        $scope.load = function(id) {
            Objcategory.get({id : id}, function(result) {
                $scope.objcategory = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:objcategoryUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.objcategory.id != null) {
                Objcategory.update($scope.objcategory, onSaveSuccess, onSaveError);
            } else {
                Objcategory.save($scope.objcategory, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
