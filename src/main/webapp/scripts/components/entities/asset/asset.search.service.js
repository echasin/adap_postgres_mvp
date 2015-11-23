'use strict';

angular.module('adapApp')
    .factory('AssetSearch', function ($resource) {
        return $resource('api/_search/assets/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
