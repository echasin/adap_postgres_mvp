'use strict';

angular.module('adapApp')
    .factory('RouteSearch', function ($resource) {
        return $resource('api/_search/routes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
