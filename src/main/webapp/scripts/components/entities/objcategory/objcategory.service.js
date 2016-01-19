'use strict';

angular.module('adapApp')
    .factory('Objcategory', function ($resource, DateUtils) {
        return $resource('api/objcategorys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'getcategories': {method: 'GET',isArray: true, params: {id:'@id'}, url: 'api/categorys/classification/:id'},
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
