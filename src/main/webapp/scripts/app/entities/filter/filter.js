'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('filter', {
                parent: 'entity',
                url: '/filters',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.filter.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/filter/filters.html',
                        controller: 'FilterController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('filter');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('filter.detail', {
                parent: 'entity',
                url: '/filter/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.filter.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/filter/filter-detail.html',
                        controller: 'FilterDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('filter');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Filter', function($stateParams, Filter) {
                        return Filter.get({id : $stateParams.id});
                    }]
                }
            })
            .state('filter.new', {
                parent: 'filter',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/filter/filter-dialog.html',
                        controller: 'FilterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    querysql: null,
                                    queryspringdata: null,
                                    queryelastic: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('filter', null, { reload: true });
                    }, function() {
                        $state.go('filter');
                    })
                }]
            })
            .state('filter.edit', {
                parent: 'filter',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/filter/filter-dialog.html',
                        controller: 'FilterDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Filter', function(Filter) {
                                return Filter.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('filter', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
