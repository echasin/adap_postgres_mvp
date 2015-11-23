'use strict';

angular.module('adapApp')
    .factory('ObjrecordtypeSearch', function ($resource) {
        return $resource('api/_search/objrecordtypes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
