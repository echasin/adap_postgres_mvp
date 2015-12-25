'use strict';

angular.module('adapApp')
    .factory('Identifier', function ($resource, DateUtils) {
        return $resource('api/identifiers/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.effectivedatetime = DateUtils.convertDateTimeFromServer(data.effectivedatetime);
                    data.enddatetime = DateUtils.convertDateTimeFromServer(data.enddatetime);
                    data.lastmodifieddate = DateUtils.convertDateTimeFromServer(data.lastmodifieddate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
