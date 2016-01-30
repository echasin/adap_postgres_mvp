'use strict';

angular.module('adapApp')
    .factory('Event', function ($resource, DateUtils) {
        return $resource('api/events/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'geteventsByAsset': {method: 'GET', isArray: true, params: {page: '@page',size: '@size'}, url: 'api/eventsByAsset/:page/:size'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.eventdate = DateUtils.convertDateTimeFromServer(data.eventdate);
                    data.lastmodifieddate = DateUtils.convertDateTimeFromServer(data.lastmodifieddate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }).factory('EventService', function ($http) {
     	 return {
  	        count: function(name) {
  	            var promise = $http.get('api/event/recordsLength').then(function (response) {
  	            	return response.data;
  	            });
  	            return promise;
  	        }
  	 }
  });
