'use strict';

angular.module('adapApp')
    .factory('Objclassification', function ($resource, DateUtils) {
        return $resource('api/objclassifications/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'getclassifications': {method: 'GET',isArray: true, params: {id:'@id'}, url: 'api/classifications/recordtype/:id'},
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
