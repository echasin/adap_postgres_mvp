'use strict';

angular.module('adapApp').controller('AssetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Asset', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Location', 'Score',
        function($scope, $stateParams, $modalInstance, entity, Asset, Objrecordtype, Objclassification, Objcategory, Objtype, Location, Score) {

        $scope.asset = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.locations = Location.query();
        $scope.scores = Score.query();
        $scope.load = function(id) {
            Asset.get({id : id}, function(result) {
                $scope.asset = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:assetUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.asset.id != null) {
                Asset.update($scope.asset, onSaveSuccess, onSaveError);
            } else {
                Asset.save($scope.asset, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
