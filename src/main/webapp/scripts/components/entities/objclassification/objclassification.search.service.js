'use strict';

angular.module('adapApp')
    .factory('ObjclassificationSearch', function ($resource) {
        return $resource('api/_search/objclassifications/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
