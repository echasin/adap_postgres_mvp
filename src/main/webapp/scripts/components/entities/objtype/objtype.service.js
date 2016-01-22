'use strict';

angular.module('adapApp')
    .factory('Objtype', function ($resource, DateUtils) {
        return $resource('api/objtypes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'gettypes': {method: 'GET',isArray: true, params: {id:'@id'}, url: 'api/types/category/:id'},
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
