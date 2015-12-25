'use strict';

angular.module('adapApp')
    .factory('SegmentSearch', function ($resource) {
        return $resource('api/_search/segments/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
