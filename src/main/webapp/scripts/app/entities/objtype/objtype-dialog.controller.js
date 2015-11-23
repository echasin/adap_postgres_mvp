'use strict';

angular.module('adapApp').controller('ObjtypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Objtype', 'Objcategory', 'Asset', 'Location', 'Score',
        function($scope, $stateParams, $modalInstance, entity, Objtype, Objcategory, Asset, Location, Score) {

        $scope.objtype = entity;
        $scope.objcategorys = Objcategory.query();
        $scope.assets = Asset.query();
        $scope.locations = Location.query();
        $scope.scores = Score.query();
        $scope.load = function(id) {
            Objtype.get({id : id}, function(result) {
                $scope.objtype = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:objtypeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.objtype.id != null) {
                Objtype.update($scope.objtype, onSaveSuccess, onSaveError);
            } else {
                Objtype.save($scope.objtype, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
