'use strict';

angular.module('adapApp').controller('ScoreDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Score', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Asset',
        function($scope, $stateParams, $modalInstance, entity, Score, Objrecordtype, Objclassification, Objcategory, Objtype, Asset) {

        $scope.score = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.assets = Asset.query();
        $scope.load = function(id) {
            Score.get({id : id}, function(result) {
                $scope.score = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:scoreUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.score.id != null) {
                Score.update($scope.score, onSaveSuccess, onSaveError);
            } else {
                Score.save($scope.score, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
