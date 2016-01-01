'use strict';

angular.module('adapApp')
    .factory('Filter', function ($resource, DateUtils) {
        return $resource('api/filters/:id', {}, {
            'filtersByRecordtype': { method: 'GET', isArray: true,url: 'api/filtersByRecordtype'},
            'query': { method: 'GET', isArray: true},
            'saveES': {method: 'GET', params: {rulejson:'@rulejson',esjson: '@esjson',id:'@id'}, url: 'api/saveES/:rulejson/:esjson'},
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
