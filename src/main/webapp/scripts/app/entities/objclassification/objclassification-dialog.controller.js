'use strict';

angular.module('adapApp').controller('ObjclassificationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Objclassification', 'Objrecordtype', 'Objcategory', 'Asset', 'Location', 'Score',
        function($scope, $stateParams, $modalInstance, entity, Objclassification, Objrecordtype, Objcategory, Asset, Location, Score) {

        $scope.objclassification = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objcategorys = Objcategory.query();
        $scope.assets = Asset.query();
        $scope.locations = Location.query();
        $scope.scores = Score.query();
        $scope.load = function(id) {
            Objclassification.get({id : id}, function(result) {
                $scope.objclassification = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:objclassificationUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.objclassification.id != null) {
                Objclassification.update($scope.objclassification, onSaveSuccess, onSaveError);
            } else {
                Objclassification.save($scope.objclassification, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
