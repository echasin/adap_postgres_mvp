'use strict';

angular.module('adapApp')
    .controller('AssetController', function ($scope,$http,$q,Asset,Testrecord, $resource,AssetSearch, ParseLinks) {
        $scope.assets = [];
        $scope.page = 0;
        $scope.data=[];
        $scope.loadAll = function() {
            Asset.query({page: $scope.page, size: 2500}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.assets = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
         //   $scope.loadAll();
        };
        
  	//  function set(){
	//		  $scope.ali=Testrecord.set("My name ali ali alrabi"); 
        //}
        
  	 function get(){
		  Testrecord.get(function(obj){
			  $scope.ali=obj; 
     		 console.log(obj);
     		 return obj
     	});
     }
  	//set();
        //console.log("------------------------------------");
        //console.log($scope.ali);
        //console.log("------------------------------------");
        //$scope.loadAll();

        $scope.delete = function (id) {
            Asset.get({id: id}, function(result) {
                $scope.asset = result;
                $('#deleteAssetConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Asset.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAssetConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            AssetSearch.query({query: $scope.searchQuery}, function(result) {
            }, function(response) {
                if(response.status === 404) {
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.asset = {
                name: null,
                id: null
            };
        };

        var x=0;
  	    var page=0; var size=2500;
  	     function getcount(){
		  Testrecord.count().then(function(obj){
			  $scope.gridOptions.totalItems=obj; 
      		 console.log(obj);
      		 return obj
      	});
       }
	  getcount();
        var paginationOptions = { pageNumber: 1,  pageSize: 10,sort: null};
        	  $scope.gridOptions = {
        	    enableFiltering: true,
        	    paginationPageSizes: [10, 20, 30],
        	    paginationPageSize: 10,
        	    useExternalPagination: true,
        	    useExternalSorting: true,
        	    columnDefs: [
        	                 { name: 'id' },
        	                 { name: 'name', enableSorting: false },
        	                 { name: 'value', enableSorting: false },
        	                 { name: 'Action',
        		            	 field: 'action',
                                 cellTemplate:
                                	          ' <button type="submit" ui-sref="asset.detail({id:row.entity.id})" class="btn btn-info btn-sm"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;<span translate="entity.action.view"> View</span></button>'+
                                              ' <button type="submit" ui-sref="asset.edit({id:row.entity.id})" class="btn btn-primary btn-sm"><span class="glyphicon glyphicon-pencil"></span>&nbsp;<span translate="entity.action.edit"> Edit</span></button>'+
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
        	        getPage();
        	      });
        	      $scope.gridApi.core.on.filterChanged( $scope, function() {
        	    	  var grid = this.grid;
        	    	  if( grid.columns[0].filters[0].term === '' ) {
        	    		  getcount();
        	    	  }else{
            	    	  $scope.gridOptions.totalItems = $scope.data.length;
                      	  $scope.gridOptions.data = $scope.data;        	    		  
        	    	  }

        	      });
        	      gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
        	    	  console.log(newPage)
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
        	        });
        	    }
        	  };
        
        	  
        	  function getData(){
        		 var sasa= $resource('api/assets/:page/:size', {
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
         	  	
         	    var chartdata=[]
      			for(var i=0;i<$scope.gridOptions.data.length;++i){
      				chartdata.push({x: $scope.gridOptions.data[i].name,y: $scope.gridOptions.data[i].value})
      			}

      		    $('#container').highcharts({
      	          chart: {
      	            plotBackgroundColor: null,
      	            plotBorderWidth: null,
      	            plotShadow: false,
      	            type: 'pie'
      	        },
      	        title: {
      	            text: 'Chart For Current Page Data'
      	        },
      	        tooltip: {
      	            pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      	        },
      	        plotOptions: {
      	            pie: {
      	                allowPointSelect: true,
      	                cursor: 'pointer',
      	                dataLabels: {
      	                    enabled: true,
      	                    format: '<b>{point.x}</b>: {point.percentage:.1f} %',
      	                    style: {
      	                        color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
      	                    }
      	                }
      	            }
      	        },
      	        series: [{
      	            name: 'Brands',
      	            colorByPoint: true,
      	            data: chartdata
      	        }]
      	    });
         		 });
          	  }
          	 
        	  getPage();
        	  
        	});