'use strict';

angular.module('adapApp')
    .controller('FilterDetailController', function ($scope, $rootScope, $stateParams, entity, Filter,Asset, Objrecordtype, Objclassification, Objcategory, Objtype, Location) {
        $scope.filter = entity;
        $scope.load = function (id) {
            Filter.get({id: id}, function(result) {
                $scope.filter = result;
            });
        };
        var unsubscribe = $rootScope.$on('adapApp:filterUpdate', function(event, result) {
            $scope.filter = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $('#builder').queryBuilder({
            filters: [ {
                id: 'id',
                label: 'Id',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            }, {
                id: 'name',
                label: 'Name',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            }, {
                id: 'status',
                label: 'Status',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            }, {
                id: 'lastmodifiedby',
                label: 'Lastmodifiedby',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            } , {
                id: 'lastmodifieddate',
                label: 'Lastmodifieddate',
                type: 'date',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            } , {
                id: 'domain',
                label: 'Domain',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            } , {
                id: 'objrecordtype.name',
                label: 'Recordtype Name',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            } , {
                id: 'objclassification.name',
                label: 'Classification Name',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            } , {
                id: 'objcategory.name',
                label: 'Category Name',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            } , {
                id: 'objtype.name',
                label: 'Type Name',
                type: 'string',
                operators: ['equal', 'not_equal', 'in', 'between','less','less_or_equal','greater','greater_or_equal']
            }  ]
          });

                
        //set querybuilder values
        if(entity.queryspringdata) {
        	  var parsed =JSON.parse(entity.queryspringdata );
              $('#builder').queryBuilder('setRules', parsed);
        }
     

        $('#btn-reset').on('click', function() {
         	  $('#builder').queryBuilder('reset');
         	});

         	$('#btn-set').on('click', function() {
         	  $('#builder').queryBuilder('setRules', JSON.parse(parsed));
         	});

         	$('#btn-get').on('click', function() {
         	  var result = $('#builder').queryBuilder('getRules');
         	  console.log("output as json")
         	  if (!$.isEmptyObject(result)) {
         	    alert(JSON.stringify(result, null, 2));
         	  }
         	});
         	

         	$('#btn-getsql').on('click', function() {
         	  var rulesresult = $('#builder').queryBuilder('getRules');
           	  var sqlresult = $('#builder').queryBuilder('getSQL','question_mark');
           	  console.log("output as sql")
           	  console.log(sqlresult);
           	  if (!$.isEmptyObject(sqlresult)) {
           	    alert(sqlresult);
           	  }
           	});
         	
         	$('#btn-saveES').on('click', function() {
         		var ruleresult = $('#builder').queryBuilder('getRules');
         		var esresult =  $('#builder').queryBuilder('getESBool');
             	  console.log("output as ES")
             	  console.log(ruleresult)
             	  if (!$.isEmptyObject(esresult)) {
             	    console.log(JSON.stringify(esresult));
             	    var rulejson=JSON.stringify(ruleresult)
             	    var esjson=JSON.stringify(esresult)
             	    console.log()
             	    Filter.saveES({id:$stateParams.id,rulejson:rulejson,esjson: esjson}, function(output) {
                       $scope.elasticquery = output;
                   });
             	  }
             	});
         	
         	 $scope.executefilter=function(){ 
                 console.log($stateParams.id);
             	 Asset.executefilter({id:$stateParams.id}, function(output) {
                     $scope.assets = output;
                     window.alert("Number of records : "+output.length +" Records");
                  });
             };
         	
    });
