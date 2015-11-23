'use strict';

angular.module('adapApp')
    .factory('LocationSearch', function ($resource) {
        return $resource('api/_search/locations/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
