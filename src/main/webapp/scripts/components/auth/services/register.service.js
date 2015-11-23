'use strict';

angular.module('adapApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


