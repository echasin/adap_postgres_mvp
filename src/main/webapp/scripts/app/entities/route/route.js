'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('route', {
                parent: 'entity',
                url: '/routes',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.route.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route/routes.html',
                        controller: 'RouteController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('route.detail', {
                parent: 'entity',
                url: '/route/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.route.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route/route-detail.html',
                        controller: 'RouteDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Route', function($stateParams, Route) {
                        return Route.get({id : $stateParams.id});
                    }]
                }
            })
               .state('route.score', {
                parent: 'entity',
                url: '/routescore/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.route.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/route/RouteScoreDetails.html',
                        controller: 'RouteScoreDetailsController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('route');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Route', function($stateParams, Route) {
                        return Route.get({id : $stateParams.id});
                    }]
                }
            })
            .state('route.new', {
                parent: 'route',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/route/route-dialog.html',
                        controller: 'RouteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    description: null,
                                    details: null,
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('route', null, { reload: true });
                    }, function() {
                        $state.go('route');
                    })
                }]
            })
            .state('route.edit', {
                parent: 'route',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/route/route-dialog.html',
                        controller: 'RouteDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Route', function(Route) {
                                return Route.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('route', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('route.delete', {
                parent: 'route',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/route/route-delete-dialog.html',
                        controller: 'RouteDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Route', function(Route) {
                                return Route.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('route', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
