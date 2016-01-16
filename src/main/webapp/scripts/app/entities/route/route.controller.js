'use strict';

angular.module('adapApp')
    .controller('RouteController', function ($scope, $state,$resource, Route,Filter, RouteService, RouteSearch, ParseLinks) {

        $scope.routes = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;

        $scope.loadFilters = function() {
        Filter.filtersByRecordtype({name: "Route"}, function(data) {
        	$scope.filters = data;
          });
        }
        $scope.loadFilters();
        
        $scope.loadAll = function() {
            Route.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.routes = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
        //    $scope.loadAll();
        };
      //  $scope.loadAll();
     	function initialize()
       	{
       	    $scope.map = new google.maps.Map(document.getElementById("map_canvas"),
       	    {
       	        zoom: 4,
       	        center: new google.maps.LatLng(38.3629444,-97.0063889),
       	        mapTypeId: google.maps.MapTypeId.ROADMAP  
       	    });
       	}
     	
        $scope.index = function () {
        	Route.index();
        };
        
        $scope.executefilter=function(id){ 
        	$scope.filterId=id;
            console.log(id+""+$scope.filterId);
        	 Route.executefilter({id:$scope.filterId}, function(output) {
             	$scope.gridOptions.totalItems=output.length;
                //$scope.assets = output;
             	console.log(output)
  	    		$scope.searchdata=output;
 	    		getPagesearch();
             });
        };
        
        $scope.search = function () {
        	console.log($scope.searchQuery)
        	if($scope.searchQuery == "" || $scope.searchQuery == null){
        		$scope.searchdata=[];
     	    	$scope.gridOptions.data = $scope.data.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
            	getcount();
        		getData();
        		
        	}else{
            RouteSearch.query({query: $scope.searchQuery}, function(result) {
            	$scope.gridOptions.totalItems=result.length;
                $scope.assets = result;
 	    		$scope.searchdata=result;
 	    		getPagesearch();
                console.log(result)
               });
        	}
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.route = {
                name: null,
                description: null,
                details: null,
                status: null,
                lastmodifiedby: null,
                lastmodifieddate: null,
                domain: null,
                id: null
            };
        };
        

        var x=0;
 	    var page=0; var size=2500;
 	     function getcount(){
 	    	RouteService.count().then(function(obj){
			  $scope.gridOptions.totalItems=obj; 
     		 console.log(obj);
     		 return obj
     	});
      }
	  getcount();
       var paginationOptions = { pageNumber: 1,  pageSize: 10,sort: null};
       	  $scope.gridOptions = {
       	    enableFiltering: true,
       	    paginationPageSizes: [10, 20, 50, 100],
       	    paginationPageSize: 10,
       	    useExternalPagination: true,
       	    useExternalSorting: false,
       	    columnDefs: [
       	                 { name: 'routeId',
       	                   cellTemplate:' <a ui-sref="route.detail({id:row.entity.routeId})">{{row.entity.routeId}}</a>'
       	                 },
       	                 { field: 'routName', displayName: 'Route Name', enableSorting: true },
                         { field: 'originName', displayName: 'origin', enableSorting: true },
                         { field: 'destinationName', displayName: 'destination', enableSorting: true },
                         { field: 'averageScore', displayName: 'averageScore', enableSorting: true },
                         { name: 'averageScore',
         	                   cellTemplate:' <a ui-sref="route.score({id:row.entity.routeId})">{{row.entity.averageScore}}</a>'
         	                 },
       	                 { name: 'Action',
       		            	field: 'action',enableFiltering: false,enableSorting: false,
                                cellTemplate:
                               	          ' <button type="submit" ui-sref="route.detail({id:row.entity.routeId})" class="btn btn-info btn-sm"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;<span translate="entity.action.view"> View</span></button>'+
                                          ' <button type="submit" ui-sref="route.edit({id:row.entity.routeId})" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>&nbsp;<span translate="entity.action.edit"> Edit</span></button>'+
                                          ' <button type="submit" ng-click="grid.appScope.delete(row.entity.routeId)" class="btn btn-danger btn-sm"><span class="glyphicon glyphicon-remove-circle"></span>&nbsp;<span translate="entity.action.delete"> Delete</span></button>'
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
       	        getPage();
       	      });
       	      $scope.gridApi.core.on.filterChanged( $scope, function() {
       	    	if($scope.searchdata != null && $scope.searchdata.length > 0){
       	    		 $scope.gridOptions.data = $scope.searchdata; 
       	    	}else{
                      $scope.gridOptions.data = $scope.data;        	    		  
               }
       	    	

       	      });
       	      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
       	    	  console.log(newPage)
       	    	  if($scope.searchdata != null && $scope.searchdata.length > 0){
             	        paginationOptions.pageNumber = newPage;
             	        paginationOptions.pageSize = pageSize;
             	       getPagesearch();
       	    	  }
       	    	  else{
       	    	  var length=$scope.gridOptions.totalItems/size;
       	    	  var x=size/(paginationOptions.pageSize);
       	    	  Number.prototype.between = function (min, max) {
                       return this >= min && this <= max;
                      };
                      for(var i=1;i<length;i++){
                   	   if ((newPage).between(x*i, (i+1)*x)) {
            	    		  page=i;
              	              loadMore(size*i);
              	            }
                      }
       	        getPage();
       	        paginationOptions.pageNumber = newPage;
       	        paginationOptions.pageSize = pageSize;
       	        }
       	      });
       	    }
       	  };

       	  function getData(){
       		 var sasa= $resource('api/routes/:page/:size', {
         	    	page : page,
         	    	size : size
         	   }).query();
       		 $scope.data=sasa;
       	}
       	  
       	  function loadMore(startposition){
       		 if($scope.data[startposition+1]!=null){
       			 console.log("required data are available")
       		 }else{
       			 Asset.getassets({page: page, size: size}, function(data, headers) {
                     console.log("new data has loaded");
                     for (var i=0 ;i < data.length; ++i) {
                	    	$scope.data[startposition+i]=data[i];
                       }
         	    	$scope.gridOptions.data = $scope.data.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
         	      });
       		 }
          	  }
       	  
       	  getData();
	    	
       	  function isInArray(value, array) {
	         	  return array.indexOf(value) > -1;
	         	}
	    	
         	  function getPage() {
         		$scope.data.$promise.then(function(result) {
         	    var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
        	    $scope.gridOptions.data = $scope.data.slice(firstRow, firstRow + paginationOptions.pageSize);
              	initialize();
              	var points=[];
        	    for(var i=0;i<$scope.gridOptions.data.length;++i){        	    	
        	       for(var j=0;j<$scope.gridOptions.data[i].originLocations.length;++j){
        	    	$scope.route =[
                 	        new google.maps.LatLng($scope.gridOptions.data[i].originLocations[j].latitudedd,$scope.gridOptions.data[i].originLocations[j].longitudedd),
                 	        new google.maps.LatLng($scope.gridOptions.data[i].destinationLocations[j].latitudedd,$scope.gridOptions.data[i].destinationLocations[j].longitudedd),
                	    ];         	    	        	    	
        	    	console.log(points)
        	    	  if(isInArray($scope.gridOptions.data[i].originLocations[j].id,points)){
                       }
        	    	  else{
                      	points.push($scope.gridOptions.data[i].originLocations[j].id);
                        var mapLabelorigin = new MapLabel({
                 	        text: $scope.gridOptions.data[i].originNames[j],
                 	        position: new google.maps.LatLng($scope.gridOptions.data[i].originLocations[j].latitudedd, $scope.gridOptions.data[i].originLocations[j].longitudedd),
                 	        map: $scope.map,
                 	        fontSize: 15,
                 	        align: 'right'
                 	    });
                      }
        	    	
        	    	  if(isInArray($scope.gridOptions.data[i].destinationLocations[j].id,points)){
        	             }
        		    	  else{
        	            	points.push($scope.gridOptions.data[i].destinationLocations[j].id);
        	              var mapLabelorigin = new MapLabel({
        	       	        text: $scope.gridOptions.data[i].destinationNames[j],
        	       	        position: new google.maps.LatLng($scope.gridOptions.data[i].destinationLocations[j].latitudedd, $scope.gridOptions.data[i].destinationLocations[j].longitudedd),
        	       	        map: $scope.map,
        	       	        fontSize: 15,
        	       	        align: 'right'
        	       	    });
        	            }
      			  var color;
   		    	  if (isNaN($scope.gridOptions.data[i].averageScore)) {
   			      color = "black";
   			       }else if ($scope.gridOptions.data[i].averageScore <= 7.5 && $scope.gridOptions.data[i].averageScore > 5) {
   				    color = "yellow";
				   } else if ($scope.gridOptions.data[i].averageScore > 7.5){
				    color = "red";
				  }else if ($scope.gridOptions.data[i].averageScore <= 5) {
					  color = "green";
				 } 
       	          var path = new google.maps.Polyline(
              	    {
              	        path: $scope.route,
              	        strokeColor: color,
              	        strokeOpacity: 0.75,
              	        strokeWeight: 2,
              	        geodesic: true   
              	      });
       	       
           	    path.setMap($scope.map); 
        	    }
     		}
        });
     }
            	 
       	  getPage();

       	 function getPagesearch() {
       		initialize();
       		var points=[];
  	    	$scope.gridOptions.data = $scope.searchdata.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
 			for(var i=0;i<$scope.gridOptions.data.length;++i){
 				
              
     	       for(var j=0;j<$scope.gridOptions.data[i].originLocations.length;++j){
       	    	$scope.route =[
                	        new google.maps.LatLng($scope.gridOptions.data[i].originLocations[j].latitudedd,$scope.gridOptions.data[i].originLocations[j].longitudedd),
                	        new google.maps.LatLng($scope.gridOptions.data[i].destinationLocations[j].latitudedd,$scope.gridOptions.data[i].destinationLocations[j].longitudedd),
               	    ];         	    	        	    	
       	    	console.log(points)
       	    	  if(isInArray($scope.gridOptions.data[i].originLocations[j].id,points)){
                      }
       	    	  else{
                     	points.push($scope.gridOptions.data[i].originLocations[j].id);
                       var mapLabelorigin = new MapLabel({
                	        text: $scope.gridOptions.data[i].originNames[j],
                	        position: new google.maps.LatLng($scope.gridOptions.data[i].originLocations[j].latitudedd, $scope.gridOptions.data[i].originLocations[j].longitudedd),
                	        map: $scope.map,
                	        fontSize: 15,
                	        align: 'right'
                	    });
                     }
       	    	
       	     if(isInArray($scope.gridOptions.data[i].destinationLocations[j].id,points)){
             }
	    	  else{
            	points.push($scope.gridOptions.data[i].destinationLocations[j].id);
              var mapLabelorigin = new MapLabel({
       	        text: $scope.gridOptions.data[i].destinationNames[j],
       	        position: new google.maps.LatLng($scope.gridOptions.data[i].destinationLocations[j].latitudedd, $scope.gridOptions.data[i].destinationLocations[j].longitudedd),
       	        map: $scope.map,
       	        fontSize: 15,
       	        align: 'right'
       	    });
            }
     			  var color;
  		    	  if (isNaN($scope.gridOptions.data[i].averageScore)) {
  			      color = "black";
  			       }else if ($scope.gridOptions.data[i].averageScore <= 7.5 && $scope.gridOptions.data[i].averageScore > 5) {
  				    color = "yellow";
				   } else if ($scope.gridOptions.data[i].averageScore > 7.5){
				    color = "red";
				  }else if ($scope.gridOptions.data[i].averageScore <= 5) {
					  color = "green";
				 } 
      	          var path = new google.maps.Polyline(
             	    {
             	        path: $scope.route,
             	        strokeColor: color,
             	        strokeOpacity: 0.75,
             	        strokeWeight: 2,
             	        geodesic: true   
             	      });
      	       
          	    path.setMap($scope.map); 
       	    }		
           	    
 			}
         }
    });
