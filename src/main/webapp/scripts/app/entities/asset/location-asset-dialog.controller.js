'use strict';

angular.module('adapApp').controller('LocationAssetDialogController',
        ['$scope', '$stateParams', '$modalInstance', 'entity', 'Location', 'Objrecordtype', 'Objclassification', 'Objcategory', 'Objtype', 'Asset',
            'Principal', 'User',
            function ($scope, $stateParams, $modalInstance, entity, Location, Obj_recordtype, Obj_classification, Obj_category, Obj_type, Asset,
                    Principal, User) {

                console.log('In location-dialog.controller.js');
               // $scope.assets=Asset.query();
               // console.log(Asset.query());
                $scope.location = entity;
                $scope.obj_recordtypes = Obj_recordtype.query();
                //$scope.objclassifications = Objclassification.query();
                //$scope.objcategorys = Objcategory.query();
                //$scope.objtypes = Objtype.query();
                $scope.obj_classifications;
                $scope.obj_categories;
                $scope.obj_types;
               // $scope.logindata = User.logindata();
                //$scope.assets = Asset.query();

                $scope.getclassifications = function (id) {
                    Obj_classification.getclassifications({id: id}, function (result) {
                        $scope.obj_classifications = result;
                    });
                };

                $scope.getcategories = function (id) {
                    Obj_category.getcategories({id: id}, function (result) {
                        $scope.obj_categories = result;
                    });
                };

                $scope.gettypes = function (id) {
                    Obj_type.gettypes({id: id}, function (result) {
                        $scope.obj_types = result;
                    });
                };

                $scope.load = function (id) {
                    Location.get({id: id}, function (result) {
                      //  $scope.location = result;
                        $scope.getclassifications(result.objRecordtype.id);
                        $scope.getcategories(result.objClassification.id);
                        $scope.gettypes(result.objCategory.id);
                    });
                };

           //     $scope.load($stateParams.ld);

                var onSaveFinished = function (result) {
                    $scope.$emit('adapApp:locationUpdate', result);
                    $modalInstance.close(result);
                };

                $scope.save = function () {
                    if ($scope.location.id != null) {
                    	$scope.location.asset=$stateParams;
                        Location.update($scope.location, onSaveFinished);
                    } else {
                    	$scope.location.asset=$stateParams;
                        Location.save($scope.location, onSaveFinished);
                    }
                };

                $scope.clear = function () {
                    $modalInstance.dismiss('cancel');
                };
            }]);
