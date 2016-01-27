'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('eventmbr', {
                parent: 'entity',
                url: '/eventmbrs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.eventmbr.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventmbr/eventmbrs.html',
                        controller: 'EventmbrController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('eventmbr');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('eventmbr.detail', {
                parent: 'entity',
                url: '/eventmbr/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.eventmbr.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/eventmbr/eventmbr-detail.html',
                        controller: 'EventmbrDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('eventmbr');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Eventmbr', function($stateParams, Eventmbr) {
                        return Eventmbr.get({id : $stateParams.id});
                    }]
                }
            })
            .state('eventmbr.new', {
                parent: 'eventmbr',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/eventmbr/eventmbr-dialog.html',
                        controller: 'EventmbrDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    status: null,
                                    lastmodifiedby: null,
                                    lastmodifieddate: null,
                                    domain: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('eventmbr', null, { reload: true });
                    }, function() {
                        $state.go('eventmbr');
                    })
                }]
            })
            .state('eventmbr.edit', {
                parent: 'eventmbr',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/eventmbr/eventmbr-dialog.html',
                        controller: 'EventmbrDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Eventmbr', function(Eventmbr) {
                                return Eventmbr.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('eventmbr', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('eventmbr.delete', {
                parent: 'eventmbr',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/eventmbr/eventmbr-delete-dialog.html',
                        controller: 'EventmbrDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Eventmbr', function(Eventmbr) {
                                return Eventmbr.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('eventmbr', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
