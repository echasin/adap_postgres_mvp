'use strict';

angular.module('adapApp')
    .factory('Segment', function ($resource, DateUtils) {
        return $resource('api/segments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.origineta = DateUtils.convertDateTimeFromServer(data.origineta);
                    data.originAta = DateUtils.convertDateTimeFromServer(data.originAta);
                    data.destinationeta = DateUtils.convertDateTimeFromServer(data.destinationeta);
                    data.destinationata = DateUtils.convertDateTimeFromServer(data.destinationata);
                    data.lastmodifieddate = DateUtils.convertDateTimeFromServer(data.lastmodifieddate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
