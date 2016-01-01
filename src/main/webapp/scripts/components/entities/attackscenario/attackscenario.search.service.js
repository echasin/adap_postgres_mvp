'use strict';

angular.module('adapApp')
    .factory('AttackscenarioSearch', function ($resource) {
        return $resource('api/_search/attackscenarios/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
