'use strict';

angular.module('adapApp')
    .factory('ObjtypeSearch', function ($resource) {
        return $resource('api/_search/objtypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
