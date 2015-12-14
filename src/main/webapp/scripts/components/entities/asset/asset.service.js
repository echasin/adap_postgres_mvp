'use strict';

angular.module('adapApp')
        .factory('Asset', function ($resource, DateUtils) {
            return $resource('api/assets/:id', {}, {
                'query': {method: 'GET', isArray: true},
                'getassets': {method: 'GET', isArray: true, params: {page: '@page', size: '@size'}, url: 'api/assets/:page/:size'},
                'recordsLength': {method: 'GET', isArray: false, url: 'api/asset/recordsLength'},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        data.lastmodifieddate = DateUtils.convertDateTimeFromServer(data.lastmodifieddate);
                        return data;
                    }
                },
                'update': {method: 'PUT'}
            });
        })
        .factory('Testrecord', function ($http) {
            return {
                count: function (name) {
                    var promise = $http.get('api/asset/recordsLength').then(function (response) {
                        return response.data;
                    });
                    return promise;
                },
          
            }
        });