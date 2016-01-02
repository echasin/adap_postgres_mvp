'use strict';

angular.module('adapApp')
    .factory('Asset', function ($resource, DateUtils) {
        return $resource('api/assets/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'executefilter': {method: 'GET',isArray: true, params: {id:'@id'}, url: 'api/executefilter/:id'},
            'editfilter': {method: 'GET', params: {id:'@id'}, url: 'api/editfilter/:id'},
            'getassets': {method: 'GET', isArray: true, params: {page: '@page',size: '@size'}, url: 'api/assets/:page/:size'},
            'index': {method: 'GET', url: 'api/indexasset'},
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
    }).factory('AssetService', function ($http) {
          	 return {
          	        count: function(name) {
          	            var promise = $http.get('api/asset/recordsLength').then(function (response) {
          	            	return response.data;
          	            });
          	            return promise;
          	        }
          	 }
          });
