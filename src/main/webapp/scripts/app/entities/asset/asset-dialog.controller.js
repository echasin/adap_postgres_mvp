'use strict';

angular.module('adapApp').controller('AssetDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Asset', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Location', 'Score', 'Vulnerability', 'Identifier',
        function($scope, $stateParams, $modalInstance, entity, Asset, Objrecordtype, Objclassification, Objcategory, Objtype, Location, Score, Vulnerability, Identifier) {

        $scope.asset = entity;
        $scope.objrecordtypes = Objrecordtype.getrecordtypes();
        $scope.objclassifications;
        $scope.objcategorys;
        $scope.objtypes;
        $scope.locations = Location.query();
        $scope.scores = Score.query();
        $scope.vulnerabilitys = Vulnerability.query();
        $scope.identifiers = Identifier.query();
        
        $scope.getclassifications = function (id) {
            Objclassification.getclassifications({id: id}, function (result) {
               $scope.objclassifications = result;
               $scope.objcategorys="";
               $scope.objtypes="";
           });
        };
        
        $scope.getcategories = function (id) {
        	Objcategory.getcategories({id: id}, function (result) {
               $scope.objcategorys = result;
               $scope.objtypes="";
           });
        };
        
        $scope.gettypes = function (id) {
        	Objtype.gettypes({id: id}, function (result) {
               $scope.objtypes = result;
           });
        };
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        $scope.load = function(id) {
            Asset.get({id : id}, function(result) {
                $scope.asset = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('adapApp:assetUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.asset.id != null) {
                Asset.update($scope.asset, onSaveFinished);
            } else {
                Asset.save($scope.asset, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
