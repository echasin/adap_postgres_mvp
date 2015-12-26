'use strict';

angular.module('adapApp')
    .factory('FilterSearch', function ($resource) {
        return $resource('api/_search/filters/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
