'use strict';

angular.module('adapApp')
    .controller('AssetDetailController', function ($scope, $location,$rootScope, $stateParams, $resource,entity, Asset,LocationService, Objrecordtype, Objclassification, Objcategory, Objtype, Location, Score, Vulnerability, Identifier,Event,EventService) {
        $scope.asset = entity;
        $scope.load = function (id) {
            Asset.get({id: id}, function(result) {
                $scope.asset = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:assetUpdate', function(event, result) {
            $scope.asset = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.save = function () {
        	Asset.update($scope.asset);           
        };

        $scope.delete = function () {
            Asset.get({id: $stateParams.id}, function(result) {
                $scope.asset = result;
                $('#deleteAssetConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function () {
        	Asset.delete({id: $stateParams.id},
                function () {
            $('#deleteAssetConfirmation').modal('hide');
        	});
        	 $location.url("assets");
        };
        
        $scope.deleteEvent = function (id) {
        	$scope.eventId=id;
            Event.get({id: id}, function(result) {
                $('#deleteEventConfirmation').modal('show');
            });
        };

        $scope.confirmDeleteEvent = function () {
            	Event.delete({id: $scope.eventId},
                    function () {
               	              getEventCount();
                              getEventData();
            	              getEventPage();
                              loadEventGrid();
                              $('#deleteEventConfirmation').modal('hide');
                              $location.url("asset/"+$stateParams.id);
            	 },function(response) {
            	              if(response.status === 500) {
            	              $('#deleteEventConfirmation').modal('hide');
           		              $('#checkLinked').modal('show');
                }
            	});   
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
          
   
      function getLocationCount(){
 	    	LocationService.count().then(function(obj){
			  $scope.locationGridOptions.totalItems=obj; 
     		 console.log(obj);
     		 return obj
     	});
      }
      
	  getLocationCount();
      
	  var paginationOptions = { pageNumber: 1,  pageSize: 10,sort: null};
       
       		 $scope.locationGridOptions = {
       	       	    enableFiltering: true,
       	       	    paginationPageSizes: [10, 20, 50, 100],
       	       	    paginationPageSize: 10,
       	       	    useExternalPagination: true,
       	       	    useExternalSorting: false,
       	       	    columnDefs: [
       	       	                 { field: 'id',  displayName: 'ID', width: 60, enableSorting: true },
       	       	                 { field: 'isprimary', displayName: 'isprimary', enableSorting: true },
       	                         { field: 'address1', displayName: 'address1', enableSorting: true },
       	       	                 { field: 'domain',displayName: 'Domain', enableSorting: true },
       	       	                 { name: 'Action',
       	       		            	field: 'action',enableFiltering: false,enableSorting: false,
       	                                cellTemplate:
       	                               	          ' <button type="submit" ui-sref="score.detail({id:row.entity.id})" class="btn btn-info btn-sm"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;<span translate="entity.action.view"> View</span></button>'+
       	                                          ' <button type="submit" ui-sref="score.edit({id:row.entity.id})" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>&nbsp;<span translate="entity.action.edit"> Edit</span></button>'+
       	                                          ' <button type="submit" ng-click="grid.appScope.delete(row.entity.id)" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove-circle"></span>&nbsp;<span translate="entity.action.delete"> Delete</span></button>'
       	       	                 }
       	             ],
       	       	  onRegisterApi: function(gridApi) {
       	       	      $scope.gridApi = gridApi;
       	       	      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
       	       	        if (sortColumns.length == 0) {
       	       	          paginationOptions.sort = null;
       	       	        } else {
       	       	          paginationOptions.sort = sortColumns[0].sort.direction;
       	       	        }
       	       	        getLocationPage();
       	       	      });
       	       	      $scope.gridApi.core.on.filterChanged( $scope, function() {
       	       	    	if($scope.searchdata != null && $scope.searchdata.length > 0){
       	       	    		 $scope.locationGridOptions.data = $scope.searchdata; 
       	       	       }else{
       	                      $scope.locationGridOptions.data = $scope.data;        	    		  
       	               }

       	       	      });
       	       	      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
       	              	   var x=0;
       	       	    	  console.log(newPage)
       	       	    	  if($scope.searchdata != null && $scope.searchdata.length > 0){
       	             	        paginationOptions.pageNumber = newPage;
       	             	        paginationOptions.pageSize = pageSize;
       	             	       getLocationPagesearch();
       	       	    	  }
       	       	    	  else{
       	       	    	  var length=$scope.locationGridOptions.totalItems/size;
       	       	    	  var x=size/(paginationOptions.pageSize);
       	       	    	  Number.prototype.between = function (min, max) {
       	                       return this >= min && this <= max;
       	                      };
       	                      for(var i=1;i<length;i++){
       	                   	   if ((newPage).between(x*i, (i+1)*x)) {
       	            	    		  page=i;
       	              	              loadMoreLocation(size*i);
       	              	            }
       	                      }
       	       	        getLocationPage();
       	       	        paginationOptions.pageNumber = newPage;
       	       	        paginationOptions.pageSize = pageSize;
       	       	        }
       	       	      });
       	       	    }
       	       	  };
       	 

       	  function getLocationData(){
    	    var page=0; var size=2500;
       		 var sasa= $resource('api/locationsByAsset/:id/:page/:size', {
         	    	id: $stateParams.id,
       			    page : page,
         	    	size : size
         	   }).query();
       		 $scope.data=sasa;
       	  }
       	  
       	  function loadMoreLocation(startposition){
       		 if($scope.data[startposition+1]!=null){
       			 console.log("required data are available")
       		 }else{
       			 Location.getlocationsByAsset({page: page, size: size}, function(data, headers) {
                     console.log("new data has loaded");
                     for (var i=0 ;i < data.length; ++i) {
                	    	$scope.data[startposition+i]=data[i];
                       }
         	    	$scope.locationGridOptions.data = $scope.data.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
         	      });
       		 }
          	  }
       	  
       	  getLocationData();
       	        	           	   
         	  function getLocationPage() {
         		$scope.data.$promise.then(function(result) {
         	    var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
        	    $scope.locationGridOptions.data = $scope.data.slice(firstRow, firstRow + paginationOptions.pageSize);
    	  		var map;
    	  		Location.locationByIsprimary({id: $stateParams.id}, function(result) {      
        	    	console.log(result)
        	    	console.log(result.latitudedd)
                    function initialize() {
                      var myOptions = {
                        zoom: 4,
                        center: new google.maps.LatLng(result.latitudedd, result.longitudedd),
                        mapTypeId: google.maps.MapTypeId.ROADMAP
                      };
                      map = new google.maps.Map(document.getElementById("map"),
                          myOptions);        

                	  var myLatLng = {lat: result.latitudedd, lng:  result.longitudedd};
                	  
                      var largemarker = new google.maps.Marker({
                		    position: myLatLng,
                		    title: result.address1
                		   });
                      largemarker.setMap(map);
                    }
                    google.maps.event.addDomListener(window, "load", initialize);	
                
                });
             });
          
        }
            	 
       	 getLocationPage();

         function getLocationPagesearch() {
       	    	$scope.locationGridOptions.data = $scope.searchdata.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
        }
         
       function getEventCount(){
  	    	EventService.count().then(function(obj){
 	   	     $scope.eventGridOptions.totalItems=obj; 
      		 console.log(obj);
      		 return obj
      	});
       }
       
 	  getEventCount();
       
        
 	 function loadEventGrid(){
 	 	var paginationOptions = { pageNumber: 1,  pageSize: 10,sort: null};
 	    $scope.eventGridOptions = {
        	    enableFiltering: true,
        	    paginationPageSizes: [10, 20, 50, 100],
        	    paginationPageSize: 10,
        	    useExternalPagination: true,
        	    useExternalSorting: false,
        	    columnDefs: [
        	                 { field: 'id',  displayName: 'ID', width: 60, enableSorting: true },
        	                 { field: 'name', displayName: 'name', enableSorting: true },
                          { field: 'eventdate', displayName: 'eventdate', enableSorting: true },
        	                 { field: 'domain',displayName: 'Domain', enableSorting: true },
        	                 { name: 'Action',
        		            	field: 'action',enableFiltering: false,enableSorting: false,
                                 cellTemplate:
                                	          ' <button type="submit" ui-sref="event.detail({id:row.entity.id})" class="btn btn-info btn-sm"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;<span translate="entity.action.view"> View</span></button>'+
                                           ' <button type="submit" ui-sref="event.edit({id:row.entity.id})" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>&nbsp;<span translate="entity.action.edit"> Edit</span></button>'+
                                           ' <button type="submit" ng-click="grid.appScope.deleteEvent(row.entity.id)" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove-circle"></span>&nbsp;<span translate="entity.action.delete"> Delete</span></button>'
        	                 }
              ],
        	  onRegisterApi: function(gridApi) {
        	      $scope.gridApi = gridApi;
        	      $scope.gridApi.core.on.sortChanged($scope, function(grid, sortColumns) {
        	        if (sortColumns.length == 0) {
        	          paginationOptions.sort = null;
        	        } else {
        	          paginationOptions.sort = sortColumns[0].sort.direction;
        	        }
        	        getEventPage();
        	      });
        	      $scope.gridApi.core.on.filterChanged( $scope, function() {
        	    	if($scope.searchdata != null && $scope.searchdata.length > 0){
        	    		 $scope.eventGridOptions.data = $scope.searchdata; 
        	       }else{
                       $scope.eventGridOptions.data = $scope.data;        	    		  
                }

        	      });
        	      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
               	   var x=0;
        	    	  console.log(newPage)
        	    	  if($scope.searchdata != null && $scope.searchdata.length > 0){
              	        paginationOptions.pageNumber = newPage;
              	        paginationOptions.pageSize = pageSize;
              	       getEventPagesearch();
        	    	  }
        	    	  else{
        	    	  var length=$scope.eventGridOptions.totalItems/size;
        	    	  var x=size/(paginationOptions.pageSize);
        	    	  Number.prototype.between = function (min, max) {
                        return this >= min && this <= max;
                       };
                       for(var i=1;i<length;i++){
                    	   if ((newPage).between(x*i, (i+1)*x)) {
             	    		  page=i;
               	              loadMoreEvent(size*i);
               	            }
                       }
        	        getEventPage();
        	        paginationOptions.pageNumber = newPage;
        	        paginationOptions.pageSize = pageSize;
        	        }
        	      });
        	    }
        	  };
 	 }
	  loadEventGrid();
      
	  function getEventData(){
     	         var page=0; var size=2500;
        		 var sasa= $resource('api/eventsByAsset/:id/:page/:size', {
          	    	id: $stateParams.id,
        			page : page,
          	    	size : size
          	   }).query();
        		 $scope.data=sasa;
        	  }
        	  
       function loadMoreEvent(startposition){
      		 if($scope.data[startposition+1]!=null){
       			 console.log("required data are available")
        		 }else{
        			 Event.geteventsByAsset({page: page, size: size}, function(data, headers) {
                      console.log("new data has loaded");
                      for (var i=0 ;i < data.length; ++i) {
                 	    	$scope.data[startposition+i]=data[i];
                        }
          	    	$scope.eventGridOptions.data = $scope.data.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
          	      });
        		 }
           	  }
        	  
        	  getEventData();
        	        	           	   
          	  function getEventPage() {
          		$scope.data.$promise.then(function(result) {
          	    var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
         	    $scope.eventGridOptions.data = $scope.data.slice(firstRow, firstRow + paginationOptions.pageSize);
              });
           
         }
             	 
        	 getEventPage();

          function getEventPagesearch() {
        	    	$scope.eventGridOptions.data = $scope.searchdata.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
         }
          
    });
