'use strict';

angular.module('adapApp')
    .factory('ScoreSearch', function ($resource) {
        return $resource('api/_search/scores/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
