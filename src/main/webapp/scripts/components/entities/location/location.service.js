'use strict';

angular.module('adapApp')
    .factory('Location', function ($resource, DateUtils) {
        return $resource('api/locations/:id', {}, {
            'query': { method: 'GET', isArray: true},
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
    });
