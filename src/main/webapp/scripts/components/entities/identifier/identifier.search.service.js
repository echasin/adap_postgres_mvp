'use strict';

angular.module('adapApp')
    .factory('IdentifierSearch', function ($resource) {
        return $resource('api/_search/identifiers/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
