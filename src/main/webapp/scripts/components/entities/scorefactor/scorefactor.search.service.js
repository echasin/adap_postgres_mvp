'use strict';

angular.module('adapApp')
    .factory('ScorefactorSearch', function ($resource) {
        return $resource('api/_search/scorefactors/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
