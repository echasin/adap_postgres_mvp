'use strict';

angular.module('adapApp')
    .factory('Location', function ($resource, DateUtils) {
        return $resource('api/locations/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'getlocations': {method: 'GET', isArray: true, params: {page: '@page',size: '@size'}, url: 'api/locations/:page/:size'},
            'locationByIsprimary': {method: 'GET', params: {assetId: '@assetId'}, url: 'api/locationIsprimary/:assetId'},
            'index': {method: 'GET', url: 'api/indexLocation'},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.lastmodifieddate = DateUtils.convertDateTimeFromServer(data.lastmodifieddate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }).factory('LocationService', function ($http) {
      	 return {
 	        count: function(name) {
 	            var promise = $http.get('api/location/recordsLength').then(function (response) {
 	            	return response.data;
 	            });
 	            return promise;
 	        }
 	 }
 });
