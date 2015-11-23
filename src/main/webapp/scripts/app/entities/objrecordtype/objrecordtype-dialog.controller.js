'use strict';

angular.module('adapApp').controller('ObjrecordtypeDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Objrecordtype', 'Objclassification', 'Asset', 'Location', 'Score',
        function($scope, $stateParams, $modalInstance, entity, Objrecordtype, Objclassification, Asset, Location, Score) {

        $scope.objrecordtype = entity;
        $scope.objclassifications = Objclassification.query();
        $scope.assets = Asset.query();
        $scope.locations = Location.query();
        $scope.scores = Score.query();
        $scope.load = function(id) {
            Objrecordtype.get({id : id}, function(result) {
                $scope.objrecordtype = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:objrecordtypeUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.objrecordtype.id != null) {
                Objrecordtype.update($scope.objrecordtype, onSaveSuccess, onSaveError);
            } else {
                Objrecordtype.save($scope.objrecordtype, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
