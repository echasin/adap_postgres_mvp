'use strict';

angular.module('adapApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('attackscenario', {
                parent: 'entity',
                url: '/attackscenarios',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.attackscenario.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attackscenario/attackscenarios.html',
                        controller: 'AttackscenarioController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attackscenario');
                        $translatePartialLoader.addPart('status');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('attackscenario.detail', {
                parent: 'entity',
                url: '/attackscenario/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'adapApp.attackscenario.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attackscenario/attackscenario-detail.html',
                        controller: 'AttackscenarioDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attackscenario');
                        $translatePartialLoader.addPart('status');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Attackscenario', function($stateParams, Attackscenario) {
                        return Attackscenario.get({id : $stateParams.id});
                    }]
                }
            })
            .state('attackscenario.new', {
                parent: 'attackscenario',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/attackscenario/attackscenario-dialog.html',
                        controller: 'AttackscenarioDialogController',
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
                        $state.go('attackscenario', null, { reload: true });
                    }, function() {
                        $state.go('attackscenario');
                    })
                }]
            })
            .state('attackscenario.edit', {
                parent: 'attackscenario',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/attackscenario/attackscenario-dialog.html',
                        controller: 'AttackscenarioDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Attackscenario', function(Attackscenario) {
                                return Attackscenario.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('attackscenario', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('attackscenario.delete', {
                parent: 'attackscenario',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/attackscenario/attackscenario-delete-dialog.html',
                        controller: 'AttackscenarioDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Attackscenario', function(Attackscenario) {
                                return Attackscenario.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('attackscenario', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
