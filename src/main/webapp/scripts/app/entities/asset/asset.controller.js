'use strict';

angular.module('adapApp')
    .controller('AssetController', function ($scope, Asset, AssetService,$resource, AssetSearch, ParseLinks,Filter) {
        $scope.assets = [];
        $scope.searchdata = [];
        $scope.page = 0;
        
        $scope.loadFilters = function() {
            Filter.filtersByRecordtype({name: "Asset"}, function(data) {
            	$scope.filters = data;
              });
            }
            $scope.loadFilters();
        
        $scope.loadAll = function() {
            Asset.query({page: $scope.page, size: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.assets = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
         //   $scope.loadAll();
        };
       // $scope.loadAll();

        $scope.delete = function (id) {
            Asset.get({id: id}, function(result) {
                $scope.asset = result;
                $('#deleteAssetConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
	
        	Asset.delete({id: id},
                function () {
        		$scope.gridOptions.data.splice(id,1);
            $('#deleteAssetConfirmation').modal('hide');
                    $scope.clear();
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
            AssetSearch.query({query: $scope.searchQuery}, function(result) {
            	$scope.gridOptions.totalItems=result.length;
                $scope.assets = result;
 	    		$scope.searchdata=result;
 	    		getPagesearch();
                console.log(result)
               });
        	}
        };
        
        
        $scope.index=function(){ 
            Asset.index();
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.asset = {
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
        $scope.filterId;
       
        $scope.getfilter=function(id){ 
        	$scope.filterId=id;
            console.log(id+""+$scope.filterId);
        };
        
        $scope.executefilter=function(id){ 
        	$scope.filterId=id;
            console.log(id+""+$scope.filterId);
        	 Asset.executefilter({id:$scope.filterId}, function(output) {
             	$scope.gridOptions.totalItems=output.length;
                $scope.assets = output;
  	    		$scope.searchdata=output;
 	    		getPagesearch();
             });
        };
        
        $scope.editfilter=function(){ 
       	 Asset.editfilter({id:$scope.filterId}, function(output) {
                var rulejson=JSON.stringify(output.queryspringdata)
                $('#builder').queryBuilder({
                    filters: [ {
                        id: 'name',
                        label: 'Name',
                        type: 'string',
                        operators: ['equal', 'not_equal', 'in', 'not_in']
                    }, {
                        id: 'status',
                        label: 'Status',
                        type: 'string',
                        operators: ['equal', 'not_equal', 'in', 'not_in']
                    }, {
                        id: 'lastmodifiedby',
                        label: 'Lastmodifiedby',
                        type: 'string',
                        operators: ['equal', 'not_equal', 'in', 'not_in']
                    } , {
                    	
                        id: 'lastmodifieddate',
                        label: 'Lastmodifieddate',
                        type: 'date',
                        operators: ['equal', 'not_equal', 'in', 'not_in']
                    } , {
                        id: 'domain',
                        label: 'Domain',
                        type: 'string',
                        operators: ['equal', 'not_equal', 'in', 'not_in']
                    }  ]
                  });
                console.log(output.queryelastic);
                console.log(rulejson)
                $('#buildermodal').modal('show');
                $('#builder').queryBuilder('setRules', rulejson);
            });
       };
       
       
       var x=0;
 	    var page=0; var size=2500;
 	     function getcount(){
 	    	AssetService.count().then(function(obj){
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
       	                 { field: 'id',  displayName: 'ID', width: 60, enableSorting: true },
       	                 { field: 'name', displayName: 'Name', enableSorting: true },
                         { field: 'objclassification.name', displayName: 'Class', enableSorting: true },
                         { field: 'objcategory.name', displayName: 'Category', enableSorting: true },
       	                 { field: 'domain',displayName: 'Domain', enableSorting: true },
       	                 { name: 'Action',
       		            	field: 'action',enableFiltering: false,enableSorting: false,
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
       	    	 // var grid = this.grid;
       	    	 // if( grid.columns[0].filters[0].term === '' ) {
       	    	 //	  getcount();
       	    	 //  }else{
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
        	  	
        	    $scope.chartdata=[]
     			for(var i=0;i<$scope.gridOptions.data.length;++i){
     				$scope.chartdata.push({x: $scope.gridOptions.data[i].name,y: $scope.gridOptions.data[i].id})
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
        	            data: $scope.chartdata
        	        }]
        	    });
           		 });
            	  }
            	 
         	 
       	  getPage();
       	  

       	 function getPagesearch() {
  	    	$scope.gridOptions.data = $scope.searchdata.slice((paginationOptions.pageNumber - 1) * paginationOptions.pageSize, ((paginationOptions.pageNumber - 1) * paginationOptions.pageSize) + paginationOptions.pageSize);
     	  	
     	    $scope.chartdata=[]
  			for(var i=0;i<$scope.gridOptions.data.length;++i){
  				$scope.chartdata.push({x: $scope.gridOptions.data[i].name,y: $scope.gridOptions.data[i].id})
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
     	            data: $scope.chartdata
     	        }]
     	    });
         	  }
       	  
       
       
       
    });
