'use strict';

angular.module('adapApp')
    .factory('Segment', function ($resource, DateUtils) {
        return $resource('api/segments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'executefilter': {method: 'GET',isArray: true, params: {id:'@id'}, url: 'api/executefilter/:id'},
            'editfilter': {method: 'GET', params: {id:'@id'}, url: 'api/editfilter/:id'},
            'getsegments': {method: 'GET', isArray: true, params: {page: '@page',size: '@size'}, url: 'api/segments/:page/:size'},
            'index': {method: 'GET', url: 'api/indexsegment'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.origineta = DateUtils.convertDateTimeFromServer(data.origineta);
                    data.originAta = DateUtils.convertDateTimeFromServer(data.originAta);
                    data.destinationeta = DateUtils.convertDateTimeFromServer(data.destinationeta);
                    data.destinationata = DateUtils.convertDateTimeFromServer(data.destinationata);
                    data.lastmodifieddate = DateUtils.convertDateTimeFromServer(data.lastmodifieddate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }).factory('SegmentService', function ($http) {
     	 return {
   	        count: function(name) {
   	            var promise = $http.get('api/segment/recordsLength').then(function (response) {
   	            	return response.data;
   	            });
   	            return promise;
   	        }
   	 }
   });
