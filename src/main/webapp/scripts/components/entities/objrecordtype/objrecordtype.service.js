'use strict';

angular.module('adapApp')
    .factory('Objrecordtype', function ($resource, DateUtils) {
        return $resource('api/objrecordtypes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'getrecordtypes': { method: 'GET', isArray: true, url: 'api/objrecordtypes'},
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
