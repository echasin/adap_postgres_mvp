'use strict';

angular.module('adapApp').controller('LocationDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Location', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Asset',
        function($scope, $stateParams, $modalInstance, entity, Location, Objrecordtype, Objclassification, Objcategory, Objtype, Asset) {

        $scope.location = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.assets = Asset.query();
        $scope.load = function(id) {
            Location.get({id : id}, function(result) {
                $scope.location = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('adapApp:locationUpdate', result);
            $modalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.location.id != null) {
                Location.update($scope.location, onSaveSuccess, onSaveError);
            } else {
                Location.save($scope.location, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
