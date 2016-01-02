'use strict';

angular.module('adapApp')
    .factory('Score', function ($resource, DateUtils) {
        return $resource('api/scores/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'executefilter': {method: 'GET',isArray: true, params: {id:'@id'}, url: 'api/executefilter/:id'},
            'editfilter': {method: 'GET', params: {id:'@id'}, url: 'api/editfilter/:id'},
            'getroutes': {method: 'GET', isArray: true, params: {page: '@page',size: '@size'}, url: 'api/scores/:page/:size'},
            'index': {method: 'GET', url: 'api/indexscore'},
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
    }).factory('ScoreService', function ($http) {
   	 return {
	        count: function(name) {
	            var promise = $http.get('api/score/recordsLength').then(function (response) {
	            	return response.data;
	            });
	            return promise;
	        }
	 }
});
