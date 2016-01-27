'use strict';

angular.module('adapApp')
    .factory('EventmbrSearch', function ($resource) {
        return $resource('api/_search/eventmbrs/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
