'use strict';

angular.module('adapApp')
    .factory('ObjcategorySearch', function ($resource) {
        return $resource('api/_search/objcategorys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
