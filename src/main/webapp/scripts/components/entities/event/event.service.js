'use strict';

angular.module('adapApp')
    .factory('Event', function ($resource, DateUtils) {
        return $resource('api/events/:id', {}, {
            'query': { method: 'GET', isArray: true},
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
    });
