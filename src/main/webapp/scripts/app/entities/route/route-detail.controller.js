'use strict';

angular.module('adapApp')
    .controller('RouteDetailController', function ($scope, $rootScope, $stateParams, entity, Route, Objrecordtype, Objclassification, Objcategory, Objtype, Segment) {
        $scope.route = entity;
        $scope.load = function (id) {
            Route.get({id: id}, function(result) {
                $scope.route = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:routeUpdate', function(event, result) {
            $scope.route = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.save = function () {
        	Route.update($scope.route);           
        };
        
        $scope.objrecordtypes = Objrecordtype.getrecordtypes();
        $scope.objclassifications;
        $scope.objcategorys;
        $scope.objtypes;
                
        $scope.getclassifications = function (id) {
            Objclassification.getclassifications({id: id}, function (result) {
               $scope.objclassifications = result;
               $scope.objcategorys="";
               $scope.objtypes="";
           });
        };
        if(entity.objrecordtype!=null){
            $scope.getclassifications(entity.objrecordtype.id);
         }
        
        $scope.getcategories = function (id) {
        	Objcategory.getcategories({id: id}, function (result) {
               $scope.objcategorys = result;
               $scope.objtypes="";
           });
        };
        if(entity.objclassification!=null){
            $scope.getcategories(entity.objclassification.id)
        }
        
        $scope.gettypes = function (id) {
        	Objtype.gettypes({id: id}, function (result) {
               $scope.objtypes = result;
           });
        };
        if(entity.objcategory!=null){
        $scope.gettypes(entity.objcategory.id)
        }
    });
