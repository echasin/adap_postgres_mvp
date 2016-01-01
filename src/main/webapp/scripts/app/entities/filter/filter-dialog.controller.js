'use strict';

angular.module('adapApp').controller('FilterDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Filter', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Location',
        function($scope, $stateParams, $modalInstance, entity, Filter, Objrecordtype, Objclassification, Objcategory, Objtype, Location) {

        $scope.filter = entity;
        $scope.objrecordtypes = Objrecordtype.query();
        $scope.objclassifications = Objclassification.query();
        $scope.objcategorys = Objcategory.query();
        $scope.objtypes = Objtype.query();
        $scope.locations = Location.query();
        $scope.load = function(id) {
            Filter.get({id : id}, function(result) {
                $scope.filter = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('adapApp:filterUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.filter.id != null) {
                Filter.update($scope.filter, onSaveFinished);
            } else {
                Filter.save($scope.filter, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
