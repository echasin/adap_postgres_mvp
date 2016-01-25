'use strict';

angular.module('adapApp')
    .controller('RouteController', function ($scope, $state,$resource, Route,Filter, RouteService,$timeout, RouteSearch, ParseLinks) {

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
        };

        function isInArray(value, array) {
        	  return array.indexOf(value) > -1;
       	}
     	  
        function initialize()
       	{
     		var map_canvas = document.getElementById('map_canvas');
     		var mapcanvas = document.getElementById('mapcanvas');
     	    var mapOptions = {
     	    		zoom: 3,
           	        center: new google.maps.LatLng(38.3629444,-97.0063889),
           	        mapTypeId: google.maps.MapTypeId.ROADMAP
     	    }
     	    
       	    $scope.map = new google.maps.Map(map_canvas, mapOptions);
       	    $scope.largemap =new google.maps.Map(mapcanvas, mapOptions);

        	}
     	
        function drawmap(array){
       	 initialize();
       		var points=[];
       	    for(var i=0;i<array.length;++i){  
       	    	 if (isNaN(array[i].averageScore)) {
       	    		 console.log("no score available");
       	    	 }else{
       	       for(var j=0;j<array[i].originLocations.length;++j){
       	    	$scope.route =[
                	        new google.maps.LatLng(array[i].originLocations[j].latitudedd,array[i].originLocations[j].longitudedd),
                	        new google.maps.LatLng(array[i].destinationLocations[j].latitudedd,array[i].destinationLocations[j].longitudedd),
               	    ];         	    	        	    	
       	    	  if(isInArray(array[i].originLocations[j].id,points)){
                      }
       	    	  else{
                     	points.push(array[i].originLocations[j].id);
                       var mapLabelorigin = new MapLabel({
                	        text: $scope.gridOptions.data[i].originNames[j],
                	        position: new google.maps.LatLng(array[i].originLocations[j].latitudedd, array[i].originLocations[j].longitudedd),
                	        map: $scope.map,
                	        fontSize: 15,
                	        align: 'right'
                	    });
                     }
       	    	
       	    	  if(isInArray(array[i].destinationLocations[j].id,points)){
       	             }
       		    	  else{
       	            	points.push(array[i].destinationLocations[j].id);
       	              var mapLabelorigin = new MapLabel({
       	       	        text:array[i].destinationNames[j],
       	       	        position: new google.maps.LatLng(array[i].destinationLocations[j].latitudedd, array[i].destinationLocations[j].longitudedd),
       	       	        map: $scope.map,
       	       	        fontSize: 15,
       	       	        align: 'right'
       	       	    });
       	            }
     			  var color;
  		    	  if (isNaN(array[i].averageScore)) {
  			      color = "black";
  			       }else if (array[i].averageScore <= 7.5 && array[i].averageScore > 5) {
  				    color = "yellow";
				   } else if (array[i].averageScore > 7.5){
				    color = "red";
				  }else if (array[i].averageScore <= 5) {
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
      	       var largepath = new google.maps.Polyline(
                	    {
                	        path: $scope.route,
                	        strokeColor: color,
                	        strokeOpacity: 0.75,
                	        strokeWeight: 2,
                	        geodesic: true   
                	      });
      	       
          	    path.setMap($scope.map);
          	    largepath.setMap($scope.largemap);
       	    }
       	    	 }
    		
       	    }
        }
     	
        $scope.index = function () {
        	Route.index();
        };
        
        $scope.mapView=function(){
        	drawmap($scope.gridOptions.data);
        }
        
        $scope.executefilter=function(id){ 
        	$scope.filterId=id;
            console.log(id+""+$scope.filterId);
        	 Route.executefilter({id:$scope.filterId}, function(output) {
             	$scope.gridOptions.totalItems=output.length;
             	console.log(output)
  	    		$scope.searchdata=output;
 	    		getPagesearch();
             });
        };
        
        
        $scope.data = {}
        $scope.search = function () {
        	if($scope.data.searchQuery == "" || $scope.data.searchQuery == null){
        		$scope.searchdata=[];
     	    	$scope.gridOptions.data = $scope.data.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
     	    	getcount();
        		getData();
        		
        	}else{
            RouteSearch.query({query: $scope.data.searchQuery}, function(result) {
            	$scope.gridOptions.totalItems=result.length;
                $scope.assets = result;
 	    		$scope.searchdata=result;
 	    		getPagesearch();
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
       	    useExternalPagination: false,
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
       		            	width: 190,
                                cellTemplate:
                               	          ' <button type="submit" ui-sref="route.detail({id:row.entity.routeId})" class="btn-xs btn-info"><span class="glyphicon glyphicon-eye-open"></span><span translate="entity.action.view"> View</span></button>'+
                                          ' <button type="submit" ui-sref="route.edit({id:row.entity.routeId})" class="btn-xs btn-primary"><span class="glyphicon glyphicon-pencil"></span><span translate="entity.action.edit"> Edit</span></button>'+
                                          ' <button type="submit" ng-click="grid.appScope.delete(row.entity.routeId)" class="btn-xs btn-danger"><span class="glyphicon glyphicon-remove-circle"></span><span translate="entity.action.delete"> Delete</span></button>'
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
       	      $scope.gridApi.core.on.rowsVisibleChanged($scope, function(){
       	    	var filteredData = [];
     	    		angular.forEach($scope.gridApi.grid.rows,function(v,k){
     	    			if(v.visible)
     	    				filteredData.push(v.entity);
     	    		});
     	    		
     	    	drawmap(filteredData);
            	    
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
	    	
       function getPage() {
     	 $scope.data.$promise.then(function(result) {
      	 var firstRow = (paginationOptions.pageNumber - 1) * paginationOptions.pageSize;
         $scope.gridOptions.data = $scope.data.slice(firstRow, firstRow + paginationOptions.pageSize);              	
         drawmap($scope.gridOptions.data);
        });
     }
            	 
       	 getPage();

       	 function getPagesearch() {
  	    	$scope.gridOptions.data = $scope.searchdata.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
  	        drawmap($scope.gridOptions.data);
         }
    });
